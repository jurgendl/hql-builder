package org.springframework.orm.hibernate4;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.query.Query;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@SuppressWarnings("deprecation")
public class HibernateTemplate implements HibernateOperations {
	@Resource
	private SessionFactory sessionFactory;

	private <T> T executeWithNativeSession(HibernateCallback<T> hibernateCallback) {
		return doExecute(hibernateCallback);
	}

	/**
	 * Execute the action specified by the given action object within a {@link org.hibernate.Session}.
	 * <p>
	 * Application exceptions thrown by the action object get propagated to the caller (can only be unchecked). Hibernate exceptions are transformed into appropriate DAO ones.
	 * Allows for returning a result object, that is a domain object or a collection of domain objects.
	 * <p>
	 * Note: Callback code is not supposed to handle transactions itself! Use an appropriate transaction manager like {@link HibernateTransactionManager}. Generally, callback code
	 * must not touch any {@code Session} lifecycle methods, like close, disconnect, or reconnect, to let the template do its work.
	 * 
	 * @param action callback object that specifies the Hibernate action
	 * @return a result object returned by the action, or {@code null}
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see HibernateTransactionManager
	 * @see org.hibernate.Session
	 */
	@Override
	public <T> T execute(final org.springframework.orm.hibernate3.HibernateCallback<T> action) throws DataAccessException {
		return doExecute(action);
	}

	/**
	 * Execute the specified action assuming that the result object is a {@link List}.
	 * <p>
	 * This is a convenience method for executing Hibernate find calls or queries within an action.
	 * 
	 * @param action calback object that specifies the Hibernate action
	 * @return a List result returned by the action, or {@code null}
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 */
	@Override
	public List<?> executeFind(final org.springframework.orm.hibernate3.HibernateCallback<?> action) throws DataAccessException {
		Object result = doExecute(action);
		if (result != null && !(result instanceof List)) {
			throw new InvalidDataAccessApiUsageException("Result object returned from HibernateCallback isn't a List: [" + result + "]");
		}
		return (List<?>) result;
	}

	private <T> T doExecute(final org.springframework.orm.hibernate3.HibernateCallback<T> action) throws DataAccessException {
		Assert.notNull(action, "Callback object must not be null");
		Session session;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (org.hibernate.HibernateException ex) {
			// org.hibernate.HibernateException: No Session found for current thread
			if ("No Session found for current thread".equals(ex.getMessage())) {
				session = sessionFactory.openSession();
			} else {
				throw ex;
			}
		}
		try {
			T result = action.doInHibernate(session);
			return result;
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		} catch (SQLException ex) {
			throw convertJdbcAccessException(ex);
		} catch (RuntimeException ex) {
			// Callback code threw application exception...
			throw ex;
		}
	}

	private SQLExceptionTranslator jdbcExceptionTranslator;

	private SQLExceptionTranslator defaultJdbcExceptionTranslator;

	public SQLExceptionTranslator getJdbcExceptionTranslator() {
		return this.jdbcExceptionTranslator;
	}

	public void setJdbcExceptionTranslator(final SQLExceptionTranslator jdbcExceptionTranslator) {
		this.jdbcExceptionTranslator = jdbcExceptionTranslator;
	}

	public SQLExceptionTranslator getDefaultJdbcExceptionTranslator() {
		return this.defaultJdbcExceptionTranslator;
	}

	public void setDefaultJdbcExceptionTranslator(final SQLExceptionTranslator defaultJdbcExceptionTranslator) {
		this.defaultJdbcExceptionTranslator = defaultJdbcExceptionTranslator;
	}

	/**
	 * Convert the given HibernateException to an appropriate exception from the {@code org.springframework.dao} hierarchy.
	 * <p>
	 * Will automatically apply a specified SQLExceptionTranslator to a Hibernate JDBCException, else rely on Hibernate's default translation.
	 * 
	 * @param ex HibernateException that occured
	 * @return a corresponding DataAccessException
	 * @see SessionFactoryUtils2#convertHibernateAccessException
	 * @see #setJdbcExceptionTranslator
	 */
	public DataAccessException convertHibernateAccessException(final HibernateException ex) {
		if (getJdbcExceptionTranslator() != null && ex instanceof JDBCException) {
			return convertJdbcAccessException((JDBCException) ex, getJdbcExceptionTranslator());
		} else if (GenericJDBCException.class.equals(ex.getClass())) {
			return convertJdbcAccessException((GenericJDBCException) ex, getDefaultJdbcExceptionTranslator());
		}
		return SessionFactoryUtils2.convertHibernateAccessException(ex);
	}

	/**
	 * Convert the given Hibernate JDBCException to an appropriate exception from the {@code org.springframework.dao} hierarchy, using the given SQLExceptionTranslator.
	 * 
	 * @param ex Hibernate JDBCException that occured
	 * @param translator the SQLExceptionTranslator to use
	 * @return a corresponding DataAccessException
	 */
	private DataAccessException convertJdbcAccessException(final JDBCException ex, final SQLExceptionTranslator translator) {
		return translator.translate("Hibernate operation: " + ex.getMessage(), ex.getSQL(), ex.getSQLException());
	}

	/**
	 * Convert the given SQLException to an appropriate exception from the {@code org.springframework.dao} hierarchy. Can be overridden in subclasses.
	 * <p>
	 * Note that a direct SQLException can just occur when callback code performs direct JDBC access via {@code Session.connection()}.
	 * 
	 * @param ex the SQLException
	 * @return the corresponding DataAccessException instance
	 * @see #setJdbcExceptionTranslator
	 */
	private DataAccessException convertJdbcAccessException(final SQLException ex) {
		SQLExceptionTranslator translator = getJdbcExceptionTranslator();
		if (translator == null) {
			translator = getDefaultJdbcExceptionTranslator();
		}
		return translator.translate("Hibernate-related JDBC operation", null, ex);
	}

	// -------------------------------------------------------------------------
	// Convenience methods for loading individual objects
	// -------------------------------------------------------------------------

	/**
	 * Return the persistent instance of the given entity class with the given identifier, or {@code null} if not found.
	 * <p>
	 * This method is a thin wrapper around {@link org.hibernate.Session#get(Class, java.io.Serializable)} for convenience. For an explanation of the exact semantics of this
	 * method, please do refer to the Hibernate API documentation in the first instance.
	 * 
	 * @param entityClass a persistent class
	 * @param id the identifier of the persistent instance
	 * @return the persistent instance, or {@code null} if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#get(Class, java.io.Serializable)
	 */
	@Override
	public <T> T get(final Class<T> entityClass, final Serializable id) throws DataAccessException {
		return get(entityClass, id, null);
	}

	/**
	 * Return the persistent instance of the given entity class with the given identifier, or {@code null} if not found.
	 * <p>
	 * Obtains the specified lock mode if the instance exists.
	 * <p>
	 * This method is a thin wrapper around {@link org.hibernate.Session#get(Class, java.io.Serializable, LockMode)} for convenience. For an explanation of the exact semantics of
	 * this method, please do refer to the Hibernate API documentation in the first instance.
	 * 
	 * @param entityClass a persistent class
	 * @param id the identifier of the persistent instance
	 * @param lockMode the lock mode to obtain
	 * @return the persistent instance, or {@code null} if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#get(Class, java.io.Serializable, org.hibernate.LockMode)
	 */
	@Override
	public <T> T get(final Class<T> entityClass, final Serializable id, final LockMode lockMode) throws DataAccessException {
		return executeWithNativeSession(session -> {
			if (lockMode != null) {
				return entityClass.cast(session.get(entityClass, id, new LockOptions(lockMode)));
			}
			return entityClass.cast(session.get(entityClass, id));
		});
	}

	/**
	 * Return the persistent instance of the given entity class with the given identifier, or {@code null} if not found.
	 * <p>
	 * This method is a thin wrapper around {@link org.hibernate.Session#get(String, java.io.Serializable)} for convenience. For an explanation of the exact semantics of this
	 * method, please do refer to the Hibernate API documentation in the first instance.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param id the identifier of the persistent instance
	 * @return the persistent instance, or {@code null} if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#get(Class, java.io.Serializable)
	 */
	@Override
	public Object get(final String entityName, final Serializable id) throws DataAccessException {
		return get(entityName, id, null);
	}

	/**
	 * Return the persistent instance of the given entity class with the given identifier, or {@code null} if not found. Obtains the specified lock mode if the instance exists.
	 * <p>
	 * This method is a thin wrapper around {@link org.hibernate.Session#get(String, java.io.Serializable, LockMode)} for convenience. For an explanation of the exact semantics of
	 * this method, please do refer to the Hibernate API documentation in the first instance.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param id the identifier of the persistent instance
	 * @param lockMode the lock mode to obtain
	 * @return the persistent instance, or {@code null} if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#get(Class, java.io.Serializable, org.hibernate.LockMode)
	 */
	@Override
	public Object get(final String entityName, final Serializable id, final LockMode lockMode) throws DataAccessException {
		return executeWithNativeSession(session -> {
			if (lockMode != null) {
				return session.get(entityName, id, new LockOptions(lockMode));
			}
			return session.get(entityName, id);
		});
	}

	/**
	 * Return the persistent instance of the given entity class with the given identifier, throwing an exception if not found.
	 * <p>
	 * This method is a thin wrapper around {@link org.hibernate.Session#load(Class, java.io.Serializable)} for convenience. For an explanation of the exact semantics of this
	 * method, please do refer to the Hibernate API documentation in the first instance.
	 * 
	 * @param entityClass a persistent class
	 * @param id the identifier of the persistent instance
	 * @return the persistent instance
	 * @throws org.springframework.orm.ObjectRetrievalFailureException if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#load(Class, java.io.Serializable)
	 */
	@Override
	public <T> T load(final Class<T> entityClass, final Serializable id) throws DataAccessException {
		return load(entityClass, id, null);
	}

	/**
	 * Return the persistent instance of the given entity class with the given identifier, throwing an exception if not found. Obtains the specified lock mode if the instance
	 * exists.
	 * <p>
	 * This method is a thin wrapper around {@link org.hibernate.Session#load(Class, java.io.Serializable, LockMode)} for convenience. For an explanation of the exact semantics of
	 * this method, please do refer to the Hibernate API documentation in the first instance.
	 * 
	 * @param entityClass a persistent class
	 * @param id the identifier of the persistent instance
	 * @param lockMode the lock mode to obtain
	 * @return the persistent instance
	 * @throws org.springframework.orm.ObjectRetrievalFailureException if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#load(Class, java.io.Serializable)
	 */
	@Override
	public <T> T load(final Class<T> entityClass, final Serializable id, final LockMode lockMode) throws DataAccessException {
		return executeWithNativeSession(session -> {
			if (lockMode != null) {
				return entityClass.cast(session.load(entityClass, id, new LockOptions(lockMode)));
			}
			return entityClass.cast(session.load(entityClass, id));
		});
	}

	/**
	 * Return the persistent instance of the given entity class with the given identifier, throwing an exception if not found.
	 * <p>
	 * This method is a thin wrapper around {@link org.hibernate.Session#load(String, java.io.Serializable)} for convenience. For an explanation of the exact semantics of this
	 * method, please do refer to the Hibernate API documentation in the first instance.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param id the identifier of the persistent instance
	 * @return the persistent instance
	 * @throws org.springframework.orm.ObjectRetrievalFailureException if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#load(Class, java.io.Serializable)
	 */
	@Override
	public Object load(final String entityName, final Serializable id) throws DataAccessException {
		return load(entityName, id, null);
	}

	/**
	 * Return the persistent instance of the given entity class with the given identifier, throwing an exception if not found.
	 * <p>
	 * Obtains the specified lock mode if the instance exists.
	 * <p>
	 * This method is a thin wrapper around {@link org.hibernate.Session#load(String, java.io.Serializable, LockMode)} for convenience. For an explanation of the exact semantics of
	 * this method, please do refer to the Hibernate API documentation in the first instance.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param id the identifier of the persistent instance
	 * @param lockMode the lock mode to obtain
	 * @return the persistent instance
	 * @throws org.springframework.orm.ObjectRetrievalFailureException if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#load(Class, java.io.Serializable)
	 */
	@Override
	public Object load(final String entityName, final Serializable id, final LockMode lockMode) throws DataAccessException {
		return executeWithNativeSession(session -> {
			if (lockMode != null) {
				return session.load(entityName, id, new LockOptions(lockMode));
			}
			return session.load(entityName, id);
		});
	}

	/**
	 * Return all persistent instances of the given entity class. Note: Use queries or criteria for retrieving a specific subset.
	 * 
	 * @param entityClass a persistent class
	 * @return a {@link List} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException if there is a Hibernate error
	 * @see org.hibernate.Session#createCriteria
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> loadAll(final Class<T> entityClass) throws DataAccessException {
		return executeWithNativeSession(session -> {
			Criteria criteria = session.createCriteria(entityClass);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			prepareCriteria(criteria);
			return criteria.list();
		});
	}

	private boolean cacheQueries = false;

	private String queryCacheRegion;

	private int fetchSize = 0;

	private int maxResults = 200;

	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public boolean isCacheQueries() {
		return this.cacheQueries;
	}

	public void setCacheQueries(boolean cacheQueries) {
		this.cacheQueries = cacheQueries;
	}

	public String getQueryCacheRegion() {
		return this.queryCacheRegion;
	}

	public void setQueryCacheRegion(String queryCacheRegion) {
		this.queryCacheRegion = queryCacheRegion;
	}

	public int getFetchSize() {
		return this.fetchSize;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	public int getMaxResults() {
		return this.maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	/**
	 * Prepare the given Criteria object, applying cache settings and/or a transaction timeout.
	 * 
	 * @param criteria the Criteria object to prepare
	 * @see #setCacheQueries
	 * @see #setQueryCacheRegion
	 * @see SessionFactoryUtils2#applyTransactionTimeout
	 */
	private void prepareCriteria(Criteria criteria) {
		if (isCacheQueries()) {
			criteria.setCacheable(true);
			if (getQueryCacheRegion() != null) {
				criteria.setCacheRegion(getQueryCacheRegion());
			}
		}
		if (getFetchSize() > 0) {
			criteria.setFetchSize(getFetchSize());
		}
		if (getMaxResults() > 0) {
			criteria.setMaxResults(getMaxResults());
		}
		SessionFactoryUtils2.applyTransactionTimeout(criteria, getSessionFactory());
	}

	/**
	 * Load the persistent instance with the given identifier into the given object, throwing an exception if not found.
	 * <p>
	 * This method is a thin wrapper around {@link org.hibernate.Session#load(Object, java.io.Serializable)} for convenience. For an explanation of the exact semantics of this
	 * method, please do refer to the Hibernate API documentation in the first instance.
	 * 
	 * @param entity the object (of the target class) to load into
	 * @param id the identifier of the persistent instance
	 * @throws org.springframework.orm.ObjectRetrievalFailureException if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#load(Object, java.io.Serializable)
	 */
	@Override
	public void load(final Object entity, final Serializable id) throws DataAccessException {
		executeWithNativeSession(session -> {
			session.load(entity, id);
			return null;
		});
	}

	/**
	 * Re-read the state of the given persistent instance.
	 * 
	 * @param entity the persistent instance to re-read
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#refresh(Object)
	 */
	@Override
	public void refresh(final Object entity) throws DataAccessException {
		refresh(entity, null);
	}

	/**
	 * Re-read the state of the given persistent instance. Obtains the specified lock mode for the instance.
	 * 
	 * @param entity the persistent instance to re-read
	 * @param lockMode the lock mode to obtain
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#refresh(Object, org.hibernate.LockMode)
	 */
	@Override
	public void refresh(final Object entity, final LockMode lockMode) throws DataAccessException {
		executeWithNativeSession(session -> {
			if (lockMode != null) {
				session.refresh(entity, new LockOptions(lockMode));
			}
			session.refresh(entity);
			return null;
		});
	}

	/**
	 * Check whether the given object is in the Session cache.
	 * 
	 * @param entity the persistence instance to check
	 * @return whether the given object is in the Session cache
	 * @throws org.springframework.dao.DataAccessException if there is a Hibernate error
	 * @see org.hibernate.Session#contains
	 */
	@Override
	public boolean contains(final Object entity) throws DataAccessException {
		return executeWithNativeSession(session -> session.contains(entity));
	}

	/**
	 * Remove the given object from the {@link org.hibernate.Session} cache.
	 * 
	 * @param entity the persistent instance to evict
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#evict
	 */
	@Override
	public void evict(final Object entity) throws DataAccessException {
		executeWithNativeSession(session -> {
			session.evict(entity);
			return null;
		});
	}

	/**
	 * Force initialization of a Hibernate proxy or persistent collection.
	 * 
	 * @param proxy a proxy for a persistent object or a persistent collection
	 * @throws DataAccessException if we can't initialize the proxy, for example because it is not associated with an active Session
	 * @see org.hibernate.Hibernate#initialize
	 */
	@Override
	public void initialize(final Object proxy) throws DataAccessException {
		try {
			Hibernate.initialize(proxy);
		} catch (HibernateException ex) {
			throw SessionFactoryUtils2.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Return an enabled Hibernate {@link Filter} for the given filter name. The returned {@code Filter} instance can be used to set filter parameters.
	 * 
	 * @param filterName the name of the filter
	 * @return the enabled Hibernate {@code Filter} (either already enabled or enabled on the fly by this operation)
	 * @throws IllegalStateException if we are not running within a transactional Session (in which case this operation does not make sense)
	 */
	@Override
	public Filter enableFilter(final String filterName) throws IllegalStateException {
		Session session = SessionFactoryUtils2.getSession(getSessionFactory(), false);
		Filter filter = session.getEnabledFilter(filterName);
		if (filter == null) {
			filter = session.enableFilter(filterName);
		}
		return filter;
	}

	// -------------------------------------------------------------------------
	// Convenience methods for storing individual objects
	// -------------------------------------------------------------------------

	/**
	 * Obtain the specified lock level upon the given object, implicitly checking whether the corresponding database entry still exists.
	 * 
	 * @param entity the persistent instance to lock
	 * @param lockMode the lock mode to obtain
	 * @throws org.springframework.orm.ObjectOptimisticLockingFailureException if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#lock(Object, org.hibernate.LockMode)
	 */
	@Override
	public void lock(final Object entity, final LockMode lockMode) throws DataAccessException {
		executeWithNativeSession(session -> {
			session.buildLockRequest(new LockOptions(lockMode)).lock(entity);
			return null;
		});
	}

	/**
	 * Obtain the specified lock level upon the given object, implicitly checking whether the corresponding database entry still exists.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param entity the persistent instance to lock
	 * @param lockMode the lock mode to obtain
	 * @throws org.springframework.orm.ObjectOptimisticLockingFailureException if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#lock(String, Object, org.hibernate.LockMode)
	 */
	@Override
	public void lock(final String entityName, final Object entity, final LockMode lockMode) throws DataAccessException {
		executeWithNativeSession(session -> {
			session.buildLockRequest(new LockOptions(lockMode)).lock(entityName, entity);
			return null;
		});
	}

	/**
	 * Persist the given transient instance.
	 * 
	 * @param entity the transient instance to persist
	 * @return the generated identifier
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#save(Object)
	 */
	@Override
	public Serializable save(final Object entity) throws DataAccessException {
		return executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			return session.save(entity);
		});
	}

	private void checkWriteOperationAllowed(Session session) {
		// TODO check write operation allowed?
	}

	/**
	 * Persist the given transient instance.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param entity the transient instance to persist
	 * @return the generated identifier
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#save(String, Object)
	 */
	@Override
	public Serializable save(final String entityName, final Object entity) throws DataAccessException {
		return executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			return session.save(entityName, entity);
		});
	}

	/**
	 * Update the given persistent instance, associating it with the current Hibernate {@link org.hibernate.Session}.
	 * 
	 * @param entity the persistent instance to update
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#update(Object)
	 */
	@Override
	public void update(final Object entity) throws DataAccessException {
		update(entity, null);
	}

	/**
	 * Update the given persistent instance, associating it with the current Hibernate {@link org.hibernate.Session}.
	 * <p>
	 * Obtains the specified lock mode if the instance exists, implicitly checking whether the corresponding database entry still exists.
	 * 
	 * @param entity the persistent instance to update
	 * @param lockMode the lock mode to obtain
	 * @throws org.springframework.orm.ObjectOptimisticLockingFailureException if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#update(Object)
	 */
	@Override
	public void update(final Object entity, final LockMode lockMode) throws DataAccessException {
		executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			session.update(entity);
			if (lockMode != null) {
				session.buildLockRequest(new LockOptions(lockMode)).lock(entity);
			}
			return null;
		});
	}

	/**
	 * Update the given persistent instance, associating it with the current Hibernate {@link org.hibernate.Session}.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param entity the persistent instance to update
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#update(String, Object)
	 */
	@Override
	public void update(final String entityName, final Object entity) throws DataAccessException {
		update(entityName, entity, null);
	}

	/**
	 * Update the given persistent instance, associating it with the current Hibernate {@link org.hibernate.Session}.
	 * <p>
	 * Obtains the specified lock mode if the instance exists, implicitly checking whether the corresponding database entry still exists.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param entity the persistent instance to update
	 * @param lockMode the lock mode to obtain
	 * @throws org.springframework.orm.ObjectOptimisticLockingFailureException if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#update(String, Object)
	 */
	@Override
	public void update(final String entityName, final Object entity, final LockMode lockMode) throws DataAccessException {
		executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			session.update(entityName, entity);
			if (lockMode != null) {
				session.buildLockRequest(new LockOptions(lockMode)).lock(entityName, entity);
			}
			return null;
		});
	}

	/**
	 * Save or update the given persistent instance, according to its id (matching the configured "unsaved-value"?). Associates the instance with the current Hibernate
	 * {@link org.hibernate.Session}.
	 * 
	 * @param entity the persistent instance to save or update (to be associated with the Hibernate {@code Session})
	 * @throws DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#saveOrUpdate(Object)
	 */
	@Override
	public void saveOrUpdate(final Object entity) throws DataAccessException {
		executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			session.saveOrUpdate(entity);
			return null;
		});
	}

	/**
	 * Save or update the given persistent instance, according to its id (matching the configured "unsaved-value"?). Associates the instance with the current Hibernate
	 * {@code Session}.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param entity the persistent instance to save or update (to be associated with the Hibernate {@code Session})
	 * @throws DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#saveOrUpdate(String, Object)
	 */
	@Override
	public void saveOrUpdate(final String entityName, final Object entity) throws DataAccessException {
		executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			session.saveOrUpdate(entityName, entity);
			return null;
		});
	}

	/**
	 * Save or update all given persistent instances, according to its id (matching the configured "unsaved-value"?). Associates the instances with the current Hibernate
	 * {@code Session}.
	 * 
	 * @param entities the persistent instances to save or update (to be associated with the Hibernate {@code Session})
	 * @throws DataAccessException in case of Hibernate errors
	 * @deprecated as of Spring 2.5, in favor of individual {@code saveOrUpdate} or {@code merge} usage
	 */
	@Deprecated
	public void saveOrUpdateAll(@SuppressWarnings("rawtypes") final Collection entities) throws DataAccessException {
		executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			for (Object entity : entities) {
				session.saveOrUpdate(entity);
			}
			return null;
		});
	}

	/**
	 * Persist the state of the given detached instance according to the given replication mode, reusing the current identifier value.
	 * 
	 * @param entity the persistent object to replicate
	 * @param replicationMode the Hibernate ReplicationMode
	 * @throws DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#replicate(Object, org.hibernate.ReplicationMode)
	 */
	@Override
	public void replicate(final Object entity, final ReplicationMode replicationMode) throws DataAccessException {
		executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			session.replicate(entity, replicationMode);
			return null;
		});
	}

	/**
	 * Persist the state of the given detached instance according to the given replication mode, reusing the current identifier value.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param entity the persistent object to replicate
	 * @param replicationMode the Hibernate ReplicationMode
	 * @throws DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#replicate(String, Object, org.hibernate.ReplicationMode)
	 */
	@Override
	public void replicate(final String entityName, final Object entity, final ReplicationMode replicationMode) throws DataAccessException {
		executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			session.replicate(entityName, entity, replicationMode);
			return null;
		});
	}

	/**
	 * Persist the given transient instance. Follows JSR-220 semantics.
	 * <p>
	 * Similar to {@code save}, associating the given object with the current Hibernate {@link org.hibernate.Session}.
	 * 
	 * @param entity the persistent instance to persist
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#persist(Object)
	 * @see #save
	 */
	@Override
	public void persist(final Object entity) throws DataAccessException {
		executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			session.persist(entity);
			return null;
		});
	}

	/**
	 * Persist the given transient instance. Follows JSR-220 semantics.
	 * <p>
	 * Similar to {@code save}, associating the given object with the current Hibernate {@link org.hibernate.Session}.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param entity the persistent instance to persist
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#persist(String, Object)
	 * @see #save
	 */
	@Override
	public void persist(final String entityName, final Object entity) throws DataAccessException {
		executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			session.persist(entityName, entity);
			return null;
		});
	}

	/**
	 * Copy the state of the given object onto the persistent object with the same identifier. Follows JSR-220 semantics.
	 * <p>
	 * Similar to {@code saveOrUpdate}, but never associates the given object with the current Hibernate Session. In case of a new entity, the state will be copied over as well.
	 * <p>
	 * Note that {@code merge} will <i>not</i> update the identifiers in the passed-in object graph (in contrast to TopLink)! Consider registering Spring's
	 * {@code IdTransferringMergeEventListener} if you would like to have newly assigned ids transferred to the original object graph too.
	 * 
	 * @param entity the object to merge with the corresponding persistence instance
	 * @return the updated, registered persistent instance
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#merge(Object)
	 * @see #saveOrUpdate
	 * @see org.springframework.orm.hibernate3.support.IdTransferringMergeEventListener
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T merge(final T entity) throws DataAccessException {
		return executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			return (T) session.merge(entity);
		});
	}

	/**
	 * Copy the state of the given object onto the persistent object with the same identifier. Follows JSR-220 semantics.
	 * <p>
	 * Similar to {@code saveOrUpdate}, but never associates the given object with the current Hibernate {@link org.hibernate.Session}. In the case of a new entity, the state will
	 * be copied over as well.
	 * <p>
	 * Note that {@code merge} will <i>not</i> update the identifiers in the passed-in object graph (in contrast to TopLink)! Consider registering Spring's
	 * {@code IdTransferringMergeEventListener} if you would like to have newly assigned ids transferred to the original object graph too.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param entity the object to merge with the corresponding persistence instance
	 * @return the updated, registered persistent instance
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#merge(String, Object)
	 * @see #saveOrUpdate
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T merge(final String entityName, final T entity) throws DataAccessException {
		return executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			return (T) session.merge(entityName, entity);
		});
	}

	/**
	 * Delete the given persistent instance.
	 * 
	 * @param entity the persistent instance to delete
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#delete(Object)
	 */
	@Override
	public void delete(final Object entity) throws DataAccessException {
		delete(entity, null);
	}

	/**
	 * Delete the given persistent instance.
	 * <p>
	 * Obtains the specified lock mode if the instance exists, implicitly checking whether the corresponding database entry still exists.
	 * 
	 * @param entity the persistent instance to delete
	 * @param lockMode the lock mode to obtain
	 * @throws org.springframework.orm.ObjectOptimisticLockingFailureException if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#delete(Object)
	 */
	@Override
	public void delete(final Object entity, final LockMode lockMode) throws DataAccessException {
		executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			if (lockMode != null) {
				session.buildLockRequest(new LockOptions(lockMode)).lock(entity);
			}
			session.delete(entity);
			return null;
		});
	}

	/**
	 * Delete the given persistent instance.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param entity the persistent instance to delete
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#delete(Object)
	 */
	@Override
	public void delete(final String entityName, final Object entity) throws DataAccessException {
		delete(entityName, entity, null);
	}

	/**
	 * Delete the given persistent instance.
	 * <p>
	 * Obtains the specified lock mode if the instance exists, implicitly checking whether the corresponding database entry still exists.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param entity the persistent instance to delete
	 * @param lockMode the lock mode to obtain
	 * @throws org.springframework.orm.ObjectOptimisticLockingFailureException if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#delete(Object)
	 */
	@Override
	public void delete(final String entityName, final Object entity, final LockMode lockMode) throws DataAccessException {
		executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			if (lockMode != null) {
				session.buildLockRequest(new LockOptions(lockMode)).lock(entityName, entity);
			}
			session.delete(entityName, entity);
			return null;
		});
	}

	/**
	 * Delete all given persistent instances.
	 * <p>
	 * This can be combined with any of the find methods to delete by query in two lines of code.
	 * 
	 * @param entities the persistent instances to delete
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#delete(Object)
	 */
	@Override
	public void deleteAll(@SuppressWarnings("rawtypes") final Collection entities) throws DataAccessException {
		executeWithNativeSession(session -> {
			checkWriteOperationAllowed(session);
			for (Object entity : entities) {
				session.delete(entity);
			}
			return null;
		});
	}

	/**
	 * Flush all pending saves, updates and deletes to the database.
	 * <p>
	 * Only invoke this for selective eager flushing, for example when JDBC code needs to see certain changes within the same transaction. Else, it is preferable to rely on
	 * auto-flushing at transaction completion.
	 * 
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#flush
	 */
	@Override
	public void flush() throws DataAccessException {
		executeWithNativeSession(session -> {
			session.flush();
			return null;
		});
	}

	/**
	 * Remove all objects from the {@link org.hibernate.Session} cache, and cancel all pending saves, updates and deletes.
	 * 
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#clear
	 */
	@Override
	public void clear() throws DataAccessException {
		executeWithNativeSession(session -> {
			session.clear();
			return null;
		});
	}

	// -------------------------------------------------------------------------
	// Convenience finder methods for HQL strings
	// -------------------------------------------------------------------------

	/**
	 * Execute an HQL query.
	 * 
	 * @param queryString a query expressed in Hibernate's query language
	 * @return a {@link List} containing the results of the query execution
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#createQuery
	 */
	@Override
	public List<?> find(final String queryString) throws DataAccessException {
		return find(queryString, (Object[]) null);
	}

	/**
	 * Execute an HQL query, binding one value to a "?" parameter in the query string.
	 * 
	 * @param queryString a query expressed in Hibernate's query language
	 * @param value the value of the parameter
	 * @return a {@link List} containing the results of the query execution
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#createQuery
	 */
	@Override
	public List<?> find(final String queryString, final Object value) throws DataAccessException {
		return find(queryString, new Object[] { value });
	}

	/**
	 * Execute an HQL query, binding a number of values to "?" parameters in the query string.
	 * 
	 * @param queryString a query expressed in Hibernate's query language
	 * @param values the values of the parameters
	 * @return a {@link List} containing the results of the query execution
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#createQuery
	 */
	@Override
	public List<?> find(final String queryString, final Object... values) throws DataAccessException {
		return executeWithNativeSession(session -> {
			Query queryObject = session.createQuery(queryString);
			prepareQuery(queryObject);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					queryObject.setParameter(i, values[i]);
				}
			}
			return queryObject.list();
		});
	}

	/**
	 * Prepare the given Query object, applying cache settings and/or a transaction timeout.
	 * 
	 * @param queryObject the Query object to prepare
	 * @see #setCacheQueries
	 * @see #setQueryCacheRegion
	 * @see SessionFactoryUtils2#applyTransactionTimeout
	 */
	private void prepareQuery(Query queryObject) {
		if (isCacheQueries()) {
			queryObject.setCacheable(true);
			if (getQueryCacheRegion() != null) {
				queryObject.setCacheRegion(getQueryCacheRegion());
			}
		}
		if (getFetchSize() > 0) {
			queryObject.setFetchSize(getFetchSize());
		}
		if (getMaxResults() > 0) {
			queryObject.setMaxResults(getMaxResults());
		}
		SessionFactoryUtils2.applyTransactionTimeout(queryObject, getSessionFactory());
	}

	/**
	 * Execute an HQL query, binding one value to a ":" named parameter in the query string.
	 * 
	 * @param queryString a query expressed in Hibernate's query language
	 * @param paramName the name of the parameter
	 * @param value the value of the parameter
	 * @return a {@link List} containing the results of the query execution
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#getNamedQuery(String)
	 */
	@Override
	public List<?> findByNamedParam(final String queryString, final String paramName, final Object value) throws DataAccessException {
		return findByNamedParam(queryString, new String[] { paramName }, new Object[] { value });
	}

	/**
	 * Execute an HQL query, binding a number of values to ":" named parameters in the query string.
	 * 
	 * @param queryString a query expressed in Hibernate's query language
	 * @param paramNames the names of the parameters
	 * @param values the values of the parameters
	 * @return a {@link List} containing the results of the query execution
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#getNamedQuery(String)
	 */
	@Override
	public List<?> findByNamedParam(final String queryString, final String[] paramNames, final Object[] values) throws DataAccessException {
		if (paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		return executeWithNativeSession(session -> {
			Query queryObject = session.createQuery(queryString);
			prepareQuery(queryObject);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
				}
			}
			return queryObject.list();
		});
	}

	/**
	 * Apply the given name parameter to the given Query object.
	 * 
	 * @param queryObject the Query object
	 * @param paramName the name of the parameter
	 * @param value the value of the parameter
	 * @throws HibernateException if thrown by the Query object
	 */
	private void applyNamedParameterToQuery(Query queryObject, String paramName, Object value) throws HibernateException {
		if (value instanceof Collection) {
			queryObject.setParameterList(paramName, (Collection<?>) value);
		} else if (value instanceof Object[]) {
			queryObject.setParameterList(paramName, (Object[]) value);
		} else {
			queryObject.setParameter(paramName, value);
		}
	}

	/**
	 * Execute an HQL query, binding the properties of the given bean to <i>named</i> parameters in the query string.
	 * 
	 * @param queryString a query expressed in Hibernate's query language
	 * @param valueBean the values of the parameters
	 * @return a {@link List} containing the results of the query execution
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Query#setProperties
	 * @see org.hibernate.Session#createQuery
	 */
	@Override
	public List<?> findByValueBean(final String queryString, final Object valueBean) throws DataAccessException {
		return executeWithNativeSession(session -> {
			Query queryObject = session.createQuery(queryString);
			prepareQuery(queryObject);
			queryObject.setProperties(valueBean);
			return queryObject.list();
		});
	}

	// -------------------------------------------------------------------------
	// Convenience finder methods for named queries
	// -------------------------------------------------------------------------

	/**
	 * Execute a named query.
	 * <p>
	 * A named query is defined in a Hibernate mapping file.
	 * 
	 * @param queryName the name of a Hibernate query in a mapping file
	 * @return a {@link List} containing the results of the query execution
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#getNamedQuery(String)
	 */
	@Override
	public List<?> findByNamedQuery(final String queryName) throws DataAccessException {
		return findByNamedQuery(queryName, (Object[]) null);
	}

	/**
	 * Execute a named query, binding one value to a "?" parameter in the query string.
	 * <p>
	 * A named query is defined in a Hibernate mapping file.
	 * 
	 * @param queryName the name of a Hibernate query in a mapping file
	 * @param value the value of the parameter
	 * @return a {@link List} containing the results of the query execution
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#getNamedQuery(String)
	 */
	@Override
	public List<?> findByNamedQuery(final String queryName, final Object value) throws DataAccessException {
		return findByNamedQuery(queryName, new Object[] { value });
	}

	/**
	 * Execute a named query binding a number of values to "?" parameters in the query string.
	 * <p>
	 * A named query is defined in a Hibernate mapping file.
	 * 
	 * @param queryName the name of a Hibernate query in a mapping file
	 * @param values the values of the parameters
	 * @return a {@link List} containing the results of the query execution
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#getNamedQuery(String)
	 */
	@Override
	public List<?> findByNamedQuery(final String queryName, final Object... values) throws DataAccessException {
		return executeWithNativeSession(session -> {
			Query queryObject = session.getNamedQuery(queryName);
			prepareQuery(queryObject);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					queryObject.setParameter(i, values[i]);
				}
			}
			return queryObject.list();
		});
	}

	/**
	 * Execute a named query, binding one value to a ":" named parameter in the query string.
	 * <p>
	 * A named query is defined in a Hibernate mapping file.
	 * 
	 * @param queryName the name of a Hibernate query in a mapping file
	 * @param paramName the name of parameter
	 * @param value the value of the parameter
	 * @return a {@link List} containing the results of the query execution
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#getNamedQuery(String)
	 */
	@Override
	public List<?> findByNamedQueryAndNamedParam(final String queryName, final String paramName, final Object value) throws DataAccessException {
		return findByNamedQueryAndNamedParam(queryName, new String[] { paramName }, new Object[] { value });
	}

	/**
	 * Execute a named query, binding a number of values to ":" named parameters in the query string.
	 * <p>
	 * A named query is defined in a Hibernate mapping file.
	 * 
	 * @param queryName the name of a Hibernate query in a mapping file
	 * @param paramNames the names of the parameters
	 * @param values the values of the parameters
	 * @return a {@link List} containing the results of the query execution
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#getNamedQuery(String)
	 */
	@Override
	public List<?> findByNamedQueryAndNamedParam(final String queryName, final String[] paramNames, final Object[] values) throws DataAccessException {
		if (paramNames != null && values != null && paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		return executeWithNativeSession(session -> {
			Query queryObject = session.getNamedQuery(queryName);
			prepareQuery(queryObject);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
				}
			}
			return queryObject.list();
		});
	}

	/**
	 * Execute a named query, binding the properties of the given bean to ":" named parameters in the query string.
	 * <p>
	 * A named query is defined in a Hibernate mapping file.
	 * 
	 * @param queryName the name of a Hibernate query in a mapping file
	 * @param valueBean the values of the parameters
	 * @return a {@link List} containing the results of the query execution
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Query#setProperties
	 * @see org.hibernate.Session#getNamedQuery(String)
	 */
	@Override
	public List<?> findByNamedQueryAndValueBean(final String queryName, final Object valueBean) throws DataAccessException {
		return executeWithNativeSession(session -> {
			Query queryObject = session.getNamedQuery(queryName);
			prepareQuery(queryObject);
			queryObject.setProperties(valueBean);
			return queryObject.list();
		});
	}

	// -------------------------------------------------------------------------
	// Convenience finder methods for detached criteria
	// -------------------------------------------------------------------------

	/**
	 * Execute a query based on a given Hibernate criteria object.
	 * 
	 * @param criteria the detached Hibernate criteria object. <b>Note: Do not reuse criteria objects! They need to recreated per execution, due to the suboptimal design of
	 *            Hibernate's criteria facility.</b>
	 * @return a {@link List} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.criterion.DetachedCriteria#getExecutableCriteria(org.hibernate.Session)
	 */
	@Override
	public List<?> findByCriteria(final DetachedCriteria criteria) throws DataAccessException {
		return findByCriteria(criteria, -1, -1);
	}

	/**
	 * Execute a query based on the given Hibernate criteria object.
	 * 
	 * @param criteria the detached Hibernate criteria object. <b>Note: Do not reuse criteria objects! They need to recreated per execution, due to the suboptimal design of
	 *            Hibernate's criteria facility.</b>
	 * @param firstResult the index of the first result object to be retrieved (numbered from 0)
	 * @param maxResults the maximum number of result objects to retrieve (or <=0 for no limit)
	 * @return a {@link List} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.criterion.DetachedCriteria#getExecutableCriteria(org.hibernate.Session)
	 * @see org.hibernate.Criteria#setFirstResult(int)
	 * @see org.hibernate.Criteria#setMaxResults(int)
	 */
	@Override
	public List<?> findByCriteria(final DetachedCriteria criteria, final int firstResult, final int max) throws DataAccessException {
		Assert.notNull(criteria, "DetachedCriteria must not be null");
		return executeWithNativeSession(session -> {
			Criteria executableCriteria = criteria.getExecutableCriteria(session);
			prepareCriteria(executableCriteria);
			if (firstResult >= 0) {
				executableCriteria.setFirstResult(firstResult);
			}
			if (max > 0) {
				executableCriteria.setMaxResults(max);
			}
			return executableCriteria.list();
		});
	}

	/**
	 * Execute a query based on the given example entity object.
	 * 
	 * @param exampleEntity an instance of the desired entity, serving as example for "query-by-example"
	 * @return a {@link List} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.criterion.Example#create(Object)
	 */
	@Override
	public List<Object> findByExample(final Object exampleEntity) throws DataAccessException {
		return findByExample(null, exampleEntity, -1, -1);
	}

	/**
	 * Execute a query based on the given example entity object.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param exampleEntity an instance of the desired entity, serving as example for "query-by-example"
	 * @return a {@link List} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.criterion.Example#create(Object)
	 */
	@Override
	public List<Object> findByExample(final String entityName, final Object exampleEntity) throws DataAccessException {
		return findByExample(entityName, exampleEntity, -1, -1);
	}

	/**
	 * Execute a query based on a given example entity object.
	 * 
	 * @param exampleEntity an instance of the desired entity, serving as example for "query-by-example"
	 * @param firstResult the index of the first result object to be retrieved (numbered from 0)
	 * @param maxResults the maximum number of result objects to retrieve (or <=0 for no limit)
	 * @return a {@link List} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.criterion.Example#create(Object)
	 * @see org.hibernate.Criteria#setFirstResult(int)
	 * @see org.hibernate.Criteria#setMaxResults(int)
	 */
	@Override
	public List<Object> findByExample(final Object exampleEntity, final int firstResult, final int max) throws DataAccessException {
		return findByExample(null, exampleEntity, firstResult, max);
	}

	/**
	 * Execute a query based on a given example entity object.
	 * 
	 * @param entityName the name of the persistent entity
	 * @param exampleEntity an instance of the desired entity, serving as example for "query-by-example"
	 * @param firstResult the index of the first result object to be retrieved (numbered from 0)
	 * @param maxResults the maximum number of result objects to retrieve (or <=0 for no limit)
	 * @return a {@link List} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.criterion.Example#create(Object)
	 * @see org.hibernate.Criteria#setFirstResult(int)
	 * @see org.hibernate.Criteria#setMaxResults(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findByExample(final String entityName, final Object exampleEntity, final int firstResult, final int max) throws DataAccessException {
		Assert.notNull(exampleEntity, "Example entity must not be null");
		return executeWithNativeSession(session -> {
			Criteria executableCriteria = (entityName != null ? session.createCriteria(entityName) : session.createCriteria(exampleEntity.getClass()));
			executableCriteria.add(Example.create(exampleEntity));
			prepareCriteria(executableCriteria);
			if (firstResult >= 0) {
				executableCriteria.setFirstResult(firstResult);
			}
			if (max > 0) {
				executableCriteria.setMaxResults(max);
			}
			return executableCriteria.list();
		});
	}

	// -------------------------------------------------------------------------
	// Convenience query methods for iteration and bulk updates/deletes
	// -------------------------------------------------------------------------

	/**
	 * Execute a query for persistent instances.
	 * <p>
	 * Returns the results as an {@link Iterator}. Entities returned are initialized on demand. See the Hibernate API documentation for details.
	 * 
	 * @param queryString a query expressed in Hibernate's query language
	 * @return an {@link Iterator} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#createQuery
	 * @see org.hibernate.Query#iterate
	 */
	@Override
	public Iterator<?> iterate(final String queryString) throws DataAccessException {
		return iterate(queryString, (Object[]) null);
	}

	/**
	 * Execute a query for persistent instances, binding one value to a "?" parameter in the query string.
	 * <p>
	 * Returns the results as an {@link Iterator}. Entities returned are initialized on demand. See the Hibernate API documentation for details.
	 * 
	 * @param queryString a query expressed in Hibernate's query language
	 * @param value the value of the parameter
	 * @return an {@link Iterator} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#createQuery
	 * @see org.hibernate.Query#iterate
	 */
	@Override
	public Iterator<?> iterate(final String queryString, final Object value) throws DataAccessException {
		return iterate(queryString, new Object[] { value });
	}

	/**
	 * Execute a query for persistent instances, binding a number of values to "?" parameters in the query string.
	 * <p>
	 * Returns the results as an {@link Iterator}. Entities returned are initialized on demand. See the Hibernate API documentation for details.
	 * 
	 * @param queryString a query expressed in Hibernate's query language
	 * @param values the values of the parameters
	 * @return an {@link Iterator} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#createQuery
	 * @see org.hibernate.Query#iterate
	 */
	@Override
	public Iterator<?> iterate(final String queryString, final Object... values) throws DataAccessException {
		return executeWithNativeSession(session -> {
			Query queryObject = session.createQuery(queryString);
			prepareQuery(queryObject);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					queryObject.setParameter(i, values[i]);
				}
			}
			return queryObject.iterate();
		});
	}

	/**
	 * Immediately close an {@link Iterator} created by any of the various {@code iterate(..)} operations, instead of waiting until the session is closed or disconnected.
	 * 
	 * @param it the {@code Iterator} to close
	 * @throws DataAccessException if the {@code Iterator} could not be closed
	 * @see org.hibernate.Hibernate#close
	 */
	@Override
	public void closeIterator(@SuppressWarnings("rawtypes") final Iterator it) throws DataAccessException {
		try {
			Hibernate.close(it);
		} catch (HibernateException ex) {
			throw SessionFactoryUtils2.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Update/delete all objects according to the given query.
	 * 
	 * @param queryString an update/delete query expressed in Hibernate's query language
	 * @return the number of instances updated/deleted
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#createQuery
	 * @see org.hibernate.Query#executeUpdate
	 */
	@Override
	public int bulkUpdate(final String queryString) throws DataAccessException {
		return bulkUpdate(queryString, (Object[]) null);
	}

	/**
	 * Update/delete all objects according to the given query, binding one value to a "?" parameter in the query string.
	 * 
	 * @param queryString an update/delete query expressed in Hibernate's query language
	 * @param value the value of the parameter
	 * @return the number of instances updated/deleted
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#createQuery
	 * @see org.hibernate.Query#executeUpdate
	 */
	@Override
	public int bulkUpdate(final String queryString, final Object value) throws DataAccessException {
		return bulkUpdate(queryString, new Object[] { value });
	}

	/**
	 * Update/delete all objects according to the given query, binding a number of values to "?" parameters in the query string.
	 * 
	 * @param queryString an update/delete query expressed in Hibernate's query language
	 * @param values the values of the parameters
	 * @return the number of instances updated/deleted
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.Session#createQuery
	 * @see org.hibernate.Query#executeUpdate
	 */
	@Override
	public int bulkUpdate(final String queryString, final Object... values) throws DataAccessException {
		return executeWithNativeSession(session -> {
			Query queryObject = session.createQuery(queryString);
			prepareQuery(queryObject);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					queryObject.setParameter(i, values[i]);
				}
			}
			return queryObject.executeUpdate();
		});
	}
}

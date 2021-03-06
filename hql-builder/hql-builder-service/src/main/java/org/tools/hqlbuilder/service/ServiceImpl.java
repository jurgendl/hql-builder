package org.tools.hqlbuilder.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.LockOptions;
import org.hibernate.NaturalIdLoadAccess;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SimpleNaturalIdLoadAccess;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.tools.hqlbuilder.common.EntityERHAdapter;
import org.tools.hqlbuilder.common.exceptions.ServiceException;

@SuppressWarnings({ "unchecked", "hiding" })
public abstract class ServiceImpl {
    protected <P extends EntityERHAdapter> IdentifierLoadAccess<P> byId(Class<P> entityClass) {
        return this.getSession().byId(entityClass);
    }

    protected <P extends EntityERHAdapter> IdentifierLoadAccess<P> byId(String entityName) {
        return this.getSession().byId(entityName);
    }

    protected <P extends EntityERHAdapter> NaturalIdLoadAccess<P> byNaturalId(Class<P> entityClass) {
        return this.getSession().byNaturalId(entityClass);
    }

    protected <P extends EntityERHAdapter> NaturalIdLoadAccess<P> byNaturalId(String entityName) {
        return this.getSession().byNaturalId(entityName);
    }

    protected <P extends EntityERHAdapter> SimpleNaturalIdLoadAccess<P> bySimpleNaturalId(Class<P> entityClass) {
        return this.getSession().bySimpleNaturalId(entityClass);
    }

    protected <P extends EntityERHAdapter> SimpleNaturalIdLoadAccess<P> bySimpleNaturalId(String entityName) {
        return this.getSession().bySimpleNaturalId(entityName);
    }

	protected <P extends EntityERHAdapter> boolean contains(P entity) {
        return this.getSession().contains(entity);
    }

	protected <P extends EntityERHAdapter> Criteria createCriteria(Class<P> persistentClass) {
        return this.getSession().createCriteria(persistentClass);
    }

	protected <P extends EntityERHAdapter> Criteria createCriteria(Class<P> persistentClass, String alias) {
        return this.getSession().createCriteria(persistentClass, alias);
    }

	protected <P extends EntityERHAdapter> Criteria createCriteria(String entityName) {
        return this.getSession().createCriteria(entityName);
    }

	protected <P extends EntityERHAdapter> Criteria createCriteria(String entityName, String alias) {
        return this.getSession().createCriteria(entityName, alias);
    }

	protected <P extends EntityERHAdapter> org.hibernate.Query<P> createFilter(P collection, String queryString) {
        return this.getSession().createFilter(collection, queryString);
    }

    protected <P extends EntityERHAdapter> org.hibernate.query.Query<P> createQuery(Class<P> type) {
        return createQuery("from " + type.getSimpleName());
    }

    protected <P> org.hibernate.query.Query<P> createQuery(String queryString) {
        return this.getSession().createQuery(queryString);
    }

    protected <P extends EntityERHAdapter> org.hibernate.query.NativeQuery<P> createSQLQuery(String queryString) {
        return this.getSession().createNativeQuery(queryString);
    }

	protected <P extends EntityERHAdapter> P delete(P entity) {
        this.getSession().delete(entity);
        return entity;
    }

	protected <P extends EntityERHAdapter> void deleteAll(Class<P> entityClass) {
        this.getSession().createQuery("delete from " + entityClass.getName()).executeUpdate();
    }

	protected <P extends EntityERHAdapter> Long count(Class<P> entityClass) {
        return (Long) this.getSession().createQuery("select count(o) from " + entityClass.getName() + " o").list().get(0);
    }

	protected <P extends EntityERHAdapter> P delete(String entityName, P entity) {
        this.getSession().delete(entityName, entity);
        return entity;
    }

    protected <T> T doReturningWork(ReturningWork<T> work) throws HibernateException {
        return this.getSession().doReturningWork(work);
    }

    protected void doWork(Work work) throws HibernateException {
        this.getSession().doWork(work);
    }

	protected <P extends EntityERHAdapter> P evict(P entity) {
        this.getSession().evict(entity);
        return entity;
    }

    protected void flush() throws HibernateException {
        this.getSession().flush();
    }

	protected <P extends EntityERHAdapter> P get(Class<P> clazz, Serializable id) {
        return this.getSession().get(clazz, id);
    }

	protected <P extends EntityERHAdapter> P get(Class<P> clazz, Serializable id, LockOptions lockOptions) {
        return this.getSession().get(clazz, id, lockOptions);
    }

	protected <P extends EntityERHAdapter> P get(String entityName, Serializable id) {
        return (P) this.getSession().get(entityName, id);
    }

	protected <P extends EntityERHAdapter> P get(String entityName, Serializable id, LockOptions lockOptions) {
        return (P) this.getSession().get(entityName, id, lockOptions);
    }

	protected Serializable getIdentifier(Object entity) {
        return this.getSession().getIdentifier(entity);
    }

    protected <R> org.hibernate.query.Query<R> getNamedQuery(String queryName) {
        return this.getSession().getNamedQuery(queryName);
    }

    protected abstract Session getSession();

    protected SessionFactory getSessionFactory() {
        return this.getSession().getSessionFactory();
    }

	protected <P extends EntityERHAdapter> boolean isReadOnly(P entityOrProxy) {
        return this.getSession().isReadOnly(entityOrProxy);
    }

	protected <P extends EntityERHAdapter> P load(Class<P> theClass, Serializable id) {
        return this.getSession().load(theClass, id);
    }

	protected <P extends EntityERHAdapter> P load(Class<P> theClass, Serializable id, LockOptions lockOptions) {
        return this.getSession().load(theClass, id, lockOptions);
    }

	protected <P extends EntityERHAdapter> void load(P entity, Serializable id) {
        this.getSession().load(entity, id);
    }

	protected <P extends EntityERHAdapter> P load(String entityName, Serializable id) {
        return (P) this.getSession().load(entityName, id);
    }

	protected <P extends EntityERHAdapter> P load(String entityName, Serializable id, LockOptions lockOptions) {
        return (P) this.getSession().load(entityName, id, lockOptions);
    }

	protected <P extends EntityERHAdapter> P merge(P entity) {
        return (P) this.getSession().merge(entity);
    }

	protected <P extends EntityERHAdapter> P merge(String entityName, P entity) {
        return (P) this.getSession().merge(entityName, entity);
    }

	protected <P extends EntityERHAdapter> P persist(P entity) {
        try {
			this.getSession().persist(id(entity));
			flush();
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
        return entity;
    }

	protected <P extends EntityERHAdapter> P persist(String entityName, P entity) {
        try {
			this.getSession().persist(entityName, id(entity));
			flush();
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
        return entity;
    }

	protected <P extends EntityERHAdapter> P refresh(P entity) {
        this.getSession().refresh(entity);
        return entity;
    }

	protected <P extends EntityERHAdapter> P refresh(P entity, LockOptions lockOptions) {
        this.getSession().refresh(entity, lockOptions);
        return entity;
    }

	protected <P extends EntityERHAdapter> P refresh(String entityName, P entity) {
        this.getSession().refresh(entityName, entity);
        return entity;
    }

	protected <P extends EntityERHAdapter> P refresh(String entityName, P entity, LockOptions lockOptions) {
        this.getSession().refresh(entityName, entity, lockOptions);
        return entity;
    }

	protected <P extends EntityERHAdapter> P replicate(P entity, ReplicationMode replicationMode) {
        this.getSession().replicate(entity, replicationMode);
        return entity;
    }

	protected <P extends EntityERHAdapter> P replicate(String entityName, P entity, ReplicationMode replicationMode) {
        this.getSession().replicate(entityName, entity, replicationMode);
        return entity;
    }

	protected <P extends EntityERHAdapter> Serializable save(P entity) {
        try {
			Serializable e = this.getSession().save(id(entity));
			flush();
			return e;
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
    }

	protected <P extends EntityERHAdapter> Serializable save(String entityName, P entity) {
        try {
			Serializable e = this.getSession().save(entityName, id(entity));
			flush();
			return e;
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
    }

	protected <P extends EntityERHAdapter> P saveOrUpdate(P entity) {
        try {
			this.getSession().saveOrUpdate(id(entity));
			flush();
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
        return entity;
    }

	protected <P extends EntityERHAdapter> P saveOrUpdate(String entityName, P entity) {
        try {
			this.getSession().saveOrUpdate(entityName, id(entity));
			flush();
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
        return entity;
    }

	protected <P extends EntityERHAdapter> P update(P entity) {
        try {
			this.getSession().update(id(entity));
			flush();
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
        return entity;
    }

	protected <P extends EntityERHAdapter> P update(String entityName, P entity) {
        try {
			this.getSession().update(entityName, id(entity));
			flush();
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
        return entity;
    }

    private ConcurrentMap<String, AtomicLong> SEQ = new ConcurrentHashMap<>();

	protected <P extends EntityERHAdapter> P id(P entity) {
		if (entity.getId() == null) {
            String name = entity.getClass().getSimpleName();
            AtomicLong id = SEQ.get(name);
            if (id == null) {
                Object max = getSession().createNativeQuery("select max(id) from " + name).list().get(0);
                if (max == null) {
                    id = new AtomicLong(0l);
                } else {
                    id = new AtomicLong(BigInteger.class.cast(max).longValue());
                }
                SEQ.put(name, id);
            }
            entity.setId(id.incrementAndGet());
		}
		return entity;
	}

    public static <T extends Comparable<? super T>> List<T> sort(List<T> list) {
        Collections.sort(list);
        return list;
    }

	/*
	 * protected FullTextSession createSearchSession() { return
	 * Search.getFullTextSession(this.getSession()); }
	 */
}

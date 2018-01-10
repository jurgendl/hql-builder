package org.tools.hqlbuilder.service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.LockOptions;
import org.hibernate.NaturalIdLoadAccess;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SimpleNaturalIdLoadAccess;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.hibernate.query.Query;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.security.access.method.P;
import org.tools.hqlbuilder.common.exceptions.ServiceException;

@SuppressWarnings({ "unchecked", "hiding" })
public abstract class ServiceImpl {
    protected <P> IdentifierLoadAccess byId(Class<P> entityClass) {
        return this.getSession().byId(entityClass);
    }

    protected <P> IdentifierLoadAccess byId(String entityName) {
        return this.getSession().byId(entityName);
    }

    protected <P> NaturalIdLoadAccess byNaturalId(Class<P> entityClass) {
        return this.getSession().byNaturalId(entityClass);
    }

    protected <P> NaturalIdLoadAccess byNaturalId(String entityName) {
        return this.getSession().byNaturalId(entityName);
    }

    protected <P> SimpleNaturalIdLoadAccess bySimpleNaturalId(Class<P> entityClass) {
        return this.getSession().bySimpleNaturalId(entityClass);
    }

    protected <P> SimpleNaturalIdLoadAccess bySimpleNaturalId(String entityName) {
        return this.getSession().bySimpleNaturalId(entityName);
    }

    protected <P> boolean contains(P entity) {
        return this.getSession().contains(entity);
    }

    protected <P> Criteria createCriteria(Class<P> persistentClass) {
        return this.getSession().createCriteria(persistentClass);
    }

    protected <P> Criteria createCriteria(Class<P> persistentClass, String alias) {
        return this.getSession().createCriteria(persistentClass, alias);
    }

    protected <P> Criteria createCriteria(String entityName) {
        return this.getSession().createCriteria(entityName);
    }

    protected <P> Criteria createCriteria(String entityName, String alias) {
        return this.getSession().createCriteria(entityName, alias);
    }

    protected <P> Query createFilter(P collection, String queryString) {
        return this.getSession().createFilter(collection, queryString);
    }

    protected <P> Query createQuery(String queryString) {
        return this.getSession().createQuery(queryString);
    }

    protected <P> SQLQuery createSQLQuery(String queryString) {
        return this.getSession().createSQLQuery(queryString);
    }

    protected <P> P delete(P entity) {
        this.getSession().delete(entity);
        return entity;
    }

    protected <P> void deleteAll(Class<P> entityClass) {
        this.getSession().createQuery("delete from " + entityClass.getName()).executeUpdate();
    }

    protected <P> Long count(Class<P> entityClass) {
        return (Long) this.getSession().createQuery("select count(o) from " + entityClass.getName() + " o").list().get(0);
    }

    protected <P> P delete(String entityName, P entity) {
        this.getSession().delete(entityName, entity);
        return entity;
    }

    protected <T> T doReturningWork(ReturningWork<T> work) throws HibernateException {
        return this.getSession().doReturningWork(work);
    }

    protected void doWork(Work work) throws HibernateException {
        this.getSession().doWork(work);
    }

    protected <P> P evict(P entity) {
        this.getSession().evict(entity);
        return entity;
    }

    protected void flush() throws HibernateException {
        this.getSession().flush();
    }

    protected <P> P get(Class<P> clazz, Serializable id) {
        return (P) this.getSession().get(clazz, id);
    }

    protected <P> P get(Class<P> clazz, Serializable id, LockOptions lockOptions) {
        return (P) this.getSession().get(clazz, id, lockOptions);
    }

    protected <P> P get(String entityName, Serializable id) {
        return (P) this.getSession().get(entityName, id);
    }

    protected <P> P get(String entityName, Serializable id, LockOptions lockOptions) {
        return (P) this.getSession().get(entityName, id, lockOptions);
    }

    protected Serializable getIdentifier(P entity) {
        return this.getSession().getIdentifier(entity);
    }

    protected Query getNamedQuery(String queryName) {
        return this.getSession().getNamedQuery(queryName);
    }

    protected abstract Session getSession();

    protected SessionFactory getSessionFactory() {
        return this.getSession().getSessionFactory();
    }

    protected <P> boolean isReadOnly(P entityOrProxy) {
        return this.getSession().isReadOnly(entityOrProxy);
    }

    protected <P> P load(Class<P> theClass, Serializable id) {
        return (P) this.getSession().load(theClass, id);
    }

    protected <P> P load(Class<P> theClass, Serializable id, LockOptions lockOptions) {
        return (P) this.getSession().load(theClass, id, lockOptions);
    }

    protected <P> void load(P entity, Serializable id) {
        this.getSession().load(entity, id);
    }

    protected <P> P load(String entityName, Serializable id) {
        return (P) this.getSession().load(entityName, id);
    }

    protected <P> P load(String entityName, Serializable id, LockOptions lockOptions) {
        return (P) this.getSession().load(entityName, id, lockOptions);
    }

    protected <P> P merge(P entity) {
        return (P) this.getSession().merge(entity);
    }

    protected <P> P merge(String entityName, P entity) {
        return (P) this.getSession().merge(entityName, entity);
    }

    protected <P> P persist(P entity) {
        try {
            this.getSession().persist(entity);
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
        return entity;
    }

    protected <P> P persist(String entityName, P entity) {
        try {
            this.getSession().persist(entityName, entity);
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
        return entity;
    }

    protected <P> P refresh(P entity) {
        this.getSession().refresh(entity);
        return entity;
    }

    protected <P> P refresh(P entity, LockOptions lockOptions) {
        this.getSession().refresh(entity, lockOptions);
        return entity;
    }

    protected <P> P refresh(String entityName, P entity) {
        this.getSession().refresh(entityName, entity);
        return entity;
    }

    protected <P> P refresh(String entityName, P entity, LockOptions lockOptions) {
        this.getSession().refresh(entityName, entity, lockOptions);
        return entity;
    }

    protected <P> P replicate(P entity, ReplicationMode replicationMode) {
        this.getSession().replicate(entity, replicationMode);
        return entity;
    }

    protected <P> P replicate(String entityName, P entity, ReplicationMode replicationMode) {
        this.getSession().replicate(entityName, entity, replicationMode);
        return entity;
    }

    protected <P> Serializable save(P entity) {
        try {
            return this.getSession().save(entity);
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
    }

    protected <P> Serializable save(String entityName, P entity) {
        try {
            return this.getSession().save(entityName, entity);
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
    }

    protected <P> P saveOrUpdate(P entity) {
        try {
            this.getSession().saveOrUpdate(entity);
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
        return entity;
    }

    protected <P> P saveOrUpdate(String entityName, P entity) {
        try {
            this.getSession().saveOrUpdate(entityName, entity);
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
        return entity;
    }

    protected <P> P update(P entity) {
        try {
            this.getSession().update(entity);
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
        return entity;
    }

    protected <P> P update(String entityName, P entity) {
        try {
            this.getSession().update(entityName, entity);
        } catch (javax.validation.ConstraintViolationException ex) {
            throw new ServiceException(ex.getConstraintViolations());
        }
        return entity;
    }

    public static <T extends Comparable<? super T>> List<T> sort(List<T> list) {
        Collections.sort(list);
        return list;
    }

    protected FullTextSession createSearchSession() {
        return Search.getFullTextSession(this.getSession());
    }
}

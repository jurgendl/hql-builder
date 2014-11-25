package org.tools.hqlbuilder.service;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.LockOptions;
import org.hibernate.NaturalIdLoadAccess;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SimpleNaturalIdLoadAccess;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

public abstract class ServiceImpl {
    protected IdentifierLoadAccess byId(Class<?> entityClass) {
        return this.getSession().byId(entityClass);
    }

    protected IdentifierLoadAccess byId(String entityName) {
        return this.getSession().byId(entityName);
    }

    protected NaturalIdLoadAccess byNaturalId(Class<?> entityClass) {
        return this.getSession().byNaturalId(entityClass);
    }

    protected NaturalIdLoadAccess byNaturalId(String entityName) {
        return this.getSession().byNaturalId(entityName);
    }

    protected SimpleNaturalIdLoadAccess bySimpleNaturalId(Class<?> entityClass) {
        return this.getSession().bySimpleNaturalId(entityClass);
    }

    protected SimpleNaturalIdLoadAccess bySimpleNaturalId(String entityName) {
        return this.getSession().bySimpleNaturalId(entityName);
    }

    protected boolean contains(Object object) {
        return this.getSession().contains(object);
    }

    protected Criteria createCriteria(Class<?> persistentClass) {
        return this.getSession().createCriteria(persistentClass);
    }

    protected Criteria createCriteria(Class<?> persistentClass, String alias) {
        return this.getSession().createCriteria(persistentClass, alias);
    }

    protected Criteria createCriteria(String entityName) {
        return this.getSession().createCriteria(entityName);
    }

    protected Criteria createCriteria(String entityName, String alias) {
        return this.getSession().createCriteria(entityName, alias);
    }

    protected Query createFilter(Object collection, String queryString) {
        return this.getSession().createFilter(collection, queryString);
    }

    protected Query createQuery(String queryString) {
        return this.getSession().createQuery(queryString);
    }

    protected SQLQuery createSQLQuery(String queryString) {
        return this.getSession().createSQLQuery(queryString);
    }

    protected void delete(Object object) {
        this.getSession().delete(object);
    }

    protected void delete(String entityName, Object object) {
        this.getSession().delete(entityName, object);
    }

    protected <T> T doReturningWork(ReturningWork<T> work) throws HibernateException {
        return this.getSession().doReturningWork(work);
    }

    protected void doWork(Work work) throws HibernateException {
        this.getSession().doWork(work);
    }

    protected void evict(Object object) {
        this.getSession().evict(object);
    }

    protected void flush() throws HibernateException {
        this.getSession().flush();
    }

    protected Object get(Class<?> clazz, Serializable id) {
        return this.getSession().get(clazz, id);
    }

    protected Object get(Class<?> clazz, Serializable id, LockOptions lockOptions) {
        return this.getSession().get(clazz, id, lockOptions);
    }

    protected Object get(String entityName, Serializable id) {
        return this.getSession().get(entityName, id);
    }

    protected Object get(String entityName, Serializable id, LockOptions lockOptions) {
        return this.getSession().get(entityName, id, lockOptions);
    }

    protected Serializable getIdentifier(Object object) {
        return this.getSession().getIdentifier(object);
    }

    protected Query getNamedQuery(String queryName) {
        return this.getSession().getNamedQuery(queryName);
    }

    protected abstract Session getSession();

    protected SessionFactory getSessionFactory() {
        return this.getSession().getSessionFactory();
    }

    protected boolean isReadOnly(Object entityOrProxy) {
        return this.getSession().isReadOnly(entityOrProxy);
    }

    protected Object load(Class<?> theClass, Serializable id) {
        return this.getSession().load(theClass, id);
    }

    protected Object load(Class<?> theClass, Serializable id, LockOptions lockOptions) {
        return this.getSession().load(theClass, id, lockOptions);
    }

    protected void load(Object object, Serializable id) {
        this.getSession().load(object, id);
    }

    protected Object load(String entityName, Serializable id) {
        return this.getSession().load(entityName, id);
    }

    protected Object load(String entityName, Serializable id, LockOptions lockOptions) {
        return this.getSession().load(entityName, id, lockOptions);
    }

    protected Object merge(Object object) {
        return this.getSession().merge(object);
    }

    protected Object merge(String entityName, Object object) {
        return this.getSession().merge(entityName, object);
    }

    protected void persist(Object object) {
        this.getSession().persist(object);
    }

    protected void persist(String entityName, Object object) {
        this.getSession().persist(entityName, object);
    }

    protected void refresh(Object object) {
        this.getSession().refresh(object);
    }

    protected void refresh(Object object, LockOptions lockOptions) {
        this.getSession().refresh(object, lockOptions);
    }

    protected void refresh(String entityName, Object object) {
        this.getSession().refresh(entityName, object);
    }

    protected void refresh(String entityName, Object object, LockOptions lockOptions) {
        this.getSession().refresh(entityName, object, lockOptions);
    }

    protected void replicate(Object object, ReplicationMode replicationMode) {
        this.getSession().replicate(object, replicationMode);
    }

    protected void replicate(String entityName, Object object, ReplicationMode replicationMode) {
        this.getSession().replicate(entityName, object, replicationMode);
    }

    protected Serializable save(Object object) {
        return this.getSession().save(object);
    }

    protected Serializable save(String entityName, Object object) {
        return this.getSession().save(entityName, object);
    }

    protected void saveOrUpdate(Object object) {
        this.getSession().saveOrUpdate(object);
    }

    protected void saveOrUpdate(String entityName, Object object) {
        this.getSession().saveOrUpdate(entityName, object);
    }

    protected void update(Object object) {
        this.getSession().update(object);
    }

    protected void update(String entityName, Object object) {
        this.getSession().update(entityName, object);
    }
}

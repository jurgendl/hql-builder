package org.tools.hqlbuilder.common;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.tools.hqlbuilder.common.exceptions.ValidationException;

public abstract class DelegatingHqlService implements HqlService {
    private static final long serialVersionUID = 768218798588915375L;

    public abstract HqlService getDelegate();

    @Override
    public SortedSet<String> getClasses() {
        return getDelegate().getClasses();
    }

    @Override
    public String getSqlForHql(String hql) {
        return getDelegate().getSqlForHql(hql);
    }

    @Override
    public ExecutionResult execute(String string, QueryParameter... queryParameters) {
        return getDelegate().execute(string, queryParameters);
    }

    @Override
    public ExecutionResult execute(String string, List<QueryParameter> queryParameters) {
        return getDelegate().execute(string, queryParameters);
    }

    @Override
    public ExecutionResult execute(String string, int max, QueryParameter... queryParameters) {
        return getDelegate().execute(string, max, queryParameters);
    }

    @Override
    public ExecutionResult execute(String string, int max, List<QueryParameter> queryParameters) {
        return getDelegate().execute(string, max, queryParameters);
    }

    @Override
    public HibernateWebResolver getHibernateWebResolver() {
        return getDelegate().getHibernateWebResolver();
    }

    @Override
    public List<String> getProperties(String classname) {
        return getDelegate().getProperties(classname);
    }

    @Override
    public String getConnectionInfo() {
        return getDelegate().getConnectionInfo();
    }

    @Override
    public String getProject() {
        return getDelegate().getProject();
    }

    @Override
    public <T> T save(T object) throws ValidationException {
        return getDelegate().save(object);
    }

    @Override
    public <T> void delete(T object) {
        getDelegate().delete(object);
    }

    @Override
    public List<String> search(String text, String typeName) {
        return getDelegate().search(text, typeName);
    }

    @Override
    public Set<String> getReservedKeywords() {
        return getDelegate().getReservedKeywords();
    }

    @Override
    public Map<String, String> getNamedQueries() {
        return getDelegate().getNamedQueries();
    }

    @Override
    public List<QueryParameter> findParameters(String hql) {
        return getDelegate().findParameters(hql);
    }

    @Override
    public List<String> getPropertyNames(Object key, String[] parts) {
        return getDelegate().getPropertyNames(key, parts);
    }

    @Override
    public void log() {
        getDelegate().log();
    }

    @Override
    public void createScript() {
        getDelegate().createScript();
    }
}

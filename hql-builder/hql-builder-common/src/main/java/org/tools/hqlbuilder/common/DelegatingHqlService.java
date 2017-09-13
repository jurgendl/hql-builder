package org.tools.hqlbuilder.common;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.tools.hqlbuilder.common.exceptions.ValidationException;

public abstract class DelegatingHqlService implements HqlService {
    public abstract HqlService getDelegate();

    @Override
    public String getVersion() {
        return getDelegate().getVersion();
    }

    @Override
    public SortedSet<String> getClasses() {
        return getDelegate().getClasses();
    }

    @Override
    public boolean stopQuery(String uuid) {
        return getDelegate().stopQuery(uuid);
    }

    @Override
    public String getSqlForHql(String hql) {
        return getDelegate().getSqlForHql(hql);
    }

    @Override
    public ExecutionResult execute(QueryParameters queryParameters) {
        return getDelegate().execute(queryParameters);
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
    public <T extends Serializable, I extends Serializable> I save(T object) throws ValidationException {
        return getDelegate().save(object);
    }

    @Override
    public <T extends Serializable> void delete(T object) {
        getDelegate().delete(object);
    }

    @Override
    public List<String> search(String text, String typeName, int hitsPerPage) throws UnsupportedOperationException, IOException {
        return getDelegate().search(text, typeName, hitsPerPage);
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
    public List<String> getPropertyNames(String key, String[] parts) {
        return getDelegate().getPropertyNames(key, parts);
    }

    @Override
    public void log() {
        getDelegate().log();
    }

    @Override
    public String createScript() {
        return getDelegate().createScript();
    }

    @Override
    public void sql(String[] sql) {
        getDelegate().sql(sql);
    }

    @Override
    public Map<String, String> getHibernateInfo() {
        return getDelegate().getHibernateInfo();
    }

    @Override
    public String getHibernateHelpURL() {
        return getDelegate().getHibernateHelpURL();
    }

    @Override
    public String getHqlHelpURL() {
        return getDelegate().getHqlHelpURL();
    }

    @Override
    public String getLuceneHelpURL() {
        return getDelegate().getLuceneHelpURL();
    }

    @Override
    public <T extends Serializable, I extends Serializable> T get(Class<T> type, I id) {
        return getDelegate().get(type, id);
    }
}

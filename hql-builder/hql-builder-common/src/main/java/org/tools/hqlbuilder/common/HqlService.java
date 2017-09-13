package org.tools.hqlbuilder.common;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.tools.hqlbuilder.common.exceptions.ValidationException;

public interface HqlService {
    public abstract SortedSet<String> getClasses();

    public abstract String getSqlForHql(String hql);

    public abstract ExecutionResult execute(QueryParameters queryParameters);

    public abstract boolean stopQuery(String uuid);

    public abstract HibernateWebResolver getHibernateWebResolver();

    public abstract List<String> getProperties(String classname);

    public abstract String getConnectionInfo();

    public abstract String getProject();

    public abstract <T extends Serializable, I extends Serializable> I save(T object) throws ValidationException;

    public abstract <T extends Serializable> void delete(T object);

    public abstract List<String> search(String text, String typeName, int hitsPerPage) throws UnsupportedOperationException, IOException;

    public abstract Set<String> getReservedKeywords();

    public abstract Map<String, String> getNamedQueries();

    public abstract List<QueryParameter> findParameters(String hql);

    public abstract List<String> getPropertyNames(String key, String[] parts);

    public abstract void log();

    public abstract String createScript();

    public abstract void sql(String[] sql);

    public abstract Map<String, String> getHibernateInfo();

    public abstract String getHibernateHelpURL();

    public abstract String getHqlHelpURL();

    public abstract String getLuceneHelpURL();

    public abstract <T extends Serializable, I extends Serializable> T get(Class<T> type, I id);

    public abstract String getVersion();
}

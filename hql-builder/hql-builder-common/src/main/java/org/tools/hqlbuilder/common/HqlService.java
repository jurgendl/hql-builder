package org.tools.hqlbuilder.common;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

public interface HqlService {
    public abstract SortedSet<String> getClasses();

    public abstract String getSqlForHql(String hql);

    public abstract ExecutionResult execute(String string, int max, QueryParameter... queryParameters);

    public abstract ExecutionResult execute(String string, int max, List<QueryParameter> queryParameters);

    public abstract HibernateWebResolver getHibernateWebResolver();

    public abstract List<String> getProperties(String classname);

    public abstract String getConnectionInfo();

    public abstract <T> T save(T object);

    public abstract <T> void delete(T object);

    /**
     * 
     * JDOC
     * 
     * @param text
     * @param typeName "class" or "field"
     * @return
     */
    public abstract List<String> search(String text, String typeName);

    public abstract Set<String> getReservedKeywords();

    public abstract Map<String, String> getNamedQueries();

    public abstract List<QueryParameter> findParameters(String hql);

    public abstract List<String> getPropertyNames(Object key, String[] parts);
}

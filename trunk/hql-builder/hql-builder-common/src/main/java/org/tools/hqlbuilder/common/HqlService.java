package org.tools.hqlbuilder.common;

import java.util.List;

public interface HqlService {
    public abstract List<String> getClasses();

    public abstract String getSqlForHql(String hql);

    public abstract ExecutionResult execute(String string, List<QueryParameter> queryParameters);

    public abstract HibernateWebResolver getHibernateWebResolver();

    public abstract String getConnectionInfo();
}

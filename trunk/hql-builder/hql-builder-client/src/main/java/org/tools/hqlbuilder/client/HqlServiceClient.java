package org.tools.hqlbuilder.client;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.HibernateWebResolver;
import org.tools.hqlbuilder.common.HqlService;
import org.tools.hqlbuilder.common.QueryParameter;

public class HqlServiceClient implements HqlService {
    @Autowired
    private HqlService hqlService;

    public HqlService getHqlService() {
        return hqlService;
    }

    public void setHqlService(HqlService hqlService) {
        this.hqlService = hqlService;
    }

    @Override
    public SortedSet<String> getClasses() {
        return this.hqlService.getClasses();
    }

    @Override
    public String getSqlForHql(String hql) {
        return this.hqlService.getSqlForHql(hql);
    }

    @Override
    public ExecutionResult execute(String string, int max, QueryParameter... queryParameters) {
        return hqlService.execute(string, max, queryParameters);
    }

    @Override
    public ExecutionResult execute(String string, int max, List<QueryParameter> queryParameters) {
        return hqlService.execute(string, max, queryParameters);
    }

    @Override
    public HibernateWebResolver getHibernateWebResolver() {
        return this.hqlService.getHibernateWebResolver();
    }

    @Override
    public String getConnectionInfo() {
        return this.hqlService.getConnectionInfo();
    }

    @Override
    public List<String> search(String text, String typeName) {
        return this.hqlService.search(text, typeName);
    }

    @Override
    public List<String> getProperties(String classname) {
        return this.hqlService.getProperties(classname);
    }

    @Override
    public <T> T save(T object) {
        return this.hqlService.save(object);
    }

    @Override
    public <T> void delete(T object) {
        this.hqlService.delete(object);
    }

    @Override
    public Set<String> getReservedKeywords() {
        return hqlService.getReservedKeywords();
    }

    @Override
    public Map<String, String> getNamedQueries() {
        return hqlService.getNamedQueries();
    }

    @Override
    public List<QueryParameter> findParameters(String hql) {
        return hqlService.findParameters(hql);
    }

    @Override
    public List<String> getPropertyNames(Object key, String[] parts) {
        return hqlService.getPropertyNames(key, parts);
    }
}

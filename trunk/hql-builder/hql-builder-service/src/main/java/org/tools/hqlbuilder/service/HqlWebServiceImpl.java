package org.tools.hqlbuilder.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.HibernateWebResolver;
import org.tools.hqlbuilder.common.HqlService;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.exceptions.ValidationException;

public class HqlWebServiceImpl implements HqlWebService {
    private static final long serialVersionUID = -7297624992346972087L;

    private HqlService hqlService;

    public HqlWebServiceImpl() {
        super();
    }

    public HqlService getHqlService() {
        return this.hqlService;
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
    public ExecutionResult execute(String string, QueryParameter... queryParameters) {
        return this.hqlService.execute(string, queryParameters);
    }

    @Override
    public ExecutionResult execute(String string, List<QueryParameter> queryParameters) {
        return this.hqlService.execute(string, queryParameters);
    }

    @Override
    public ExecutionResult execute(String string, int max, QueryParameter... queryParameters) {
        return this.hqlService.execute(string, max, queryParameters);
    }

    @Override
    public ExecutionResult execute(String string, int max, List<QueryParameter> queryParameters) {
        return this.hqlService.execute(string, max, queryParameters);
    }

    @Override
    public HibernateWebResolver getHibernateWebResolver() {
        return this.hqlService.getHibernateWebResolver();
    }

    @Override
    public List<String> getProperties(String classname) {
        return this.hqlService.getProperties(classname);
    }

    @Override
    public String getConnectionInfo() {
        return this.hqlService.getConnectionInfo();
    }

    @Override
    public <T> T save(T object) throws ValidationException {
        return this.hqlService.save(object);
    }

    @Override
    public <T> void delete(T object) {
        this.hqlService.delete(object);
    }

    @Override
    public List<String> search(String text, String typeName) {
        return this.hqlService.search(text, typeName);
    }

    @Override
    public Set<String> getReservedKeywords() {
        return this.hqlService.getReservedKeywords();
    }

    @Override
    public Map<String, String> getNamedQueries() {
        return this.hqlService.getNamedQueries();
    }

    @Override
    public List<QueryParameter> findParameters(String hql) {
        return this.hqlService.findParameters(hql);
    }

    @Override
    public List<String> getPropertyNames(Object key, String[] parts) {
        return this.hqlService.getPropertyNames(key, parts);
    }

    @Override
    public String getProject() {
        return this.hqlService.getProject();
    }

    @Override
    public void log() {
        this.hqlService.log();
    }
}
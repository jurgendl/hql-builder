package org.tools.hqlbuilder.webservice.resteasy;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.webservice.HqlWebService;
import org.tools.hqlbuilder.webservice.jaxb.XmlWrapper;

/**
 * @see http://docs.jboss.org/resteasy/docs/2.3.7.Final/userguide/html/
 * @see http://blog.comsysto.com/2012/08/02/resteasy-integration-with-spring-tutorial-part-1-introduction/
 * @see https://access.redhat.com/site/documentation/en-US/JBoss_Enterprise_Web_Platform/5/html-single/RESTEasy_Reference_Guide/index.html
 */
@Component
public class PojoResourceImpl implements PojoResource {
    @Resource
    private HqlWebService hqlWebService;

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#ping()
     */
    @Override
    public String ping() {
        try {
            return "hello from Rest-Easy, service " + hqlWebService.getProject() + " on " + hqlWebService.getConnectionInfo() + " is available";
        } catch (Exception ex) {
            return "hello from Rest-Easy, service is not available: " + ex;
        }
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#getClasses()
     */
    @Override
    public XmlWrapper<SortedSet<String>> getClasses() {
        return new XmlWrapper<SortedSet<String>>(hqlWebService.getClasses());
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#getSqlForHql(java.lang.String)
     */
    @Override
    public String getSqlForHql(String hql) {
        return hqlWebService.getSqlForHql(hql);
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#getProperties(java.lang.String)
     */
    @Override
    public XmlWrapper<List<String>> getProperties(String classname) {
        return new XmlWrapper<List<String>>(hqlWebService.getProperties(classname));
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#getConnectionInfo()
     */
    @Override
    public String getConnectionInfo() {
        return hqlWebService.getConnectionInfo();
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#getProject()
     */
    @Override
    public String getProject() {
        return hqlWebService.getProject();
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#search(java.lang.String, java.lang.String, int)
     */
    @Override
    public XmlWrapper<List<String>> search(String text, String typeName, int hitsPerPage) {
        try {
            return new XmlWrapper<List<String>>(hqlWebService.search(text, typeName, hitsPerPage));
        } catch (UnsupportedOperationException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#getReservedKeywords()
     */
    @Override
    public XmlWrapper<Set<String>> getReservedKeywords() {
        return new XmlWrapper<Set<String>>(hqlWebService.getReservedKeywords());
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#getNamedQueries()
     */
    @Override
    public XmlWrapper<Map<String, String>> getNamedQueries() {
        return new XmlWrapper<Map<String, String>>(hqlWebService.getNamedQueries());
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#createScript()
     */
    @Override
    public String createScript() {
        return hqlWebService.createScript();
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#getHibernateInfo()
     */
    @Override
    public XmlWrapper<Map<String, String>> getHibernateInfo() {
        return new XmlWrapper<Map<String, String>>(hqlWebService.getHibernateInfo());
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#getHibernateHelpURL()
     */
    @Override
    public String getHibernateHelpURL() {
        return hqlWebService.getHibernateHelpURL();
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#getHqlHelpURL()
     */
    @Override
    public String getHqlHelpURL() {
        return hqlWebService.getHqlHelpURL();
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#getLuceneHelpURL()
     */
    @Override
    public String getLuceneHelpURL() {
        return hqlWebService.getLuceneHelpURL();
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#getPropertyNames(java.lang.String, java.lang.String[])
     */
    @Override
    public XmlWrapper<List<String>> getPropertyNames(String key, String[] parts) {
        return new XmlWrapper<List<String>>(hqlWebService.getPropertyNames(key, parts));
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#sql(java.lang.String[])
     */
    @Override
    public void sql(String[] sql) {
        hqlWebService.sql(sql);
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#findParameters(java.lang.String)
     */
    @Override
    public List<QueryParameter> findParameters(String hql) {
        return hqlWebService.findParameters(hql);
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#save(java.lang.Object)
     */
    @Override
    public <T> T save(T object) {
        return hqlWebService.save(object);
    }

    /**
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#delete(java.lang.Object)
     */
    @Override
    public <T> void delete(T object) {
        hqlWebService.delete(object);
    }

    /**
     * 
     * @see org.tools.hqlbuilder.webservice.resteasy.PojoResource#execute(java.lang.String, int, java.util.List)
     */
    @Override
    public ExecutionResult execute(String hql, int max, List<QueryParameter> queryParameters) {
        return hqlWebService.execute(hql, max, queryParameters);
    }
}

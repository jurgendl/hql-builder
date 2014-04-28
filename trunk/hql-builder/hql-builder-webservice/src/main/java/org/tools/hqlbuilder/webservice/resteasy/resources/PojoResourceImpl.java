package org.tools.hqlbuilder.webservice.resteasy.resources;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.annotation.Resource;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.springframework.stereotype.Component;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.common.XmlWrapper;
import org.tools.hqlbuilder.webcommon.resteasy.PojoResource;
import org.tools.hqlbuilder.webservice.HqlWebService;

/**
 * @see http://docs.jboss.org/resteasy/docs/2.3.7.Final/userguide/html/
 * @see http://blog.comsysto.com/2012/08/02/resteasy-integration-with-spring-tutorial-part-1-introduction/
 * @see https://access.redhat.com/site/documentation/en-US/JBoss_Enterprise_Web_Platform/5/html-single/RESTEasy_Reference_Guide/index.html
 * @see http://howtodoinjava.com/2013/07/25/jax-rs-2-0-resteasy-3-0-2-final-security-tutorial/
 */
@Component
public class PojoResourceImpl implements PojoResource {
    @Resource
    protected HqlWebService hqlWebService;

    @Override
    public String ping() {
        try {
            return "hello from Rest-Easy, service " + hqlWebService.getProject() + " on " + hqlWebService.getConnectionInfo() + " is ready to use";
        } catch (Exception ex) {
            return "hello from Rest-Easy, service is not available: " + ex;
        }
    }

    @Override
    public XmlWrapper<SortedSet<String>> getClasses() {
        return new XmlWrapper<SortedSet<String>>(hqlWebService.getClasses());
    }

    @Override
    public String getSqlForHql(String hql) {
        return hqlWebService.getSqlForHql(hql);
    }

    @Override
    public XmlWrapper<List<String>> getProperties(String classname) {
        return new XmlWrapper<List<String>>(hqlWebService.getProperties(classname));
    }

    @Override
    public String getConnectionInfo() {
        return hqlWebService.getConnectionInfo();
    }

    @Override
    public String getProject() {
        return hqlWebService.getProject();
    }

    @Override
    public XmlWrapper<List<String>> search(String text, String typeName, int hitsPerPage) {
        try {
            return new XmlWrapper<List<String>>(hqlWebService.search(text, typeName, hitsPerPage));
        } catch (UnsupportedOperationException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public XmlWrapper<Set<String>> getReservedKeywords() {
        return new XmlWrapper<Set<String>>(hqlWebService.getReservedKeywords());
    }

    @Override
    public XmlWrapper<Map<String, String>> getNamedQueries() {
        return new XmlWrapper<Map<String, String>>(hqlWebService.getNamedQueries());
    }

    @Override
    public String createScript() {
        return hqlWebService.createScript();
    }

    @Override
    public XmlWrapper<Map<String, String>> getHibernateInfo() {
        return new XmlWrapper<Map<String, String>>(hqlWebService.getHibernateInfo());
    }

    @Override
    public String getHibernateHelpURL() {
        return hqlWebService.getHibernateHelpURL();
    }

    @Override
    public String getHqlHelpURL() {
        return hqlWebService.getHqlHelpURL();
    }

    @Override
    public String getLuceneHelpURL() {
        return hqlWebService.getLuceneHelpURL();
    }

    @Override
    public XmlWrapper<List<String>> getPropertyNames(String key, String[] parts) {
        return new XmlWrapper<List<String>>(hqlWebService.getPropertyNames(key, parts));
    }

    @Override
    public void sql(String[] sql) {
        hqlWebService.sql(sql);
    }

    @Override
    public XmlWrapper<List<QueryParameter>> findParameters(String hql) {
        return new XmlWrapper<List<QueryParameter>>(hqlWebService.findParameters(hql));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable, I extends Serializable> XmlWrapper<I> save(String pojo, XmlWrapper<T> object) {
        return new XmlWrapper<I>((I) hqlWebService.save(object.getValue()));
    }

    @Override
    public <T extends Serializable> void delete(String pojo, XmlWrapper<T> object) {
        hqlWebService.delete(object.getValue());
    }

    @Override
    public StreamingOutput getHibernateWebResolver() {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(new BufferedOutputStream(output));
                    oos.writeObject(hqlWebService.getHibernateWebResolver());
                } catch (RuntimeException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (oos != null) {
                            oos.close();
                        }
                    } catch (Exception unhandled) {
                        //
                    }
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable> XmlWrapper<T> get(String type, String id) {
        try {
            return (XmlWrapper<T>) new XmlWrapper<Object>(hqlWebService.get((Class<Serializable>) Class.forName(type), id));
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ExecutionResult execute(QueryParameters queryParameters) {
        return hqlWebService.execute(queryParameters);
    }

    @Override
    public ExecutionResult execute(String hql) {
        return execute(new QueryParameters(hql));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public <T extends Serializable> XmlWrapper<List<T>> executePlainResult(QueryParameters queryParameters) {
        return (XmlWrapper) execute(queryParameters).getResults();
    }

    @Override
    public <T extends Serializable> XmlWrapper<List<T>> executePlainResult(String hql) {
        return executePlainResult(new QueryParameters(hql));
    }
}

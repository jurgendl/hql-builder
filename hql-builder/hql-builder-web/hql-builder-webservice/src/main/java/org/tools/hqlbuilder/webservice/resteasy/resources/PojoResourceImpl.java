package org.tools.hqlbuilder.webservice.resteasy.resources;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.annotation.Resource;
import javax.ws.rs.core.StreamingOutput;

import org.jhaws.common.jaxb.XmlWrapper;
import org.springframework.stereotype.Component;
import org.tools.hqlbuilder.common.DetachedHqlService;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.HibernateWebResolver;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.QueryParameters;
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

    protected boolean wrapped = false;

    public PojoResourceImpl() {
        super();
    }

    protected HqlWebService getService() {
        if (!wrapped) {
            hqlWebService = new DetachedHqlWebService(hqlWebService);
            wrapped = true;
        }
        return hqlWebService;
    }

    protected static class DetachedHqlWebService extends DetachedHqlService implements HqlWebService {
        public DetachedHqlWebService(HqlWebService delegate) {
            setDelegate(delegate);
        }
    }

    @Override
    public String ping() {
        try {
            return "hello from Rest-Easy, service " + getService().getProject() + " on " + getService().getConnectionInfo() + " is ready to use";
        } catch (Exception ex) {
            return "hello from Rest-Easy, service is not available: " + ex;
        }
    }

    @Override
    public XmlWrapper<SortedSet<String>> getClasses() {
        return new XmlWrapper<>(getService().getClasses());
    }

    @Override
    public String getSqlForHql(String hql) {
        return getService().getSqlForHql(hql);
    }

    @Override
    public XmlWrapper<List<String>> getProperties(String classname) {
        return new XmlWrapper<>(getService().getProperties(classname));
    }

    @Override
    public String getConnectionInfo() {
        return getService().getConnectionInfo();
    }

    @Override
    public String getProject() {
        return getService().getProject();
    }

    @Override
    public XmlWrapper<List<String>> search(String text, String typeName, int hitsPerPage) {
        try {
            return new XmlWrapper<>(getService().search(text, typeName, hitsPerPage));
        } catch (UnsupportedOperationException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public XmlWrapper<Set<String>> getReservedKeywords() {
        return new XmlWrapper<>(getService().getReservedKeywords());
    }

    @Override
    public XmlWrapper<Map<String, String>> getNamedQueries() {
        return new XmlWrapper<>(getService().getNamedQueries());
    }

    @Override
    public String createScript() {
        return getService().createScript();
    }

    @Override
    public XmlWrapper<Map<String, String>> getHibernateInfo() {
        return new XmlWrapper<>(getService().getHibernateInfo());
    }

    @Override
    public String getHibernateHelpURL() {
        return INTERNET_SHORTCUT_URL + getService().getHibernateHelpURL();
    }

    @Override
    public String getHqlHelpURL() {
        return INTERNET_SHORTCUT_URL + getService().getHqlHelpURL();
    }

    @Override
    public String getLuceneHelpURL() {
        return INTERNET_SHORTCUT_URL + getService().getLuceneHelpURL();
    }

    @Override
    public XmlWrapper<List<String>> getPropertyNames(String key, String[] parts) {
        return new XmlWrapper<>(getService().getPropertyNames(key, parts));
    }

    @Override
    public void sql(String[] sql) {
        getService().sql(sql);
    }

    @Override
    public XmlWrapper<List<QueryParameter>> findParameters(String hql) {
        return new XmlWrapper<>(getService().findParameters(hql));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable, I extends Serializable> XmlWrapper<I> save(String pojo, XmlWrapper<T> object) {
        return new XmlWrapper<>((I) getService().save(object.getValue()));
    }

    @Override
    public <T extends Serializable> void delete(String pojo, XmlWrapper<T> object) {
        getService().delete(object.getValue());
    }

    @Override
    public StreamingOutput getSerializedHibernateWebResolver() {
        return output -> {
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(output))) {
                oos.writeObject(getHibernateWebResolver());
                oos.flush();
            } catch (Exception ex) {
                throw new javax.ws.rs.WebApplicationException(ex);
            }
        };
    }

    @Override
    public HibernateWebResolver getHibernateWebResolver() {
        return getService().getHibernateWebResolver();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T extends Serializable> XmlWrapper<T> get(String type, String id) {
        try {
            XmlWrapper<?> xmlWrapper = new XmlWrapper(getService().get((Class<Serializable>) Class.forName(type), id));
            return (XmlWrapper<T>) xmlWrapper;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ExecutionResult execute(QueryParameters queryParameters) {
        ExecutionResult execute = getService().execute(queryParameters);
        return execute;
    }

    @Override
    public ExecutionResult execute(String hql) {
        return execute(new QueryParameters(hql, false));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public <T extends Serializable> XmlWrapper<List<T>> executePlainResult(QueryParameters queryParameters) {
        return (XmlWrapper) execute(queryParameters).getResults();
    }

    @Override
    public <T extends Serializable> XmlWrapper<List<T>> executePlainResult(String hql) {
        return executePlainResult(new QueryParameters(hql, false));
    }

    @Override
    public String stopQuery(String uuid) {
        return String.valueOf(getService().stopQuery(uuid));
    }
}

package org.tools.hqlbuilder.webclient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.jhaws.common.web.xml.XmlWrapper;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.HibernateWebResolver;
import org.tools.hqlbuilder.common.HqlService;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.common.exceptions.ValidationException;
import org.tools.hqlbuilder.webcommon.resteasy.PojoResource;

public class HqlWebServiceClient extends HqlWebServiceClientFactory<PojoResource> implements HqlService {
    public HqlWebServiceClient() {
        super();
    }

    // public HqlWebServiceClient(String packages) {
    // super(new String[] { packages });
    // }
    //
    // public HqlWebServiceClient(String[] packages) {
    // super(packages);
    // }

    @Override
    public String getVersion() {
        return "?"; // TODO version = "?"
    }

    public String ping() {
        return getResource().ping();
    }

    @Override
    public String getSqlForHql(String hql) {
        return getResource().getSqlForHql(hql);
    }

    @Override
    public SortedSet<String> getClasses() {
        return getResource().getClasses().getValue();
    }

    @Override
    public List<String> getProperties(String classname) {
        return getResource().getProperties(classname).getValue();
    }

    @Override
    public String getConnectionInfo() {
        return getResource().getConnectionInfo();
    }

    @Override
    public String getProject() {
        return getResource().getProject();
    }

    @Override
    public List<String> search(String text, String typeName, int hitsPerPage) {
        return getResource().search(text, typeName, hitsPerPage).getValue();
    }

    @Override
    public Set<String> getReservedKeywords() {
        return getResource().getReservedKeywords().getValue();
    }

    @Override
    public Map<String, String> getNamedQueries() {
        return getResource().getNamedQueries().getValue();
    }

    @Override
    public String createScript() {
        return getResource().createScript();
    }

    @Override
    public Map<String, String> getHibernateInfo() {
        return getResource().getHibernateInfo().getValue();
    }

    @Override
    public String getHibernateHelpURL() {
        return getResource().getHibernateHelpURL();
    }

    @Override
    public String getHqlHelpURL() {
        return getResource().getHqlHelpURL();
    }

    @Override
    public String getLuceneHelpURL() {
        return getResource().getLuceneHelpURL();
    }

    @Override
    public List<String> getPropertyNames(String key, String[] parts) {
        return getResource().getPropertyNames(key, parts).getValue();
    }

    @Override
    public void sql(String[] sql) {
        getResource().sql(sql);
    }

    @Override
    public List<QueryParameter> findParameters(String hql) {
        return getResource().findParameters(hql).getValue();
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable, I extends Serializable> I save(String pojo, T object) {
        return (I) getResource().save(pojo, new XmlWrapper<T>(object)).getValue();
    }

    public <T extends Serializable> void delete(String pojo, T object) {
        getResource().delete(pojo, new XmlWrapper<T>(object));
    }

    @Override
    public ExecutionResult execute(QueryParameters queryParameters) {
        return getResource().execute(queryParameters);
    }

    @Override
    public HibernateWebResolver getHibernateWebResolver() {
        try {
            StreamingOutput hibernateWebResolver = getResource().getHibernateWebResolver();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            hibernateWebResolver.write(output);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(output.toByteArray()));
            return (HibernateWebResolver) ois.readObject();
        } catch (WebApplicationException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public <T extends Serializable, I extends Serializable> I save(T object) throws ValidationException {
        return save(object.getClass().getName(), object);
    }

    @Override
    public <T extends Serializable> void delete(T object) {
        delete(object.getClass().getName(), object);
    }

    @Override
    public void log() {
        //
    }

    @Override
    public <T extends Serializable, I extends Serializable> T get(Class<T> type, I id) {
        return type.cast(getResource().get(type.getName(), String.valueOf(id)).getValue());
    }
}

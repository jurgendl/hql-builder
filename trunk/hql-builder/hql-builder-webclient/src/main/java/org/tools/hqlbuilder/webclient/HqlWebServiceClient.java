package org.tools.hqlbuilder.webclient;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.HibernateWebResolver;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.exceptions.ValidationException;
import org.tools.hqlbuilder.webservice.HqlWebService;

public class HqlWebServiceClient implements HqlWebService {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(HqlWebServiceClient.class);

    public static void main(String[] args) {
        try {
            ClientRequest request = new ClientRequest("http://localhost:80/hqlbuilder/rest/pojo/project");
            request.accept("text/plain");
            ClientResponse<String> response = request.get(String.class);
            System.out.println(response.getEntity());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public SortedSet<String> getClasses() {
        return null;
    }

    @Override
    public String getSqlForHql(String hql) {
        return null;
    }

    @Override
    public ExecutionResult execute(String string, QueryParameter... queryParameters) {
        return null;
    }

    @Override
    public ExecutionResult execute(String string, List<QueryParameter> queryParameters) {
        return null;
    }

    @Override
    public ExecutionResult execute(String string, int max, QueryParameter... queryParameters) {
        return null;
    }

    @Override
    public ExecutionResult execute(String string, int max, List<QueryParameter> queryParameters) {
        return null;
    }

    @Override
    public HibernateWebResolver getHibernateWebResolver() {
        return null;
    }

    @Override
    public List<String> getProperties(String classname) {
        return null;
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public String getProject() {
        return null;
    }

    @Override
    public <T> T save(T object) throws ValidationException {
        return null;
    }

    @Override
    public <T> void delete(T object) {
        //
    }

    @Override
    public List<String> search(String text, String typeName, int hitsPerPage) throws UnsupportedOperationException, IOException {
        return null;
    }

    @Override
    public Set<String> getReservedKeywords() {
        return null;
    }

    @Override
    public Map<String, String> getNamedQueries() {
        return null;
    }

    @Override
    public List<QueryParameter> findParameters(String hql) {
        return null;
    }

    @Override
    public List<String> getPropertyNames(String key, String[] parts) {
        return null;
    }

    @Override
    public void log() {
        //
    }

    @Override
    public String createScript() {
        return null;
    }

    @Override
    public void sql(String... sql) {
        //
    }

    @Override
    public Map<String, String> getHibernateInfo() {
        return null;
    }

    @Override
    public String getHibernateHelpURL() {
        return null;
    }

    @Override
    public String getHqlHelpURL() {
        return null;
    }

    @Override
    public String getLuceneHelpURL() {
        return null;
    }
}

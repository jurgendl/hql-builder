package org.tools.hqlbuilder.webclient;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.HibernateWebResolver;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.exceptions.ValidationException;
import org.tools.hqlbuilder.webservice.HqlWebService;
import org.tools.hqlbuilder.webservice.resteasy.PojoResource;

public class HqlWebServiceClient implements HqlWebService {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(HqlWebServiceClient.class);

    public static <T> T create(final Class<T> classs) throws Exception {
        ProxyFactory factory = new ProxyFactory();
        factory.setInterfaces(new Class[] { classs });
        Class<?> clazz = factory.createClass();
        MethodHandler handler = new MethodHandler() {
            @Override
            public Object invoke(Object self, Method method, Method forwarder, Object[] args) throws Throwable {
                Path classPath = classs.getAnnotation(Path.class);
                Path methodPath = method.getAnnotation(Path.class);
                Produces produces = method.getAnnotation(Produces.class);
                Consumes consumes = method.getAnnotation(Consumes.class);
                GET get = method.getAnnotation(GET.class);
                PUT put = method.getAnnotation(PUT.class);
                POST post = method.getAnnotation(POST.class);
                DELETE delete = method.getAnnotation(DELETE.class);
                String uri = "http://localhost:80/hqlbuilder/rest" + classPath.value() + methodPath.value();
                System.out.println(uri);
                ClientRequest request = new ClientRequest(uri);
                if (produces != null) {
                    request.accept(produces.value()[0]);
                }
                try {
                    if (get != null) {
                        ClientResponse<?> response = request.get(method.getReturnType());
                        return response.getEntity();
                    }
                    if (put != null) {
                        ClientResponse<?> response = request.put(method.getReturnType());
                        return response.getEntity();
                    }
                    if (post != null) {
                        ClientResponse<?> response = request.post(method.getReturnType());
                        return response.getEntity();
                    }
                    if (delete != null) {
                        ClientResponse<?> response = request.delete(method.getReturnType());
                        return response.getEntity();
                    }
                } catch (org.jboss.resteasy.spi.UnhandledException ex) {
                    return ex.getCause();
                }
                throw new IllegalArgumentException("" + method);
            }
        };
        Object instance = clazz.newInstance();
        ((ProxyObject) instance).setHandler(handler);
        return classs.cast(instance);
    }

    public static void main(String[] args) {
        try {
            PojoResource t = create(PojoResource.class);
            System.out.println(t.getSqlForHql("from Pojo"));
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

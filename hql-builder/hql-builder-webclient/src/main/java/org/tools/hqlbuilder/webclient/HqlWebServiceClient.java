package org.tools.hqlbuilder.webclient;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.UriBuilder;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.common.jaxb.XmlWrapper;
import org.tools.hqlbuilder.webcommon.resteasy.PojoResource;

public class HqlWebServiceClient implements PojoResource, MethodHandler, InitializingBean {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(HqlWebServiceClient.class);

    protected URI uri;

    protected PojoResource pojoResource;

    public HqlWebServiceClient() {
        super();
    }

    /**
     * @see javassist.util.proxy.MethodHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object invoke(Object self, Method method, Method forwarder, Object[] args) throws Throwable {
        logger.debug("Method=" + method);
        Produces produces = method.getAnnotation(Produces.class);
        logger.debug("produces=" + (produces == null ? null : Arrays.toString(produces.value())));
        Consumes consumes = method.getAnnotation(Consumes.class);
        logger.debug("consumes=" + (consumes == null ? null : Arrays.toString(consumes.value())));
        GET get = method.getAnnotation(GET.class);
        if (get != null) {
            logger.debug("GET=" + get);
        }
        PUT put = method.getAnnotation(PUT.class);
        if (put != null) {
            logger.debug("PUT=" + put);
        }
        POST post = method.getAnnotation(POST.class);
        if (post != null) {
            logger.debug("POST=" + post);
        }
        DELETE delete = method.getAnnotation(DELETE.class);
        if (delete != null) {
            logger.debug("DELETE=" + delete);
        }
        HEAD head = method.getAnnotation(HEAD.class);
        if (head != null) {
            logger.debug("HEAD=" + head);
        }
        OPTIONS options = method.getAnnotation(OPTIONS.class);
        if (options != null) {
            logger.debug("OPTIONS=" + options);
        }
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class<?>[] parameterTypes = method.getParameterTypes();
        UriBuilder uriBuilder = UriBuilder.fromUri(uri).path(PojoResource.class).path(method);
        List<Object> notAcceptedParameters = new ArrayList<Object>(Arrays.asList(args));
        for (int i = 0; i < parameterTypes.length; i++) {
            for (Annotation parameterAnnotation : parameterAnnotations[i]) {
                if (parameterAnnotation instanceof QueryParam) {
                    QueryParam queryParam = QueryParam.class.cast(parameterAnnotation);
                    String name = queryParam.value();
                    Object values = args[i];
                    uriBuilder.queryParam(name, values);
                    notAcceptedParameters.remove(args[i]);
                }
            }
        }
        String uriPath = uriBuilder.build().toASCIIString();
        logger.debug("URI=" + uriPath);
        System.out.println(uriPath);
        ClientRequest request = new ClientRequest(uriPath);
        for (int i = 0; i < parameterTypes.length; i++) {
            for (Annotation parameterAnnotation : parameterAnnotations[i]) {
                if (parameterAnnotation instanceof MatrixParam) {
                    MatrixParam matrixParam = MatrixParam.class.cast(parameterAnnotation);
                    String parameterName = matrixParam.value();
                    Object value = args[i];
                    request.matrixParameter(parameterName, value);
                    notAcceptedParameters.remove(args[i]);
                }
                if (parameterAnnotation instanceof FormParam) {
                    FormParam formParam = FormParam.class.cast(parameterAnnotation);
                    String parameterName = formParam.value();
                    Object value = args[i];
                    request.formParameter(parameterName, value);
                    notAcceptedParameters.remove(args[i]);
                }
                if (parameterAnnotation instanceof PathParam) {
                    PathParam pathParam = PathParam.class.cast(parameterAnnotation);
                    String parameterName = pathParam.value();
                    Object value = args[i];
                    request.pathParameter(parameterName, value);
                    notAcceptedParameters.remove(args[i]);
                }
                if (parameterAnnotation instanceof CookieParam) {
                    CookieParam cookieParam = CookieParam.class.cast(parameterAnnotation);
                    String cookieName = cookieParam.value();
                    Object value = args[i];
                    request.cookie(cookieName, value);
                    notAcceptedParameters.remove(args[i]);
                }
                if (parameterAnnotation instanceof HeaderParam) {
                    HeaderParam headerParam = HeaderParam.class.cast(parameterAnnotation);
                    String headerName = headerParam.value();
                    Object value = args[i];
                    request.header(headerName, value);
                    notAcceptedParameters.remove(args[i]);
                }
            }
        }
        if (produces != null && produces.value() != null && produces.value().length > 0) {
            request.accept(produces.value()[0]);
        }
        if (consumes != null && consumes.value() != null && consumes.value().length > 0) {
            if (notAcceptedParameters.size() != 1) {
                throw new IllegalArgumentException("body: " + notAcceptedParameters);
            }
            Object data = notAcceptedParameters.get(0);
            if (data != null) {
                request.body(consumes.value()[0], data);
            }
        }
        try {
            ClientResponse<?> response = null;
            if (get != null) {
                response = request.get(method.getReturnType());
            } else if (put != null) {
                response = request.put(method.getReturnType());
            } else if (post != null) {
                response = request.post(method.getReturnType());
            } else if (delete != null) {
                response = request.delete(method.getReturnType());
            } else if (head != null) {
                response = request.head();
            } else if (options != null) {
                response = request.options(method.getReturnType());
            } else {
                throw new IllegalArgumentException();
            }
            return response == null ? null : response.getEntity();
        } catch (org.jboss.resteasy.spi.UnhandledException ex) {
            return ex.getCause();
        }
    }

    @Override
    public String ping() {
        return this.pojoResource.ping();
    }

    @Override
    public String getSqlForHql(String hql) {
        return this.pojoResource.getSqlForHql(hql);
    }

    @Override
    public XmlWrapper<SortedSet<String>> getClasses() {
        return this.pojoResource.getClasses();
    }

    @Override
    public XmlWrapper<List<String>> getProperties(String classname) {
        return this.pojoResource.getProperties(classname);
    }

    @Override
    public String getConnectionInfo() {
        return this.pojoResource.getConnectionInfo();
    }

    @Override
    public String getProject() {
        return this.pojoResource.getProject();
    }

    @Override
    public XmlWrapper<List<String>> search(String text, String typeName, int hitsPerPage) {
        return this.pojoResource.search(text, typeName, hitsPerPage);
    }

    @Override
    public XmlWrapper<Set<String>> getReservedKeywords() {
        return this.pojoResource.getReservedKeywords();
    }

    @Override
    public XmlWrapper<Map<String, String>> getNamedQueries() {
        return this.pojoResource.getNamedQueries();
    }

    @Override
    public String createScript() {
        return this.pojoResource.createScript();
    }

    @Override
    public XmlWrapper<Map<String, String>> getHibernateInfo() {
        return this.pojoResource.getHibernateInfo();
    }

    @Override
    public String getHibernateHelpURL() {
        return this.pojoResource.getHibernateHelpURL();
    }

    @Override
    public String getHqlHelpURL() {
        return this.pojoResource.getHqlHelpURL();
    }

    @Override
    public String getLuceneHelpURL() {
        return this.pojoResource.getLuceneHelpURL();
    }

    @Override
    public XmlWrapper<List<String>> getPropertyNames(String key, String[] parts) {
        return this.pojoResource.getPropertyNames(key, parts);
    }

    @Override
    public void sql(String[] sql) {
        this.pojoResource.sql(sql);
    }

    @Override
    public List<QueryParameter> findParameters(String hql) {
        return this.pojoResource.findParameters(hql);
    }

    @Override
    public void save(String pojo, Object object) {
        this.pojoResource.save(pojo, object);
    }

    @Override
    public void delete(String pojo, Object object) {
        this.pojoResource.delete(pojo, object);
    }

    @Override
    public ExecutionResult execute(QueryParameters queryParameters) {
        return this.pojoResource.execute(queryParameters);
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ProxyFactory factory = new ProxyFactory();
        factory.setInterfaces(new Class[] { PojoResource.class });
        Class<?> clazz = factory.createClass();
        Object instance;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        ((ProxyObject) instance).setHandler(this);
        pojoResource = PojoResource.class.cast(instance);
    }

    public URI getUri() {
        return this.uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}

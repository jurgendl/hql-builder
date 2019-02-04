package org.tools.hqlbuilder.webclient;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriBuilder;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScheme;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.tools.hqlbuilder.webcommon.ResteasyProvidersBean;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

/**
 * @deprecated see {@link org.jhaws.common.net.resteasy.client.RestEasyClient}
 */
@Deprecated
public abstract class HqlWebServiceClientFactory<R> implements MethodHandler, InitializingBean {
    protected final Logger logger;

    // protected String[] packages;

    protected String serviceUrl;

    protected ServiceUrlBuilder serviceUrlBuilder;

    protected R resource;

    protected Class<R> resourceClass;

    protected ResteasyProviderFactory resteasyProvider;

    protected ResteasyProvidersBean resteasyProvidersBean;

    protected org.jboss.resteasy.client.ClientExecutor clientExecutor;

    public HqlWebServiceClientFactory() {
        this.logger = setupLogger();
    }

    // public HqlWebServiceClientFactory(String[] packages) {
    // this.packages = packages;
    // this.logger = setupLogger();
    // }

    public Logger setupLogger() {
        return LoggerFactory.getLogger(getClass());
    }

    // protected ResteasyProviderFactory setupResteasyProvider() {
    // ResteasyProviderFactory resteasyProviderFactory = ResteasyProviderFactory.getInstance();
    // RegisterBuiltin.register(resteasyProviderFactory);
    // JAXBContextResolver provider = new JAXBContextResolver(getPackages());
    // resteasyProviderFactory.registerProviderInstance(provider);
    // return resteasyProviderFactory;
    // }

    protected org.jboss.resteasy.client.ClientExecutor setupClientExecutor() {
        AuthCache authCache = new BasicAuthCache();
        AuthScheme basicAuth = new BasicScheme();
        authCache.put(new HttpHost(getServiceUrl()), basicAuth);

        BasicHttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(URI.create(getServiceUrl()).getHost(), authCache);

        // BasicCredentialsProvider credentialsProvider = new
        // BasicCredentialsProvider();
        // Credentials credentials = new
        // UsernamePasswordCredentials("hqlbuilder", "hqlbuilder");
        // credentialsProvider.setCredentials(new
        // AuthScope(URI.create(getServiceUrl()).getHost(),
        // URI.create(getServiceUrl()).getPort()),
        // credentials);

        HttpClient httpClient = HttpClientBuilder.create()//
                // .setDefaultCredentialsProvider(credentialsProvider)//
                .build();//

        return new ApacheHttpClient4Executor(httpClient, localContext);
    }

    @SuppressWarnings("unchecked")
    protected Class<R> setupResourceClass() {
        return (Class<R>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected R setupResource() {
        ProxyFactory factory = new ProxyFactory();
        factory.setInterfaces(new Class[] { getResourceClass() });
        Class<?> clazz = factory.createClass();
        Object instance;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        ProxyObject.class.cast(instance).setHandler(this);
        return getResourceClass().cast(instance);
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.resourceClass == null) {
            this.resourceClass = setupResourceClass();
        }
        if (this.resteasyProvider == null) {
            if (resteasyProvidersBean != null) {
                resteasyProvider = resteasyProvidersBean.getInstance();
                // } else if (getPackages() != null) {
                // this.resteasyProvider = setupResteasyProvider();
            } else {
                throw new IllegalArgumentException("either packages or restEasyProvidersBean is required");
            }
        }
        if (this.clientExecutor == null) {
            this.clientExecutor = setupClientExecutor();
        }
        if (this.resource == null) {
            this.resource = setupResource();
        }
    }

    /**
     * @see javassist.util.proxy.MethodHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object invoke(Object self, Method method, Method forwarder, Object[] args) throws Throwable {
        logger.debug("Method=" + method);
        Produces produces = method.getAnnotation(Produces.class);
        if (produces != null) {
            logger.debug("produces=" + Arrays.toString(produces.value()));
        }
        Consumes consumes = method.getAnnotation(Consumes.class);
        if (consumes != null) {
            logger.debug("consumes=" + Arrays.toString(consumes.value()));
        }
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
        UriBuilder uriBuilder = UriBuilder.fromUri(URI.create(getServiceUrl())).path(getResourceClass()).path(method);
        List<Object> notAcceptedParameters = new ArrayList<>(Arrays.asList(args));
        Map<String, Object> templateValues = new HashMap<>();
        for (int i = 0; i < parameterTypes.length; i++) {
            logger.debug("parameter: " + parameterTypes[i].getName() + " " + args[i]);
            logger.debug("parameter-annotations: " + Arrays.toString(parameterAnnotations[i]));
            for (Annotation parameterAnnotation : parameterAnnotations[i]) {
                if (parameterAnnotation instanceof QueryParam) {
                    QueryParam queryParam = QueryParam.class.cast(parameterAnnotation);
                    String name = queryParam.value();
                    Object values = args[i];
                    uriBuilder.queryParam(name, values);
                    notAcceptedParameters.remove(args[i]);
                    logger.debug("QueryParam[" + name + "=" + values);
                }
                if (parameterAnnotation instanceof PathParam) {
                    PathParam pathParam = PathParam.class.cast(parameterAnnotation);
                    String parameterName = pathParam.value();
                    Object value = args[i];
                    templateValues.put(parameterName, value);
                    logger.debug("PathParam[" + parameterName + "=" + value);
                }
            }
        }

        uriBuilder = uriBuilder.resolveTemplates(templateValues);
        String uriPath = uriBuilder.build().toASCIIString();
        logger.debug("URI=" + uriPath);

        org.jboss.resteasy.client.ClientRequest request = new org.jboss.resteasy.client.ClientRequest(uriBuilder, getClientExecutor(),
                getResteasyProvider());

        for (int i = 0; i < parameterTypes.length; i++) {
            logger.debug("parameter: " + parameterTypes[i].getName() + " " + args[i]);
            logger.debug("parameter-annotations: " + Arrays.toString(parameterAnnotations[i]));
            for (Annotation parameterAnnotation : parameterAnnotations[i]) {
                if (parameterAnnotation instanceof PathParam) {
                    PathParam pathParam = PathParam.class.cast(parameterAnnotation);
                    String parameterName = pathParam.value();
                    Object value = args[i];
                    request.pathParameter(parameterName, value);
                    notAcceptedParameters.remove(args[i]);
                    logger.debug("PathParam[" + parameterName + "=" + value);
                }
                if (parameterAnnotation instanceof MatrixParam) {
                    MatrixParam matrixParam = MatrixParam.class.cast(parameterAnnotation);
                    String parameterName = matrixParam.value();
                    Object value = args[i];
                    request.matrixParameter(parameterName, value);
                    notAcceptedParameters.remove(args[i]);
                    logger.debug("MatrixParam[" + parameterName + "=" + value);
                }
                if (parameterAnnotation instanceof FormParam) {
                    FormParam formParam = FormParam.class.cast(parameterAnnotation);
                    String parameterName = formParam.value();
                    Object value = args[i];
                    request.formParameter(parameterName, value);
                    notAcceptedParameters.remove(args[i]);
                    logger.debug("FormParam[" + parameterName + "=" + value);
                }
                if (parameterAnnotation instanceof CookieParam) {
                    CookieParam cookieParam = CookieParam.class.cast(parameterAnnotation);
                    String cookieName = cookieParam.value();
                    Object value = args[i];
                    request.cookie(cookieName, value);
                    notAcceptedParameters.remove(args[i]);
                    logger.debug("CookieParam[" + cookieName + "=" + value);
                }
                if (parameterAnnotation instanceof HeaderParam) {
                    HeaderParam headerParam = HeaderParam.class.cast(parameterAnnotation);
                    String headerName = headerParam.value();
                    Object value = args[i];
                    request.header(headerName, value);
                    notAcceptedParameters.remove(args[i]);
                    logger.debug("HeaderParam[" + headerParam + "=" + value);
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
            org.jboss.resteasy.client.ClientResponse<?> response = null;
            Class<?> returnType = method.getReturnType();
            boolean stream = false;
            if (StreamingOutput.class.equals(returnType)) {
                returnType = InputStream.class;
                stream = true;
            }
            if (Void.TYPE.equals(returnType)) {
                if (get != null) {
                    response = request.get();
                } else if (put != null) {
                    response = request.put();
                } else if (post != null) {
                    response = request.post();
                } else if (delete != null) {
                    response = request.delete();
                } else if (head != null) {
                    response = request.head();
                } else if (options != null) {
                    response = request.options();
                } else {
                    throw new IllegalArgumentException();
                }
                if (response != null && !(200 <= response.getStatus() && response.getStatus() <= 299)) {
                    String errorpage = response.getEntity(String.class);
                    throw new HttpResponseException(response.getStatus(), errorpage);
                }
                return null;
            }
            if (get != null) {
                response = request.get(returnType);
            } else if (put != null) {
                response = request.put(returnType);
            } else if (post != null) {
                response = request.post(returnType);
            } else if (delete != null) {
                response = request.delete(returnType);
            } else if (head != null) {
                response = request.head();
            } else if (options != null) {
                response = request.options(returnType);
            } else {
                throw new IllegalArgumentException();
            }
            if (response == null) {
                throw new IOException("no response");
            }
            if (!(200 <= response.getStatus() && response.getStatus() <= 299)) {
                String errorpage = response.getEntity(String.class);
                throw new HttpResponseException(response.getStatus(), errorpage);
            }
            Object entity = response.getEntity();
            if (stream) {
                entity = new org.jhaws.common.net.resteasy.client.RedirectedStreamingOutput(InputStream.class.cast(entity));
            }
            return entity;
        } catch (org.jboss.resteasy.spi.UnhandledException ex) {
            return ex.getCause();
        }
    }

    // public String[] getPackages() {
    // return this.packages;
    // }

    public String getServiceUrl() {
        if (serviceUrl == null && serviceUrlBuilder != null) {
            serviceUrl = serviceUrlBuilder.getServiceUrl();
        }
        return this.serviceUrl;
    }

    public R getResource() {
        return this.resource;
    }

    public Class<R> getResourceClass() {
        return this.resourceClass;
    }

    public ResteasyProviderFactory getResteasyProvider() {
        return this.resteasyProvider;
    }

    // public void setPackages(String[] packages) {
    // this.packages = packages;
    // }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public void setResource(R resource) {
        this.resource = resource;
    }

    public void setResourceClass(Class<R> resourceClass) {
        this.resourceClass = resourceClass;
    }

    public void setResteasyProvider(ResteasyProviderFactory resteasyProvider) {
        this.resteasyProvider = resteasyProvider;
    }

    public org.jboss.resteasy.client.ClientExecutor getClientExecutor() {
        return this.clientExecutor;
    }

    public void setClientExecutor(org.jboss.resteasy.client.ClientExecutor clientExecutor) {
        this.clientExecutor = clientExecutor;
    }

    public ServiceUrlBuilder getServiceUrlBuilder() {
        return this.serviceUrlBuilder;
    }

    public void setServiceUrlBuilder(ServiceUrlBuilder serviceUrlBuilder) {
        this.serviceUrlBuilder = serviceUrlBuilder;
    }

    public ResteasyProvidersBean getResteasyProvidersBean() {
        return this.resteasyProvidersBean;
    }

    public void setResteasyProvidersBean(ResteasyProvidersBean resteasyProvidersBean) {
        this.resteasyProvidersBean = resteasyProvidersBean;
    }
}

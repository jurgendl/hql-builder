package org.tools.hqlbuilder.webclient;

import java.lang.reflect.Method;

import org.jhaws.common.net.resteasy.client.RestEasyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javassist.util.proxy.MethodHandler;


public abstract class HqlWebServiceClientFactory<R> implements MethodHandler, InitializingBean {
    protected final Logger logger;

    protected String serviceUrl;

    protected ServiceUrlBuilder serviceUrlBuilder;

    protected R resource;

    protected Class<R> resourceClass;

	protected RestEasyClient<R> restEasyClient;

    public HqlWebServiceClientFactory() {
        this.logger = setupLogger();
    }

    public Logger setupLogger() {
        return LoggerFactory.getLogger(getClass());
    }

    @SuppressWarnings("unchecked")
    protected Class<R> setupResourceClass() {
        return (Class<R>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

	protected RestEasyClient<R> setupRestEasyClient() {
		return new RestEasyClient<>(serviceUrl, this.resourceClass);
	}

    protected R setupResource() {
		return this.restEasyClient.proxy();
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.resourceClass == null) {
            this.resourceClass = setupResourceClass();
        }
		if (this.restEasyClient == null) {
			this.restEasyClient = setupRestEasyClient();
		}
        if (this.resource == null) {
            this.resource = setupResource();
        }
    }

    @Override
    public Object invoke(Object self, Method method, Method forwarder, Object[] args) throws Throwable {
		// this.resource.getClass().getMethod(name, parameterTypes);
        throw new UnsupportedOperationException();
    }

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

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public void setResource(R resource) {
        this.resource = resource;
    }

    public void setResourceClass(Class<R> resourceClass) {
        this.resourceClass = resourceClass;
    }

    public ServiceUrlBuilder getServiceUrlBuilder() {
        return this.serviceUrlBuilder;
    }

    public void setServiceUrlBuilder(ServiceUrlBuilder serviceUrlBuilder) {
        this.serviceUrlBuilder = serviceUrlBuilder;
    }
}

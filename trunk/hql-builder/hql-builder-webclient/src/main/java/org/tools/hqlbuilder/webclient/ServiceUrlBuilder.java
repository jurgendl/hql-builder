package org.tools.hqlbuilder.webclient;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;

public class ServiceUrlBuilder implements InitializingBean {
    protected Properties securityProperties;

    protected String serviceUrl;

    protected String protocol;

    protected String hostAddress;

    protected String contextPath;

    protected String restPath;

    protected Integer port;

    public ServiceUrlBuilder() {
        super();
    }

    public ServiceUrlBuilder(Properties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public Properties getSecurityProperties() {
        return this.securityProperties;
    }

    public void setSecurityProperties(Properties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public String getServiceUrl() {
        return this.serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        serviceUrl = new URI(getProtocol(), null, getHostAddress(), getPort(), append(getContextPath()) + append(getRestPath()), null, null)
                .toASCIIString();
    }

    protected String append(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }

    public String getProtocol() {
        if (protocol == null && securityProperties != null) {
            protocol = securityProperties.getProperty("protocol");
        }
        return this.protocol;
    }

    public String getHostAddress() {
        if (hostAddress == null) {
            try {
                hostAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException ex) {
                throw new RuntimeException(ex);
            }
        }
        return this.hostAddress;
    }

    public String getContextPath() {
        if (contextPath == null && securityProperties != null) {
            contextPath = securityProperties.getProperty("contextpath");
        }
        return this.contextPath;
    }

    public String getRestPath() {
        if (restPath == null && securityProperties != null) {
            restPath = securityProperties.getProperty("rest.root");
        }
        return this.restPath;
    }

    public Integer getPort() {
        if (port == null && securityProperties != null) {
            String tmp = securityProperties.getProperty("port");
            port = tmp == null ? 80 : Integer.parseInt(tmp);
        }
        return this.port;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setRestPath(String restPath) {
        this.restPath = restPath;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}

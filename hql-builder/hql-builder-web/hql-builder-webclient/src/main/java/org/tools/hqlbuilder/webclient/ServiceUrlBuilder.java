package org.tools.hqlbuilder.webclient;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;

public class ServiceUrlBuilder {
    protected Properties webProperties;

    protected String serviceUrl;

    protected String protocol;

    protected String hostAddress;

    protected String contextPath;

    protected String restPath;

    protected Integer port;

    @Autowired
    protected ServletContext servletContext;

    public ServiceUrlBuilder() {
        super();
    }

    public ServiceUrlBuilder(Properties webProperties) {
        this.webProperties = webProperties;
    }

    public Properties getWebProperties() {
        return this.webProperties;
    }

    public void setWebProperties(Properties webProperties) {
        this.webProperties = webProperties;
    }

    public String getServiceUrl() {
        if (serviceUrl == null) {
            try {
                serviceUrl = new URI(getProtocol(), null, getHostAddress(), getPort(), append(getContextPath()) + append(getRestPath()), null, null)
                        .toASCIIString();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return this.serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    protected String append(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }

    public String getRestPath() {
        if (restPath == null && webProperties != null) {
            restPath = webProperties.getProperty("rest.root");
        }
        return this.restPath;
    }

    public String getContextPath() {
        if (contextPath == null) {
            try {
                contextPath = webProperties.getProperty("contextpath").toString();
            } catch (Exception ex) {
                contextPath = servletContext.getContextPath();
            }
        }
        return this.contextPath;
    }

    public String getProtocol() {
        if (protocol == null) {
            if (webProperties != null && webProperties.containsKey("protocol")) {
                protocol = webProperties.getProperty("protocol");
            } else {
                protocol = getEndPoints().get(0)[0];
            }
        }
        return this.protocol;
    }

    public String getHostAddress() {
        if (hostAddress == null) {
            if (webProperties != null && webProperties.containsKey("host")) {
                hostAddress = webProperties.getProperty("host");
            } else {
                hostAddress = getEndPoints().get(0)[1];
            }
        }
        return this.hostAddress;
    }

    public Integer getPort() {
        if (port == null) {
            if (webProperties != null && webProperties.containsKey("port")) {
                port = Integer.parseInt(webProperties.getProperty("port"));
            } else {
                port = Integer.parseInt(getEndPoints().get(0)[2]);
            }
        }
        return this.port;
    }

    protected List<String[]> endPoints;

    public List<String[]> getEndPoints() {
        if (endPoints == null) {
            endPoints = new ArrayList<String[]>();
            try {
                MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
                Set<ObjectName> objs = mbs.queryNames(new ObjectName("*:type=Connector,*"),
                        Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
                String hostname = InetAddress.getLocalHost().getHostName();
                InetAddress[] addresses = InetAddress.getAllByName(hostname);
                for (Iterator<ObjectName> i = objs.iterator(); i.hasNext();) {
                    ObjectName obj = i.next();
                    String scheme = mbs.getAttribute(obj, "scheme").toString();
                    String serverport = obj.getKeyProperty("port");
                    for (InetAddress addr : addresses) {
                        if (addr instanceof Inet4Address) {
                            String host = addr.getHostAddress();
                            endPoints.add(new String[] { scheme, host, serverport });
                        }
                    }
                }
            } catch (RuntimeException ex) {
                throw ex;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (JMException ex) {
                throw new RuntimeException(ex);
            }
        }
        return endPoints;
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

    public ServletContext getServletContext() {
        return this.servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}

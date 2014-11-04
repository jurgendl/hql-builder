package org.tools.hqlbuilder.webcommon;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * @see http://www.journaldev.com/1945/servlet-listener-example-servletcontextlistener-httpsessionlistener-and-servletrequestlistener
 */
// @javax.servlet.annotation.WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {
    public static ServletContextListener get() {
        return ServletContextListener.instance;
    }

    private static ServletContextListener instance = new ServletContextListener();

    private ServletContext servletContext;

    public ServletContextListener() {
        super();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.get().servletContext = sce.getServletContext();
    }

    public ServletContext getServletContext() {
        return this.servletContext;
    }
}

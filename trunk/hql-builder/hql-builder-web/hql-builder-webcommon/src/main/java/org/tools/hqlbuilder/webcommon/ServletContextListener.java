package org.tools.hqlbuilder.webcommon;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * @see http://www.journaldev.com/1945/servlet-listener-example-servletcontextlistener-httpsessionlistener-and-servletrequestlistener
 */
// @javax.servlet.annotation.WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {
    private static ServletContextListener instance;

    private ServletContext servletContext;

    public ServletContextListener() {
        set(instance);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        get().servletContext = sce.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //
    }

    public static ServletContextListener get() {
        return instance;
    }

    private static void set(ServletContextListener instance) {
        if (instance != null) {
            throw new UnsupportedOperationException();
        }
        ServletContextListener.instance = instance;
    }

    public ServletContext getServletContext() {
        return this.servletContext;
    }
}

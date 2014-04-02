package org.tools.hqlbuilder.test;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @see https://wiki.eclipse.org/Jetty/Howto/Spring
 */
public class HqlWebServiceJetty {
    public static void main(String[] args) {
        try {
            Server server = new Server(8686);
            WebAppContext context = new WebAppContext();
            context.setDescriptor("WEB-INF/web.xml");
            context.setResourceBase("src/main/webapp");
            context.setContextPath("/hqlbuilder");
            context.setParentLoaderPriority(true);
            server.setHandler(context);
            server.start();
            server.join();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}

package org.tools.hqlbuilder.test;

/**
 * @see https://wiki.eclipse.org/Jetty/Howto/Spring
 */
public class HqlWebServiceJetty {
    public static void main(String[] args) {
        try {
            new HqlWebServiceJetty().startServer();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    public void startServer() throws Exception {
        // org.eclipse.jetty.Server server = new org.eclipse.jetty.Server(80);
        // org.eclipse.jetty. WebAppContext context = new org.eclipse.jetty.WebAppContext();
        // context.setDescriptor("WEB-INF/web.xml");
        // context.setResourceBase("src/main/webapp");
        // context.setContextPath("/hqlbuilder");
        // context.setParentLoaderPriority(true);
        // server.setHandler(context);
        // server.start();
        // server.join();
    }
}

package org.tools.hqlbuilder.client;

import javax.swing.JOptionPane;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tools.hqlbuilder.common.exceptions.ValidationException;
import org.tools.hqlbuilder.test.Pojo;

public class HqlBuilder {
    public static void main(final String[] arguments) {
        final ApplicationContext context = new ClassPathXmlApplicationContext("org/tools/hqlbuilder/client/spring-http-client-config.xml");
        final HqlServiceClient hqlServiceClient = (HqlServiceClient) context.getBean("hqlServiceClient");
        try {
            System.out.println(hqlServiceClient.getConnectionInfo());
            if (hqlServiceClient.execute("from Pojo", Integer.MAX_VALUE).getResults().size() == 0) {
                try {
                    hqlServiceClient.save(new Pojo());
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }
            HqlBuilderFrame.start(arguments, hqlServiceClient);
        } catch (org.springframework.remoting.RemoteConnectFailureException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "No connection to host");
        }
    }
}
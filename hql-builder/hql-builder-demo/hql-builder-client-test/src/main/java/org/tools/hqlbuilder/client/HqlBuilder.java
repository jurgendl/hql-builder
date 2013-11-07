package org.tools.hqlbuilder.client;

import java.util.Properties;

import javax.swing.JOptionPane;

import org.hibernate.Hibernate;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tools.hqlbuilder.common.exceptions.ValidationException;
import org.tools.hqlbuilder.test.Pojo;

public class HqlBuilder {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HqlBuilder.class);

    public static void main(final String[] arguments) {
        String v = "3";
        try {
            Properties properties = new Properties();
            properties.load(Hibernate.class.getResourceAsStream("META-INF/MANIFEST.MF"));
            String iv = properties.getProperty("Implementation-Version");
            if (iv.startsWith("4")) {
                v = "4";
            }
        } catch (Throwable ex) {
            //
        }
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("org/tools/hqlbuilder/test/applicationContext-" + v + ".xml");
        final HqlServiceClient hqlServiceClient = (HqlServiceClient) context.getBean("hqlServiceClient");
        try {
            logger.debug(hqlServiceClient.getConnectionInfo());
            if (hqlServiceClient.execute("from Pojo", Integer.MAX_VALUE).getResults().size() == 0) {
                try {
                    hqlServiceClient.save(new Pojo());
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }
            HqlBuilderFrame.start(arguments, new HqlServiceClientLoader() {
                @Override
                public HqlServiceClient getHqlServiceClient() {
                    return hqlServiceClient;
                }
            });
        } catch (org.springframework.remoting.RemoteConnectFailureException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "No connection to host");
        }
    }
}
package org.tools.hqlbuilder.client;

import javax.swing.JOptionPane;

import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.swingeasy.UIUtils;
import org.tools.hqlbuilder.common.CommonUtils;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.common.exceptions.ValidationException;
import org.tools.hqlbuilder.test.Lang;
import org.tools.hqlbuilder.test.User;

public class HqlBuilder {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HqlBuilder.class);

    public static void main(final String[] arguments) {
        UIUtils.systemLookAndFeel();
        String v = "3";
        try {
            String hibv = CommonUtils.readManifestVersion("org.hibernate.Hibernate");
            if (hibv == null) {
                hibv = CommonUtils.readMavenVersion("org.hibernate:hibernate");
            }
            if (hibv == null) {
                hibv = CommonUtils.readMavenVersion("org.hibernate:hibernate-core");
            }
            logger.info(hibv);
            if (hibv.startsWith("4")) {
                v = "4";
            }
        } catch (Throwable ex) {
            ex.printStackTrace(System.out);
        }
        logger.info("Hibernate " + v + "x");
        @SuppressWarnings("resource")
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("org/tools/hqlbuilder/client/spring-http-client-config.xml");
        final HqlServiceClientImpl hqlServiceClient = (HqlServiceClientImpl) context.getBean("hqlServiceClient");
        try {
            logger.debug(hqlServiceClient.getConnectionInfo());
            if (hqlServiceClient.execute(new QueryParameters("from User")).getResults().getValue().size() == 0) {
                try {
                    Lang langEn = new Lang("en");
                    hqlServiceClient.save(langEn);
                    User user = new User("firstName", "lastName", "test@gmail.com");
                    user.setLanguage(langEn);
                    hqlServiceClient.save(user);
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
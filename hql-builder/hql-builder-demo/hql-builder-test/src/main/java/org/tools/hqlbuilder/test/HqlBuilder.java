package org.tools.hqlbuilder.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tools.hqlbuilder.client.HqlBuilderFrame;
import org.tools.hqlbuilder.client.HqlServiceClient;
import org.tools.hqlbuilder.client.HqlServiceClientImpl;
import org.tools.hqlbuilder.client.HqlServiceClientLoader;
import org.tools.hqlbuilder.common.CommonUtils;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.common.exceptions.ValidationException;

public class HqlBuilder {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HqlBuilder.class);

    public static void main(String[] args) {
        try {
            File directory = new File("DerbyTestDb");
            try {
                FileUtils.deleteDirectory(directory);
            } catch (IOException ex) {
                //
            }
            if (directory.exists()) {
                throw new RuntimeException(directory + " not deleted");
            }
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
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("org/tools/hqlbuilder/test/applicationContext-" + v + ".xml");
            final HqlServiceClientImpl hqlServiceClient = (HqlServiceClientImpl) context.getBean("hqlServiceClient");
            hqlServiceClient.createScript();
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
            HqlBuilderFrame.start(args, new HqlServiceClientLoader() {
                @Override
                public HqlServiceClient getHqlServiceClient() {
                    return hqlServiceClient;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            System.exit(-1);
        }
    }
}

package org.tools.hqlbuilder.test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.hibernate.Hibernate;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tools.hqlbuilder.client.HqlBuilderFrame;
import org.tools.hqlbuilder.client.HqlServiceClient;
import org.tools.hqlbuilder.client.HqlServiceClientImpl;
import org.tools.hqlbuilder.client.HqlServiceClientLoader;
import org.tools.hqlbuilder.common.exceptions.ValidationException.InvalidValue;

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
            final HqlServiceClientImpl hqlServiceClient = (HqlServiceClientImpl) context.getBean("hqlServiceClient");
            hqlServiceClient.createScript();
            List<Object> results = hqlServiceClient.execute("from Pojo", Integer.MAX_VALUE).getResults();
            if (results.size() == 0) {
                Pojo object = new Pojo();
                object.setValue("value");
                object.setRegexDigits("123");
                try {
                    object.setFrom0To100(150);
                    object.getEmbedded().setFrom100(50);
                    hqlServiceClient.save(object);
                } catch (org.tools.hqlbuilder.common.exceptions.ValidationException e) {
                    for (InvalidValue iv : e.getInvalidValues()) {
                        logger.debug(String.valueOf(iv));
                    }
                    object.setSuperNotNull("superNotNull");
                    object.setFrom0To100(50);
                    object.getEmbedded().setFrom100(150);
                    object = hqlServiceClient.save(object);
                }

                Lang en = new Lang();
                en.setCode("en");
                en = hqlServiceClient.save(en);
                logger.info("en=" + en.getId());

                Lang nl = new Lang();
                nl.setCode("nl");
                nl = hqlServiceClient.save(nl);
                logger.info("nl=" + nl.getId());

                Rel2 r2;
                {
                    r2 = new Rel2();
                    r2 = hqlServiceClient.save(r2);

                    Rel2Trans t1 = new Rel2Trans();
                    t1.setLang(en);
                    t1.setRel2(r2);
                    t1 = hqlServiceClient.save(t1);

                    Rel2Trans t2 = new Rel2Trans();
                    t2.setLang(nl);
                    t2.setRel2(r2);
                    t2 = hqlServiceClient.save(t2);
                }

                object = hqlServiceClient.save(object);

                Rel1 r1;
                {
                    r1 = new Rel1();
                    r1.setPojo(object);
                    r1 = hqlServiceClient.save(r1);

                    r2.setRel1(r1);
                    r1.getRel2().add(r2);
                    hqlServiceClient.save(r2);

                    Rel1Trans t1 = new Rel1Trans();
                    t1.setLang(en);
                    t1.setRel1(r1);
                    t1 = hqlServiceClient.save(t1);

                    Rel1Trans t2 = new Rel1Trans();
                    t2.setLang(nl);
                    t2.setRel1(r1);
                    t2 = hqlServiceClient.save(t2);
                }

                {
                    Rel0 r0 = new Rel0();
                    r0.setRel1(r1);
                    r0 = hqlServiceClient.save(r0);
                }
            } else {
                logger.debug(String.valueOf(results.size()));
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

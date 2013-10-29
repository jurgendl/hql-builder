package org.tools.hqlbuilder.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.tools.hqlbuilder.client.HqlBuilderFrame;
import org.tools.hqlbuilder.client.HqlServiceClient;
import org.tools.hqlbuilder.client.HqlServiceClientImpl;
import org.tools.hqlbuilder.client.HqlServiceClientLoader;

public class HqlBuilder {
    public static void main(String[] args) {
        try {
            FileUtils.deleteDirectory(new File("DerbyTestDb"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        final HqlServiceClientImpl hqlServiceClient = (HqlServiceClientImpl) new org.springframework.context.support.ClassPathXmlApplicationContext(
                "org/tools/hqlbuilder/test/applicationContext.xml").getBean("hqlServiceClient");
        hqlServiceClient.sql("ALTER SESSION SET NLS_LANGUAGE = English");
        hqlServiceClient.createScript();
        List<Object> results = hqlServiceClient.execute("from Pojo", Integer.MAX_VALUE).getResults();
        if (results.size() == 0) {
            Pojo object = new Pojo();
            object.setValue("value");
            object.getPlainSet().add("element 1");
            object.getPlainSet().add("element 2");
            object.setRegexDigits("123");
            // try {
            // object.setFrom0To100(150);
            // object.getEmbedded().setFrom100(50);
            // hqlServiceClient.save(object);
            // } catch (org.tools.hqlbuilder.common.exceptions.ValidationException e) {
            // for (InvalidValue iv : e.getInvalidValues()) {
            // System.out.println(iv);
            // }
            object.setSuperNotNull("superNotNull");
            object.setFrom0To100(50);
            object.getEmbedded().setFrom100(150);
            hqlServiceClient.save(object);
            // }

            Lang en = new Lang();
            en.setCode("en");
            en = hqlServiceClient.save(en);
            System.out.println("en=" + en.getId());

            Lang nl = new Lang();
            nl.setCode("nl");
            nl = hqlServiceClient.save(nl);
            System.out.println("nl=" + nl.getId());

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

            {
                Rel1 r1 = new Rel1();
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
        } else {
            System.out.println(results.size());
        }
        HqlBuilderFrame.start(args, new HqlServiceClientLoader() {
            @Override
            public HqlServiceClient getHqlServiceClient() {
                return hqlServiceClient;
            }
        });
    }
}

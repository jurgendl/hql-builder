package org.tools.hqlbuilder.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.tools.hqlbuilder.client.HqlBuilderFrame;
import org.tools.hqlbuilder.client.HqlServiceClient;
import org.tools.hqlbuilder.client.HqlServiceClientImpl;
import org.tools.hqlbuilder.client.HqlServiceClientLoader;
import org.tools.hqlbuilder.common.exceptions.ValidationException.InvalidValue;

public class HqlBuilder {
    public static void main(String[] args) {
        try {
            FileUtils.deleteDirectory(new File("DerbyTestDb"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        final HqlServiceClientImpl hqlServiceClient = (HqlServiceClientImpl) new org.springframework.context.support.ClassPathXmlApplicationContext(
                "org/tools/hqlbuilder/test/applicationContext.xml").getBean("hqlServiceClient");
        hqlServiceClient.createScript();
        List<Object> results = hqlServiceClient.execute("from Pojo", Integer.MAX_VALUE).getResults();
        if (results.size() == 0) {
            Pojo object = new Pojo();
            object.setValue("value");
            object.getPlainSet().add("element 1");
            object.getPlainSet().add("element 2");
            object.setRegexDigits("123");
            try {
                object.setFrom0To100(150);
                object.getEmbedded().setFrom100(50);
                hqlServiceClient.save(object);
            } catch (org.tools.hqlbuilder.common.exceptions.ValidationException e) {
                for (InvalidValue iv : e.getInvalidValues()) {
                    System.out.println(iv);
                }
                object.setSuperNotNull("superNotNull");
                object.setFrom0To100(50);
                object.getEmbedded().setFrom100(150);
                hqlServiceClient.save(object);
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

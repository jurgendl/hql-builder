package org.tools.hqlbuilder.test;

import java.util.List;

import org.tools.hqlbuilder.client.HqlBuilderFrame;
import org.tools.hqlbuilder.client.HqlServiceClientImpl;
import org.tools.hqlbuilder.common.exceptions.ValidationException;

public class HqlBuilder {
    public static void main(String[] args) {
        HqlServiceClientImpl hqlServiceClient = (HqlServiceClientImpl) new org.springframework.context.support.ClassPathXmlApplicationContext(
                "org/tools/hqlbuilder/test/applicationContext.xml").getBean("hqlServiceClient");
        List<Object> results = hqlServiceClient.execute("from Pojo", Integer.MAX_VALUE).getResults();
        if (results.size() == 0) {
            try {
                Pojo object = new Pojo();
                object.getPlainSet().add("element 1");
                object.getPlainSet().add("element 2");
                hqlServiceClient.save(object);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(results.size());
        }
        HqlBuilderFrame.start(args, hqlServiceClient);
    }
}

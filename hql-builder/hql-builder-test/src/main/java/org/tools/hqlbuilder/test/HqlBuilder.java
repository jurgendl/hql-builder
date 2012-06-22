package org.tools.hqlbuilder.test;

import org.tools.hqlbuilder.client.HqlBuilderFrame;
import org.tools.hqlbuilder.client.HqlServiceClientImpl;
import org.tools.hqlbuilder.common.exceptions.ValidationException;

public class HqlBuilder {
    public static void main(String[] args) {
        HqlServiceClientImpl hqlServiceClient = (HqlServiceClientImpl) new org.springframework.context.support.ClassPathXmlApplicationContext(
                "org/tools/hqlbuilder/test/applicationContext.xml").getBean("hqlServiceClient");
        if (hqlServiceClient.execute("from Pojo", Integer.MAX_VALUE).getResults().size() == 0) {
            try {
                hqlServiceClient.save(new Pojo());
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
        HqlBuilderFrame.start(args, hqlServiceClient);
    }
}

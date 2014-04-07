package org.tools.hqlbuilder.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tools.hqlbuilder.client.HqlServiceClientImpl;

public class HqlBuilder extends org.tools.hqlbuilder.client.HqlBuilder {
    public static void main(String[] args) {
        new HqlBuilder(args);
    }

    public HqlBuilder(String[] args) {
        super(args);
    }

    @Override
    public HqlServiceClientImpl loadService(String v) {
        @SuppressWarnings("resource")
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("org/tools/hqlbuilder/test/applicationContext-" + v + ".xml");
        HqlServiceClientImpl hqlServiceClient = (HqlServiceClientImpl) context.getBean("hqlServiceClient");
        return hqlServiceClient;
    }
}

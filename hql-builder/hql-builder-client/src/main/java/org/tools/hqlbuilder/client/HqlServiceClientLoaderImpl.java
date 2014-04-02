package org.tools.hqlbuilder.client;

public class HqlServiceClientLoaderImpl implements HqlServiceClientLoader {
    @Override
    public HqlServiceClient getHqlServiceClient() {
        try {
            Class<?> clazz = Class.forName("org.springframework.context.support.ClassPathXmlApplicationContext");
            Object context = clazz.getConstructor(String.class).newInstance("org/tools/hqlbuilder/applicationContext.xml");
            HqlServiceClient hqlServiceClient = (HqlServiceClient) clazz.getMethod("getBean", String.class).invoke(context, "hqlServiceClient");
            // HqlServiceClient hqlServiceClient = (HqlServiceClient) new org.springframework.context.support.ClassPathXmlApplicationContext(
            // "org/tools/hqlbuilder/applicationContext.xml").getBean("hqlServiceClient");
            return hqlServiceClient;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

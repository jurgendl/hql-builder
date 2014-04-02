package org.tools.hqlbuilder.client;

import static org.tools.hqlbuilder.common.CommonUtils.call;
import static org.tools.hqlbuilder.common.CommonUtils.create;

public class HqlServiceClientLoaderImpl implements HqlServiceClientLoader {
    @Override
    public HqlServiceClient getHqlServiceClient() {
        try {
            Object context = create("org.springframework.context.support.ClassPathXmlApplicationContext",
                    "org/tools/hqlbuilder/applicationContext.xml");
            HqlServiceClient hqlServiceClient = call(context, "getBean", HqlServiceClient.class, "hqlServiceClient");
            // HqlServiceClient hqlServiceClient = HqlServiceClient.class.cast(new org.springframework.context.support.ClassPathXmlApplicationContext(
            // "org/tools/hqlbuilder/applicationContext.xml").getBean("hqlServiceClient"));
            return hqlServiceClient;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

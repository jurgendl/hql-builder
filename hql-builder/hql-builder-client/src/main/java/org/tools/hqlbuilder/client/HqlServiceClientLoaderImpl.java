package org.tools.hqlbuilder.client;

import static org.tools.hqlbuilder.common.CommonUtils.create;

import org.tools.hqlbuilder.common.CommonUtilsAdd;

public class HqlServiceClientLoaderImpl implements HqlServiceClientLoader {
    @Override
    public HqlServiceClient getHqlServiceClient() {
        try {
            Object context = create("org.springframework.context.support.ClassPathXmlApplicationContext",
                    "org/tools/hqlbuilder/applicationContext.xml");
            HqlServiceClient hqlServiceClient = CommonUtilsAdd.call(context, "getBean", HqlServiceClient.class, "hqlServiceClient");
            // HqlServiceClient hqlServiceClient = HqlServiceClient.class.cast(new org.springframework.context.support.ClassPathXmlApplicationContext(
            // "org/tools/hqlbuilder/applicationContext.xml").getBean("hqlServiceClient"));
            return hqlServiceClient;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

package org.tools.hqlbuilder.test;

import org.tools.hqlbuilder.client.HqlBuilderFrame;
import org.tools.hqlbuilder.client.HqlServiceClient;

public class StartTestClient {
    public static void main(String[] args) {
        HqlServiceClient hqlServiceClient = (HqlServiceClient) new org.springframework.context.support.ClassPathXmlApplicationContext(
                "org/tools/hqlbuilder/test/applicationContext.xml").getBean("hqlServiceClient");
        if(hqlServiceClient.execute("from Pojo", Integer.MAX_VALUE).getResults().size()==0) {
        	hqlServiceClient.save(new Pojo());
        }
        HqlBuilderFrame.start(args, hqlServiceClient);
    }
}

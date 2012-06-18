package org.tools.hqlbuilder.test;

import org.tools.hqlbuilder.client.HqlBuilderFrame;
import org.tools.hqlbuilder.client.HqlServiceClient;
import org.tools.hqlbuilder.common.exceptions.ValidationException;

public class StartTestClient {
    public static void main(String[] args) {
        HqlServiceClient hqlServiceClient = (HqlServiceClient) new org.springframework.context.support.ClassPathXmlApplicationContext(
                "org/tools/hqlbuilder/test/applicationContext.xml").getBean("hqlServiceClient");
        if(hqlServiceClient.execute("from Pojo", Integer.MAX_VALUE).getResults().size()==0) {
        	try {
				hqlServiceClient.save(new Pojo());
			} catch (ValidationException e) {
				e.printStackTrace();
			}
        }
        HqlBuilderFrame.start(args, hqlServiceClient);
    }
}

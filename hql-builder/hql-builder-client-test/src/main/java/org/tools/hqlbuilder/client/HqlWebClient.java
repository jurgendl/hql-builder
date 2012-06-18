package org.tools.hqlbuilder.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tools.hqlbuilder.common.HqlWebService;

public class HqlWebClient {
    public static void main(final String[] arguments) {
        final ApplicationContext context = new ClassPathXmlApplicationContext("org/tools/hqlbuilder/client/spring-http-client-config.xml");
        final HqlWebService hqlWebServiceClient = (HqlWebService) context.getBean("hqlWebServiceClientProxy");
        System.out.println(hqlWebServiceClient.getConnectionInfo());
    }
}
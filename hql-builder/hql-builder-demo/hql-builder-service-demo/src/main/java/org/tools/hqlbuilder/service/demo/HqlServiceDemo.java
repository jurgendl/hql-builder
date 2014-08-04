package org.tools.hqlbuilder.service.demo;

import org.tools.hqlbuilder.common.HqlService;
import org.tools.hqlbuilder.demo.User;

public class HqlServiceDemo {
    public static void main(String[] args) {
        new HqlServiceDemo().test1();
    }

    public HqlService getHqlService() {
        @SuppressWarnings("resource")
        HqlService hqlService = HqlService.class.cast(new org.springframework.context.support.ClassPathXmlApplicationContext(
                "org/tools/hqlbuilder/service/demo/spring-service-demo-config.xml").getBean("hqlService"));
        return hqlService;
    }

    public void test1() {
        try {
            System.out.println(getHqlService().get(User.class, 1l));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

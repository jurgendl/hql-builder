package org.tools.hqlbuilder.test;

import org.tools.hqlbuilder.common.HqlService;

public class ServiceTst {
    public static void main(String[] args) {
        new ServiceTst().test1();
    }

    public HqlService getHqlService() {
        @SuppressWarnings("resource")
        HqlService hqlService = HqlService.class.cast(new org.springframework.context.support.ClassPathXmlApplicationContext(
                "org/tools/hqlbuilder/test/spring-service-config.xml").getBean("hqlService"));
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

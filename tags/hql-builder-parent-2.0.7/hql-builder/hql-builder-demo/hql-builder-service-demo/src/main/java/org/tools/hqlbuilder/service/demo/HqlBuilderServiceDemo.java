package org.tools.hqlbuilder.service.demo;

import java.util.Scanner;

import org.tools.hqlbuilder.common.HqlService;
import org.tools.hqlbuilder.demo.User;

public class HqlBuilderServiceDemo implements Runnable {
    public static void main(String[] args) {
        new Thread(new HqlBuilderServiceDemo()).start();
    }

    public HqlBuilderServiceDemo() {

    }

    public HqlService getHqlService() {
        @SuppressWarnings("resource")
        HqlService hqlService = HqlService.class.cast(new org.springframework.context.support.ClassPathXmlApplicationContext(
                "org/tools/hqlbuilder/common/spring-logback.xml", "org/tools/hqlbuilder/service/demo/spring-webservice-demo-context.xml")
                .getBean("hqlService"));
        return hqlService;
    }

    @Override
    public void run() {
        try {
            System.out.println("TEST? " + getHqlService().get(User.class, 1l));
            System.out.println("type exit to stop service");
            @SuppressWarnings("resource")
            Scanner scanner = new Scanner(System.in);
            while (!"exit".equals(scanner.next())) {
                Thread.sleep(5000l);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("exit");
            System.exit(0);
        }
    }
}

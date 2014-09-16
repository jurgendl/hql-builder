package org.tools.hqlbuilder.demo;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HqlBuilderStandaloneDemo extends org.tools.hqlbuilder.client.demo.HqlBuilderWebClientDemo {
    public static void main(String[] args) {
        try {
            new HqlBuilderStandaloneDemo(args);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    public HqlBuilderStandaloneDemo(String[] args) throws Exception {
        super(args);
    }

    @Override
    protected ConfigurableApplicationContext getContext() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext("org/tools/hqlbuilder/demo/spring-standalone-demo-context.xml");
        }
        return context;
    }
}

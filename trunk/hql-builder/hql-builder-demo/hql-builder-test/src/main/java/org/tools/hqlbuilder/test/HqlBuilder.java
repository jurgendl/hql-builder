package org.tools.hqlbuilder.test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HqlBuilder extends org.tools.hqlbuilder.client.HqlBuilder {
    public static void main(String[] args) {
        new HqlBuilder(args);
    }

    public HqlBuilder(String[] args) {
        super(args);
    }

    @Override
    protected ConfigurableApplicationContext getContext() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext("org/tools/hqlbuilder/test/applicationContext-4.xml");
        }
        return context;
    }
}

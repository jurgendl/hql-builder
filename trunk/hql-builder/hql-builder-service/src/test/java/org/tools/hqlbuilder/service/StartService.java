package org.tools.hqlbuilder.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartService {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("org/tools/hqlbuilder/service/applicationContext.xml");
    }
}

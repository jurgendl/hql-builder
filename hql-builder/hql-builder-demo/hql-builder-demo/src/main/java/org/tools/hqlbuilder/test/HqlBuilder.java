package org.tools.hqlbuilder.test;

import javax.swing.JOptionPane;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HqlBuilder extends org.tools.hqlbuilder.client.HqlBuilder {
    public static void main(String[] args) {
        try {
            new HqlBuilder(args);
        } catch (org.springframework.remoting.RemoteConnectFailureException ex) {
            ex.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "No connection to host");
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    public HqlBuilder(String[] args) throws Exception {
        super(args);
    }

    @Override
    protected ConfigurableApplicationContext getContext() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext("org/tools/hqlbuilder/test/spring-service4-test-context.xml");
        }
        return context;
    }
}

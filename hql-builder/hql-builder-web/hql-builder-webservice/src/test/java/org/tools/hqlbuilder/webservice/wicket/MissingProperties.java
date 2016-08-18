package org.tools.hqlbuilder.webservice.wicket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class MissingProperties {
    public static void main(String[] args) {
        try {
            Properties all = new Properties();
            all.load(MissingProperties.class.getClassLoader().getResourceAsStream("org/apache/wicket/Application.properties"));

            Properties local = new Properties();
            local.load(MissingProperties.class.getClassLoader().getResourceAsStream("org/apache/wicket/Application_nl.properties"));

            File f = new File("src/main/resources/" + WicketApplication.class.getPackage().getName().replace('.', '/') + "/"
                    + WicketApplication.class.getSimpleName() + "_nl.properties");
            f.getParentFile().mkdirs();

            Properties missing = new Properties();
            if (f.exists()) {
                missing.load(new FileInputStream(f));
                local.putAll(missing);
            }

            for (Object key : all.keySet()) {
                System.out.println(key + " = " + all.get(key) + "\n\t" + local.get(key));
                if (!local.containsKey(key)) {
                    missing.put(key, all.get(key));
                }
            }

            missing.store(new FileOutputStream(f), "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

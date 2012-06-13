package org.tools.hqlbuilder.client;

import java.util.HashSet;
import java.util.PropertyResourceBundle;
import java.util.Set;

import org.swingeasy.system.SystemSettings;

public class HqlResourceBundle {
    private static PropertyResourceBundle singleton;

    private static Set<String> missing = new HashSet<String>();

    private HqlResourceBundle() {
        //
    }

    public static PropertyResourceBundle getSingleton() {
        if (singleton == null) {
            singleton = (PropertyResourceBundle) PropertyResourceBundle.getBundle("HqlResourceBundle", SystemSettings.getCurrentLocale());
        }
        return singleton;
    }

    public static String getMessage(String key, Object... params) {
        return String.format(getMessage(key), params);
    }

    public static String getMessage(String key) {
        String key2 = key.replace(' ', '_').replace(':', '_').replace('&', '_').replace('/', '_');
        if (missing.contains(key2)) {
            return key;
        }
        try {
            String string = getSingleton().getString(key2);

            if (string.startsWith("${") && string.endsWith("}")) {
                string = string.substring(2, string.length() - 1);
                string = getMessage(string);
            }

            return Character.toUpperCase(string.charAt(0)) + string.substring(1);
        } catch (Exception ex) {
            System.out.println(key2 + "=" + key);
            missing.add(key);
            return key;
        }
    }
}

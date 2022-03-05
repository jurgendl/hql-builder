package org.tools.hqlbuilder.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 */
public class HqlResourceBundle implements PropertyChangeListener {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HqlResourceBundle.class);

    private static PropertyResourceBundle singleton;

    private static Set<String> missing = new HashSet<>();

    private HqlResourceBundle() {
		SystemSettings.propertyChangeParent.addPropertyChangeListener(SystemSettings.LOCALE, this);
    }

    public static PropertyResourceBundle getSingleton() {
        if (singleton == null) {
            loadResources();
        }
        return singleton;
    }

    private static void loadResources() {
        singleton = (PropertyResourceBundle) ResourceBundle.getBundle("HqlResourceBundle", SystemSettings.getCurrentLocale());
    }

    public static String getMessage(String key, Object... params) {
        return String.format(getMessage(key), params);
    }

    public static String getMessage(String key, boolean ucase, Object... params) {
        return String.format(getMessage(key), ucase, params);
    }

    public static String getMessage(String key) {
        return getMessage(key, true);
    }

    public static String getMessage(String key, boolean ucase) {
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

            if (ucase) {
                return Character.toUpperCase(string.charAt(0)) + string.substring(1);
            }

            return string;
        } catch (Exception ex) {
            logger.error("{}={}", key2, key);
            missing.add(key);
            return key;
        }
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        loadResources();
    }
}

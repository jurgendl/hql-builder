package org.tools.hqlbuilder.webservice.resources.datepicker;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.js.WicketJSRoot;

public class JQueryDatePicker {
    public static final String RESOURCE_I18N_PATH = "jquery/ui/datepicker/i18n/";

    public static JavaScriptResourceReference DEFAULT = new JavaScriptResourceReference(WicketRoot.class, RESOURCE_I18N_PATH + "datepicker-en-GB.js");

    public static final JavaScriptResourceReference DATEPICKER_JS = new JavaScriptResourceReference(WicketJSRoot.class, "JQDatePicker.js");

    public static final Map<Locale, JavaScriptResourceReference> cache = new HashMap<Locale, JavaScriptResourceReference>();

    public static JavaScriptResourceReference get(Locale locale) {
        String language = locale.getLanguage();
        JavaScriptResourceReference uiRef = new JavaScriptResourceReference(WicketRoot.class, RESOURCE_I18N_PATH + "datepicker-" + language + "-"
                + locale.getCountry().toUpperCase() + ".js");
        boolean found = true;
        try {
            if (uiRef.getResource().getResourceStream().getInputStream().available() <= 0) {
                found = false;
            }
        } catch (Exception ex) {
            found = false;
        }
        if (found) {
            return uiRef;
        }
        uiRef = new JavaScriptResourceReference(WicketRoot.class, RESOURCE_I18N_PATH + "datepicker-" + language + ".js");
        found = true;
        try {
            if (uiRef.getResource().getResourceStream().getInputStream().available() <= 0) {
                found = false;
            }
        } catch (Exception ex) {
            found = false;
        }
        if (found) {
            return uiRef;
        }
        return DEFAULT;
    }

    public static JavaScriptResourceReference cached(Locale locale) {
        if (cache.containsKey(locale)) {
            return cache.get(locale);
        }
        JavaScriptResourceReference resourceReference = get(locale);
        cache.put(locale, resourceReference);
        return resourceReference;
    }
}

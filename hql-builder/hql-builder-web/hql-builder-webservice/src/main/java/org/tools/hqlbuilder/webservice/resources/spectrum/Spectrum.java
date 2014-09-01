package org.tools.hqlbuilder.webservice.resources.spectrum;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.jquery.WicketJQueryRoot;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;

/**
 * @see http://bgrins.github.io/spectrum/#why
 */
public class Spectrum {
    public static final String VERSION = "1.5.0";

    public static final String PATH = "/ui/spectrum-" + VERSION + "/";

    public static final String RESOURCE_I18N_PATH = "i18n/";

    public static JavaScriptResourceReference SPECTRUM_JS = new JavaScriptResourceReference(WicketJQueryRoot.class, PATH + "spectrum.js");

    public static CssResourceReference SPECTRUM_CSS = new CssResourceReference(WicketJQueryRoot.class, PATH + "spectrum.css");

    private static final Map<Locale, JavaScriptResourceReference> cache = new HashMap<Locale, JavaScriptResourceReference>();

    static {
        try {
            SPECTRUM_CSS.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference())
            .addJavaScriptResourceReferenceDependency(
                    ((IJQueryLibrarySettings) WicketApplication.get().getJavaScriptLibrarySettings()).getJQueryUIReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static JavaScriptResourceReference get(Locale locale) {
        String language = locale.getLanguage();
        JavaScriptResourceReference uiRef = new JavaScriptResourceReference(WicketRoot.class, PATH + RESOURCE_I18N_PATH + "jquery.spectrum-"
                + language + "-" + locale.getCountry().toUpperCase() + ".js");
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
        return null;
    }

    /**
     * can return null
     */
    public static JavaScriptResourceReference i18n(Locale locale) {
        if (cache.containsKey(locale)) {
            return cache.get(locale);
        }
        JavaScriptResourceReference resourceReference = get(locale);
        cache.put(locale, resourceReference);
        return resourceReference;
    }
}

package org.tools.hqlbuilder.webservice.jquery;

import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;

/**
 * @see http://bgrins.github.io/spectrum/#why
 */
public class Spectrum {
    public static JavaScriptResourceReference SPECTRUM_JS = new JavaScriptResourceReference(WicketRoot.class, "jquery/ui/spectrum/spectrum.js");

    public static CssResourceReference SPECTRUM_CSS = new CssResourceReference(WicketRoot.class, "jquery/ui/spectrum/spectrum.css");

    static {
        try {
            SPECTRUM_JS.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());
            SPECTRUM_JS.addJavaScriptResourceReferenceDependency(((IJQueryLibrarySettings) WicketApplication.get().getJavaScriptLibrarySettings())
                    .getJQueryUIReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

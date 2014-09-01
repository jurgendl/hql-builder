package org.tools.hqlbuilder.webservice.resources.spectrum;

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

    public static JavaScriptResourceReference SPECTRUM_JS = new JavaScriptResourceReference(WicketJQueryRoot.class, PATH + "spectrum.js");

    public static CssResourceReference SPECTRUM_CSS = new CssResourceReference(WicketJQueryRoot.class, PATH + "spectrum.css");

    static {
        try {
            SPECTRUM_CSS.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference())
                    .addJavaScriptResourceReferenceDependency(
                            ((IJQueryLibrarySettings) WicketApplication.get().getJavaScriptLibrarySettings()).getJQueryUIReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

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
    public static JavaScriptResourceReference SPECTRUM_JS = new JavaScriptResourceReference(WicketJQueryRoot.class, "ui/spectrum-1.3.4/spectrum.js");

    public static CssResourceReference SPECTRUM_CSS = new CssResourceReference(WicketJQueryRoot.class, "ui/spectrum-1.3.4/spectrum.css")
    .addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference())
    .addJavaScriptResourceReferenceDependency(
            ((IJQueryLibrarySettings) WicketApplication.get().getJavaScriptLibrarySettings()).getJQueryUIReference());
}

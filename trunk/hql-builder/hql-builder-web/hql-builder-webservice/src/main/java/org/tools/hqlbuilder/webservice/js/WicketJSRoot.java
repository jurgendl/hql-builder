package org.tools.hqlbuilder.webservice.js;

import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;

public class WicketJSRoot {
    public static JavaScriptResourceReference FLOATING_BAR = new JavaScriptResourceReference(WicketJSRoot.class, "floatingbar.js")
            .addJavaScriptResourceReferenceDependency(((IJQueryLibrarySettings) WicketApplication.get().getJavaScriptLibrarySettings())
            .getJQueryUIReference());
}

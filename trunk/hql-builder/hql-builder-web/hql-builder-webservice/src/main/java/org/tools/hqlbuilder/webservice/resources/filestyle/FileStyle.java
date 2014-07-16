package org.tools.hqlbuilder.webservice.resources.filestyle;

import org.tools.hqlbuilder.webservice.jquery.WicketJQueryRoot;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * requires twitter bootstrap
 */
public class FileStyle {
    public static JavaScriptResourceReference FILESTYLE_JS = new JavaScriptResourceReference(WicketJQueryRoot.class,
            "ui/filestyle/bootstrap-filestyle.js").addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings()
            .getJQueryReference());
}

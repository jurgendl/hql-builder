package org.tools.hqlbuilder.webservice.jquery;

import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

public class FileStyle {
    public static JavaScriptResourceReference FILESTYLE_JS = new JavaScriptResourceReference(WicketRoot.class,
            "jquery/ui/filestyle/bootstrap-filestyle.js");

    static {
        try {
            FILESTYLE_JS.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

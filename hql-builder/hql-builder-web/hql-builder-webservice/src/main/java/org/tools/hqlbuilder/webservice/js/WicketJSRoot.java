package org.tools.hqlbuilder.webservice.js;

import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

public class WicketJSRoot {
    public static JavaScriptResourceReference COLORS = new JavaScriptResourceReference(WicketJSRoot.class, "colors.js");

    public static JavaScriptResourceReference FLOATING_BAR = new JavaScriptResourceReference(WicketJSRoot.class, "floatingbar.js");

    public static JavaScriptResourceReference VELOCITY = new JavaScriptResourceReference(WicketRoot.class, "jquery/ui/velocity/jquery.velocity.js");

    static {
        try {
            VELOCITY.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());
            FLOATING_BAR.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

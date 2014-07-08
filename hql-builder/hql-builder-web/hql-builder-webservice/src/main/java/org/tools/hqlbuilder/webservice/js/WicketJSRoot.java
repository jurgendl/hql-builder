package org.tools.hqlbuilder.webservice.js;

import org.apache.wicket.request.resource.ResourceReference;
import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;

public class WicketJSRoot {
    public static JavaScriptResourceReference COLORS = new JavaScriptResourceReference(WicketJSRoot.class, "colors.js");

    public static JavaScriptResourceReference FLOATING_BAR = new JavaScriptResourceReference(WicketJSRoot.class, "floatingbar.js");

    public static JavaScriptResourceReference VELOCITY = new JavaScriptResourceReference(WicketRoot.class, "jquery/ui/velocity/jquery.velocity.js");

    static {
        try {
            ResourceReference jQueryUIReference = ((IJQueryLibrarySettings) WicketApplication.get().getJavaScriptLibrarySettings())
                    .getJQueryUIReference();
            VELOCITY.addJavaScriptResourceReferenceDependency(jQueryUIReference);
            FLOATING_BAR.addJavaScriptResourceReferenceDependency(jQueryUIReference);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

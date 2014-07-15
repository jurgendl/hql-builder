package org.tools.hqlbuilder.webservice.resources.velocity;

import org.apache.wicket.request.resource.ResourceReference;
import org.tools.hqlbuilder.webservice.jquery.WicketJQueryRoot;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;

public class Velocity {
    public static JavaScriptResourceReference VELOCITY_JS = new JavaScriptResourceReference(WicketJQueryRoot.class, "ui/velocity/jquery.velocity.js");

    static {
        try {
            ResourceReference jQueryUIReference = ((IJQueryLibrarySettings) WicketApplication.get().getJavaScriptLibrarySettings())
                    .getJQueryUIReference();
            VELOCITY_JS.addJavaScriptResourceReferenceDependency(jQueryUIReference);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

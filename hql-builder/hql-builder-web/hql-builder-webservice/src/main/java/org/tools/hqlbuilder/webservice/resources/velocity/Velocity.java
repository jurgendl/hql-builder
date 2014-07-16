package org.tools.hqlbuilder.webservice.resources.velocity;

import org.tools.hqlbuilder.webservice.jquery.WicketJQueryRoot;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;

public class Velocity {
    public static JavaScriptResourceReference VELOCITY_JS = new JavaScriptResourceReference(WicketJQueryRoot.class, "ui/velocity/jquery.velocity.js")
            .addJavaScriptResourceReferenceDependency(((IJQueryLibrarySettings) WicketApplication.get().getJavaScriptLibrarySettings())
            .getJQueryUIReference());
}

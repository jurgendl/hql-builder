package org.tools.hqlbuilder.webservice.jquery.ui.velocity;

import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;

/**
 * Velocity animation framework version 0.11.9
 *
 * @see http://css-tricks.com/improving-ui-animation-workflow-velocity-js/
 * @see http://julian.com/research/velocity/
 */
public class Velocity {
    public static JavaScriptResourceReference VELOCITY_JS = new JavaScriptResourceReference(Velocity.class, "jquery.velocity.js")
            .addJavaScriptResourceReferenceDependency(((IJQueryLibrarySettings) WicketApplication.get().getJavaScriptLibrarySettings())
                    .getJQueryUIReference());
}

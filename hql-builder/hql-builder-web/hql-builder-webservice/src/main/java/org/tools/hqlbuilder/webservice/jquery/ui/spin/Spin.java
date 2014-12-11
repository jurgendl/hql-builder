package org.tools.hqlbuilder.webservice.jquery.ui.spin;

import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * add <span id="spinner"/> to your page and give it a place and width/height, use spinner.spin() to start and spinner.stop() to stop in javascript
 *
 * @see http
 *      ://fgnass.github.io/spin.js/#?lines=13&length=14&width=5&radius=19&corners=1.0&rotate=0&trail=60&speed=1.0&direction=1&shadow=on&hwaccel=on
 */
public class Spin {
    public static JavaScriptResourceReference SPIN_JS = new JavaScriptResourceReference(Spin.class, "spin.js");

    public static JavaScriptResourceReference SPIN_JQUERY_JS = new JavaScriptResourceReference(Spin.class, "jquery.spin.js")
            .addJavaScriptResourceReferenceDependency(SPIN_JS).addJavaScriptResourceReferenceDependency(
                    WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

    public static JavaScriptResourceReference SPIN_FACTORY_JS = new JavaScriptResourceReference(Spin.class, "spin.factory.js")
            .addJavaScriptResourceReferenceDependency(SPIN_JQUERY_JS);
}

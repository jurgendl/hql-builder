package org.tools.hqlbuilder.webservice.jquery.ui.primeui;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.VirtualPackageResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;

/**
 * @see http://www.primefaces.org/primeui/demo.html
 */
public class PrimeUI {
    public static JavaScriptResourceReference PRIME_UI_JS = new JavaScriptResourceReference(PrimeUI.class, "primeui-1.1.js")
            .addJavaScriptResourceReferenceDependency(((IJQueryLibrarySettings) WicketApplication.get().getJavaScriptLibrarySettings())
                    .getJQueryUIReference());

    public static CssResourceReference PRIME_UI_CSS = new CssResourceReference(PrimeUI.class, "primeui-1.1.css");

    public static JavaScriptResourceReference PRIME_UI_PLUGIN_CURSOR_POSITION_JS = new JavaScriptResourceReference(PrimeUI.class,
            "plugins/cursorposition.js").addJavaScriptResourceReferenceDependency(PRIME_UI_JS);

    public static JavaScriptResourceReference PRIME_UI_PLUGIN_RANGY_INPUT_JS = new JavaScriptResourceReference(PrimeUI.class, "plugins/rangyinput.js")
    .addJavaScriptResourceReferenceDependency(PRIME_UI_JS);

    public static CssResourceReference forJQueryUITheme(String name) {
        return new CssResourceReference(PrimeUI.class, "themes/" + name + "/theme.css");
    }

    public static void mountImages(WebApplication webApplication) {
        String[] images = { "password-meter.png", "messages.png", "loading.gif" };
        for (String image : images) {
            String relPath = "images/" + image;
            PackageResourceReference reference = new VirtualPackageResourceReference(PrimeUI.class, relPath, PrimeUI.class, relPath);
            webApplication.getSharedResources().add(relPath, reference.getResource());
            webApplication.mountResource(relPath, reference);
        }
    }
}

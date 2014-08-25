package org.tools.hqlbuilder.webservice.resources.prime;

import org.tools.hqlbuilder.webservice.jquery.WicketJQueryRoot;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * @see http://www.primefaces.org/primeui/demo.html
 */
public class PrimeUI {
    protected static final String PATH = "ui/primeui-1.1/";

    public static CssResourceReference PRIME_UI_JS = new CssResourceReference(WicketJQueryRoot.class, PATH + "primeui-1.1.js");

    public static JavaScriptResourceReference PRIME_UI_CSS = new JavaScriptResourceReference(WicketJQueryRoot.class, PATH + "primeui-1.1.css");

    public static JavaScriptResourceReference PRIME_UI_PLUGIN_CURSOR_POSITION_JS = new JavaScriptResourceReference(WicketJQueryRoot.class, PATH
            + "plugins/cursorposition.js").addJavaScriptResourceReferenceDependency(PRIME_UI_JS);

    public static JavaScriptResourceReference PRIME_UI_PLUGIN_RANGY_INPUT_JS = new JavaScriptResourceReference(WicketJQueryRoot.class, PATH
            + "plugins/rangyinput.js").addJavaScriptResourceReferenceDependency(PRIME_UI_JS);

    public static CssResourceReference forJQueryUITheme(String name) {
        return new CssResourceReference(WicketJQueryRoot.class, PATH + "themes/" + name + "/theme.css");
    }
}

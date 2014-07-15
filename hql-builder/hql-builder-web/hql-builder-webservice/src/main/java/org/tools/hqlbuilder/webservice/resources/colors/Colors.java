package org.tools.hqlbuilder.webservice.resources.colors;

import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;
import org.tools.hqlbuilder.webservice.js.WicketJSRoot;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

public class Colors {
    public static JavaScriptResourceReference COLORS_JS = new JavaScriptResourceReference(WicketJSRoot.class, "colors.js");

    public static CssResourceReference COLORS_CSS = new CssResourceReference(WicketCSSRoot.class, "colors.css");
}

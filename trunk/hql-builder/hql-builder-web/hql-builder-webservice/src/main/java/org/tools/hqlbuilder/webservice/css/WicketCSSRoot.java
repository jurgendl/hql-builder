package org.tools.hqlbuilder.webservice.css;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;

public class WicketCSSRoot {
    public static CssResourceReference RESET = new CssResourceReference(WicketCSSRoot.class, "meyer.reset.css");

    public static CssResourceReference NORMALIZE = new CssResourceReference(WicketCSSRoot.class, "normalize.css");

    public static CssResourceReference GENERAL = new CssResourceReference(WicketCSSRoot.class, "general.css");
}

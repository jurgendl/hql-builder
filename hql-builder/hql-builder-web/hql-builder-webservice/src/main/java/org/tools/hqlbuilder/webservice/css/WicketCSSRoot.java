package org.tools.hqlbuilder.webservice.css;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;

public class WicketCSSRoot {
    public static CssResourceReference RESET = new CssResourceReference(WicketCSSRoot.class, "meyer.reset.css");

    public static CssResourceReference NORMALIZE = new CssResourceReference(WicketCSSRoot.class, "normalize.css");

    public static CssResourceReference GENERAL = new CssResourceReference(WicketCSSRoot.class, "general.css");

    public static CssResourceReference HORIZONTAL_MENU = new CssResourceReference(WicketCSSRoot.class, "horizontalmenu.css");

    public static CssResourceReference FORMS = new CssResourceReference(WicketCSSRoot.class, "form.css");

    public static CssResourceReference TABLES = new CssResourceReference(WicketCSSRoot.class, "table.css");
}

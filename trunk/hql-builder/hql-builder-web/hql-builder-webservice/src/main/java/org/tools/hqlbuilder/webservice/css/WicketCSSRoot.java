package org.tools.hqlbuilder.webservice.css;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;

public class WicketCSSRoot {
    public static CssResourceReference COLORS = new CssResourceReference(WicketCSSRoot.class, "colors.css");

    public static CssResourceReference WE_LOVE_ICONS = new CssResourceReference(WicketCSSRoot.class, "weloveiconfonts.css");

    public static CssResourceReference GENERAL = new CssResourceReference(WicketCSSRoot.class, "general.css");

    public static CssResourceReference HORIZONTAL_MENU = new CssResourceReference(WicketCSSRoot.class, "horizontalmenu.css");

    public static CssResourceReference FORMS = new CssResourceReference(WicketCSSRoot.class, "form.css");

    public static CssResourceReference TABLES = new CssResourceReference(WicketCSSRoot.class, "table.css");
}

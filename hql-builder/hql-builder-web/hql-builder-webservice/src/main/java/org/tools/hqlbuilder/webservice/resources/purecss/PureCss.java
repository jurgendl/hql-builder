package org.tools.hqlbuilder.webservice.resources.purecss;

import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;

/**
 * PURE CSS version 0.5.0
 *
 * @see http://purecss.io
 */
public class PureCss {
    /** for alternatives: http://purecss.io/customize/ */
    private static final String CDN = "http://yui.yahooapis.com/pure/0.5.0/";

    private static final String PATH = "purecss/";

    private static final String FULL_CSS = "pure.css";

    private static final String TABLES_CSS = "tables.css";

    private static final String MENUS_CSS = "menus.css";

    private static final String GRIDS_CSS = "grids.css";

    private static final String FORMS_CSS = "forms.css";

    private static final String BUTTONS_CSS = "buttons.css";

    private static final String BASE_CSS = "base.css";

    private static final String GRIDS_RESPONSIVE_CSS = "grids-responsive.css";

    public static CssResourceReference BASE = new CssResourceReference(WicketCSSRoot.class, PATH + BASE_CSS);

    public static UrlResourceReference CDN_BASE = new UrlResourceReference(Url.parse(CDN + BASE_CSS));

    public static CssResourceReference BUTTONS = new CssResourceReference(WicketCSSRoot.class, PATH + BUTTONS_CSS);

    public static UrlResourceReference CDN_BUTTONS = new UrlResourceReference(Url.parse(CDN + BUTTONS_CSS));

    public static CssResourceReference FORMS = new CssResourceReference(WicketCSSRoot.class, PATH + FORMS_CSS);

    public static UrlResourceReference CDN_FORMS = new UrlResourceReference(Url.parse(CDN + FORMS_CSS));

    public static CssResourceReference GRIDS = new CssResourceReference(WicketCSSRoot.class, PATH + GRIDS_CSS);

    public static UrlResourceReference CDN_GRIDS = new UrlResourceReference(Url.parse(CDN + GRIDS_CSS));

    public static CssResourceReference GRIDS_RESPONSIVE = new CssResourceReference(WicketCSSRoot.class, PATH + GRIDS_RESPONSIVE_CSS);

    public static UrlResourceReference CDN_GRIDS_RESPONSIVE = new UrlResourceReference(Url.parse(CDN + GRIDS_RESPONSIVE_CSS));

    public static CssResourceReference MENUS = new CssResourceReference(WicketCSSRoot.class, PATH + MENUS_CSS);

    public static UrlResourceReference CDN_MENUS = new UrlResourceReference(Url.parse(CDN + MENUS_CSS));

    public static CssResourceReference TABLES = new CssResourceReference(WicketCSSRoot.class, PATH + TABLES_CSS);

    public static UrlResourceReference CDN_TABLES = new UrlResourceReference(Url.parse(CDN + TABLES_CSS));

    public static CssResourceReference FULL = new CssResourceReference(WicketCSSRoot.class, PATH + FULL_CSS);

    public static UrlResourceReference CDN_FULL = new UrlResourceReference(Url.parse(CDN + FULL_CSS));
}

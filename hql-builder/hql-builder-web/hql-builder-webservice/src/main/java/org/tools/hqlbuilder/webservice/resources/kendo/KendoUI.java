package org.tools.hqlbuilder.webservice.resources.kendo;

import org.tools.hqlbuilder.webservice.jquery.WicketJQueryRoot;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;

/**
 * only includes core
 *
 * @see http://www.telerik.com/download/kendo-ui-core
 * @see http://www.7thweb.net/wicket-jquery-ui/
 * @see http://www.telerik.com/kendo-ui
 */
public class KendoUI {
    public static CssResourceReference KENDO_COMMON_CSS = new CssResourceReference(WicketJQueryRoot.class, "ui/kendo-2014.2.716/kendo.common.css");

    public static CssResourceReference KENDO_DEFAULT_CSS = new CssResourceReference(WicketJQueryRoot.class, "ui/kendo-2014.2.716/kendo.default.css");
}

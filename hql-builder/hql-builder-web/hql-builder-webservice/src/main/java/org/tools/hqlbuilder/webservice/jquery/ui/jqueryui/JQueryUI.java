package org.tools.hqlbuilder.webservice.jquery.ui.jqueryui;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * version 1.11.1
 */
public class JQueryUI {
    public static JavaScriptResourceReference JQUERY_UI_JS = new JavaScriptResourceReference(JQueryUI.class, "jquery-ui-1.11.1.js")
    .addJavaScriptResourceReferenceDependency(JQuery.JQUERY_JS);
}

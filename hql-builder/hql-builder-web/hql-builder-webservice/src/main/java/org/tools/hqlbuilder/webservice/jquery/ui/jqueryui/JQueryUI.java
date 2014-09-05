package org.tools.hqlbuilder.webservice.jquery.ui.jqueryui;

import org.apache.wicket.request.resource.ResourceReference;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;

/**
 * version 1.11.1
 */
public class JQueryUI {
    public static ResourceReference getJQueryUIReference() {
        return ((IJQueryLibrarySettings) WicketApplication.get().getJavaScriptLibrarySettings()).getJQueryUIReference();
    }

    /**
     * do not include directly: use "getJQueryUIReference()"
     */
    public static JavaScriptResourceReference JQUERY_UI_JS = new JavaScriptResourceReference(JQueryUI.class, "jquery-ui.js");

    public static JavaScriptResourceReference JQUERY_UI_FACTORY_JS = new JavaScriptResourceReference(JQueryUI.class, "jquery-ui-factory.js");

    public static JavaScriptResourceReference JQUERY_UI_STRUCTURE_JS = new JavaScriptResourceReference(JQueryUI.class, "jquery-ui.structure.css");

    public static CssResourceReference JQUERY_UI_CSS = new CssResourceReference(JQueryUI.class, "jquery-ui.css");

    public static CssResourceReference JQUERY_UI_THEME_CSS = new CssResourceReference(JQueryUI.class, "jquery-ui.theme.css");

    static {
        try {
            JQUERY_UI_JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
            JQUERY_UI_FACTORY_JS.addJavaScriptResourceReferenceDependency(getJQueryUIReference());
            JQUERY_UI_STRUCTURE_JS.addJavaScriptResourceReferenceDependency(getJQueryUIReference());
            JQUERY_UI_THEME_CSS.addCssResourceReferenceDependency(JQUERY_UI_CSS);
        } catch (Exception ex) {
            //
        }
    }

    public static final String jquiaccordion = "jquiaccordion";

    public static final String jquiautocomplete = "jquiautocomplete";

    public static final String jquibutton = "jquibutton";

    public static final String jquibuttonset = "jquibuttonset";

    public static final String jquidatepicker = "jquidatepicker";

    public static final String jquidialog = "jquidialog";

    public static final String jquimenu = "jquimenu";

    public static final String jquiprogressbar = "jquiprogressbar";

    public static final String jquiselectmenu = "jquiselectmenu";

    public static final String jquislider = "jquislider";

    public static final String jquispinner = "jquispinner";

    public static final String jquitabs = "jquitabs";
}

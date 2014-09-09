package org.tools.hqlbuilder.webservice.jquery.ui.jquery;

import org.apache.wicket.request.resource.ResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * version 1.11.1
 */
public class JQuery {
    public static ResourceReference getJQueryReference() {
        return WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference();
    }

    /**
     * fix errors like "$.browser.msie"
     *
     * @see https://github.com/jquery/jquery-migrate/#readme
     */
    public static JavaScriptResourceReference JQUERY_MIGRATE_JS = new JavaScriptResourceReference(JQuery.class, "jquery-migrate-1.2.1.js");

    static {
        try {
            JQUERY_MIGRATE_JS.addJavaScriptResourceReferenceDependency(getJQueryReference());
        } catch (Exception ex) {
            //
        }
    }
}

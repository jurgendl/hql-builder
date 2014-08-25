package org.tools.hqlbuilder.webservice.resources.filestyle;

import org.tools.hqlbuilder.webservice.jquery.WicketJQueryRoot;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * requires twitter bootstrap
 *
 * @see http://dev.tudosobreweb.com.br/bootstrap-filestyle/
 */
public class FileStyle {
    public static JavaScriptResourceReference FILESTYLE_JS = new JavaScriptResourceReference(WicketJQueryRoot.class,
            "ui/filestyle-1.1.0/bootstrap-filestyle.js").addJavaScriptResourceReferenceDependency(WicketApplication.get()
            .getJavaScriptLibrarySettings().getJQueryReference());
}

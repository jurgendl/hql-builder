package org.tools.hqlbuilder.webservice.jquery.ui.tablesorter;

import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * @see http://tablesorter.com/docs/#Download
 */
public class TableSorter {
    public static JavaScriptResourceReference TABLE_SORTER_JS = new JavaScriptResourceReference(TableSorter.class, "jquery.tablesorter.js")
            .addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());
}

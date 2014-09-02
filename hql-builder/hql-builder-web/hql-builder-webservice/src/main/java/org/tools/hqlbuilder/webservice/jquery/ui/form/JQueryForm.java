package org.tools.hqlbuilder.webservice.jquery.ui.form;

import java.util.Arrays;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * used version 3.51.0-2014.06.20
 *
 * @see http://malsup.com/jquery/form/
 */
public class JQueryForm {
    public static final String URL = "http://cdnjs.cloudflare.com/ajax/libs/jquery.form/3.50/";

    public static final String PATH_JQUERY_FORM_JS = "jquery.form.js";

    public static JavaScriptResourceReference FORM_JS = new JavaScriptResourceReference(JQueryForm.class, PATH_JQUERY_FORM_JS)
            .addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

    public static UrlResourceReference CDN_FORM_JS = new UrlResourceReference(Url.parse(URL + PATH_JQUERY_FORM_JS)) {
        private static final long serialVersionUID = -3223394738670776800L;

        @Override
        public Iterable<? extends HeaderItem> getDependencies() {
            return Arrays.asList(JavaScriptHeaderItem.forReference(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference()));
        }
    };
}

package org.tools.hqlbuilder.webservice.resources.validation;

import java.util.Arrays;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.tools.hqlbuilder.webservice.jquery.WicketJQueryRoot;
import org.tools.hqlbuilder.webservice.resources.form.JQueryForm;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * used version 1.13.0
 *
 * @see http://jqueryvalidation.org/
 */
public class JQueryValidation {
    public static final String PATH = "ui/jquery-validation-1.13.0/";

    /** http://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.0 */
    public static final String URL = "http://ajax.aspnetcdn.com/ajax/jquery.validate/1.13.0/";

    public static JavaScriptResourceReference VALIDATION_JS = new JavaScriptResourceReference(WicketJQueryRoot.class, PATH + "jquery.validate.js");

    public static JavaScriptResourceReference VALIDATION_ADDITIONAL_JS = new JavaScriptResourceReference(WicketJQueryRoot.class, PATH
            + "additional-methods.js");

    public static JavaScriptResourceReference VALIDATION_LOCALIZATION_JS = new JavaScriptResourceReference(WicketJQueryRoot.class, PATH
            + "localization/messages.js");

    public static UrlResourceReference CDN_VALIDATION_JS = new UrlResourceReference(Url.parse(URL + "jquery.validate.js")) {
        private static final long serialVersionUID = -3223394738670776800L;

        @Override
        public Iterable<? extends HeaderItem> getDependencies() {
            return Arrays.asList(JavaScriptHeaderItem.forReference(JQueryForm.CDN_FORM_JS));
        }
    };

    public static UrlResourceReference CDN_VALIDATION_ADDITIONAL_JS = new UrlResourceReference(Url.parse(URL + "additional-methods.js")) {
        private static final long serialVersionUID = -4452640036671378227L;

        @Override
        public Iterable<? extends HeaderItem> getDependencies() {
            return Arrays.asList(JavaScriptHeaderItem.forReference(CDN_VALIDATION_JS));
        }
    };

    public static UrlResourceReference CDN_VALIDATION_LOCALIZATION_JS = new UrlResourceReference(Url.parse(URL + "localization/messages.js")) {
        private static final long serialVersionUID = 3397791923898457501L;

        @Override
        public Iterable<? extends HeaderItem> getDependencies() {
            return Arrays.asList(JavaScriptHeaderItem.forReference(CDN_VALIDATION_JS));
        }
    };

    static {
        try {
            VALIDATION_JS.addJavaScriptResourceReferenceDependency(JQueryForm.FORM_JS);
            VALIDATION_ADDITIONAL_JS.addJavaScriptResourceReferenceDependency(VALIDATION_JS);
            VALIDATION_LOCALIZATION_JS.addJavaScriptResourceReferenceDependency(VALIDATION_JS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

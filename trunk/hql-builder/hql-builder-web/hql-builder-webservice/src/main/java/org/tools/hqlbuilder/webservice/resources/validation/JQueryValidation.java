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

    public static final String PATH_JQUERY_VALIDATE_JS = "jquery.validate.js";

    public static final String PATH_ADDITIONAL_METHODS_JS = "additional-methods.js";

    public static final String PATH_LOCALIZATION_MESSAGES_JS = "localization/messages.js";

    public static final String PATH_LOCALIZATION_METHODS_JS = "localization/methods.js";

    public static JavaScriptResourceReference VALIDATION_JS = new JavaScriptResourceReference(WicketJQueryRoot.class, PATH + PATH_JQUERY_VALIDATE_JS)
            .addJavaScriptResourceReferenceDependency(JQueryForm.FORM_JS);

    public static JavaScriptResourceReference VALIDATION_ADDITIONAL_JS = new JavaScriptResourceReference(WicketJQueryRoot.class, PATH
            + PATH_ADDITIONAL_METHODS_JS).addJavaScriptResourceReferenceDependency(VALIDATION_JS);

    public static JavaScriptResourceReference VALIDATION_LOCALIZATION_JS = new JavaScriptResourceReference(WicketJQueryRoot.class, PATH
            + PATH_LOCALIZATION_MESSAGES_JS).addJavaScriptResourceReferenceDependency(VALIDATION_JS);

    public static JavaScriptResourceReference VALIDATION_LOCALIZATION_METHODS_JS = new JavaScriptResourceReference(WicketJQueryRoot.class, PATH
            + PATH_LOCALIZATION_METHODS_JS).addJavaScriptResourceReferenceDependency(VALIDATION_LOCALIZATION_JS);

    public static UrlResourceReference CDN_VALIDATION_JS = new UrlResourceReference(Url.parse(URL + PATH_JQUERY_VALIDATE_JS)) {
        private static final long serialVersionUID = -3223394738670776800L;

        @Override
        public Iterable<? extends HeaderItem> getDependencies() {
            return Arrays.asList(JavaScriptHeaderItem.forReference(JQueryForm.CDN_FORM_JS));
        }
    };

    public static UrlResourceReference CDN_VALIDATION_ADDITIONAL_JS = new UrlResourceReference(Url.parse(URL + PATH_ADDITIONAL_METHODS_JS)) {
        private static final long serialVersionUID = -4452640036671378227L;

        @Override
        public Iterable<? extends HeaderItem> getDependencies() {
            return Arrays.asList(JavaScriptHeaderItem.forReference(CDN_VALIDATION_JS));
        }
    };

    public static UrlResourceReference CDN_VALIDATION_LOCALIZATION_JS = new UrlResourceReference(Url.parse(URL + PATH_LOCALIZATION_MESSAGES_JS)) {
        private static final long serialVersionUID = 3397791923898457501L;

        @Override
        public Iterable<? extends HeaderItem> getDependencies() {
            return Arrays.asList(JavaScriptHeaderItem.forReference(CDN_VALIDATION_JS));
        }
    };

    public static UrlResourceReference CDN_VALIDATION_LOCALIZATION_METHODS_JS = new UrlResourceReference(
            Url.parse(URL + PATH_LOCALIZATION_METHODS_JS)) {
        private static final long serialVersionUID = 7126346418465627172L;

        @Override
        public Iterable<? extends HeaderItem> getDependencies() {
            return Arrays.asList(JavaScriptHeaderItem.forReference(CDN_VALIDATION_LOCALIZATION_JS));
        }
    };
}

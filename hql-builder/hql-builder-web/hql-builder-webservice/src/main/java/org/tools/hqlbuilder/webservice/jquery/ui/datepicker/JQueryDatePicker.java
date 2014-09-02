package org.tools.hqlbuilder.webservice.jquery.ui.datepicker;

import java.util.Locale;

import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.common.icons.WicketIconsResources;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

public class JQueryDatePicker {
    public static final String RESOURCE_I18N_PATH = "i18n/";

    public static JavaScriptResourceReference DATEPICKER_JS = new JavaScriptResourceReference(JQueryDatePicker.class, "JQDatePicker.js");

    public static String initJavaScript(com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker datepicker, String dateFormatClient) {
        return ";initJQDatepicker('" + datepicker.getMarkupId() + "', '" + datepicker.getLocale().getCountry() + "', '" + dateFormatClient + "', "
                + "'" + datepicker.urlFor(WicketIconsResources.REF_CALENDER, new PageParameters()) + "');";
    }

    public static JavaScriptResourceReference i18n(Locale locale) {
        return new JavaScriptResourceReference(JQueryDatePicker.class, RESOURCE_I18N_PATH + "datepicker.js", locale, null, null)
                .addJavaScriptResourceReferenceDependency(DATEPICKER_JS);
    }

    /**
     * @see http://stackoverflow.com/questions/13824355/how-to-change-jquery-ui-datepicker-z-index-value
     */
    public static CssContentHeaderItem getCssFix() {
        return CssHeaderItem.forCSS("#ui-datepicker-div {z-index: 9999 !important;};", "datepicker_z_index_fix");
    }
}

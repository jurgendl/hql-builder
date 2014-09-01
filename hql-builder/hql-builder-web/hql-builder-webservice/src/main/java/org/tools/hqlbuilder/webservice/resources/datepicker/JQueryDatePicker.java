package org.tools.hqlbuilder.webservice.resources.datepicker;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.common.icons.WicketIconsResources;
import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.js.WicketJSRoot;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;

public class JQueryDatePicker {
    public static final String RESOURCE_I18N_PATH = "jquery/ui/datepicker/i18n/";

    public static JavaScriptResourceReference DATEPICKER_JS = new JavaScriptResourceReference(WicketJSRoot.class, "JQDatePicker.js");

    public static JavaScriptResourceReference DATEPICKER_I18N_JS = new JavaScriptResourceReference(WicketRoot.class, RESOURCE_I18N_PATH
            + "datepicker.js").addJavaScriptResourceReferenceDependency(DATEPICKER_JS);

    public static String initJavaScript(DatePicker datepicker, String dateFormatClient) {
        return ";initJQDatepicker('" + datepicker.getMarkupId() + "', '" + datepicker.getLocale().getCountry() + "', '" + dateFormatClient + "', "
                + "'" + datepicker.urlFor(WicketIconsResources.REF_CALENDER, new PageParameters()) + "');";
    }
}

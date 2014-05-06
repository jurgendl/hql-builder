package org.tools.hqlbuilder.webservice.wicket.forms;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.core.utils.LocaleUtils;
import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;

public class DatePickerPanel extends FormRowPanel<Date, DatePicker, Options> {
    private static final long serialVersionUID = -5807168584242557542L;

    public static final String DATE_FORMAT = "dateFormat";

    public DatePickerPanel(IModel<?> model, String property, boolean required, Locale locale) {
        this(model, property, required, LocaleUtils.getLocaleDatePattern(locale, DateFormat.SHORT));
    }

    public DatePickerPanel(IModel<?> model, String property, boolean required, String pattern) {
        this(model, property, required, new Options().set(DATE_FORMAT, Options.asString(pattern)));
    }

    public DatePickerPanel(IModel<?> model, String property, boolean required, Options options) {
        super(model, property, Date.class, required, options);
    }

    private String parseString(Object o) {
        String tmp = String.valueOf(o);
        if (tmp.startsWith("\"") && tmp.endsWith("\"")) {
            tmp = tmp.substring(1, tmp.length() - 1);
        }
        return tmp;
    }

    @Override
    protected DatePicker createComponent() {
        // http://api.jqueryui.com/datepicker/
        IModel<Date> valueModel = getValueModel();
        options.set("appendText", options.get(DATE_FORMAT));
        return new DatePicker(VALUE, valueModel, parseString(options.get(DATE_FORMAT)), options) {
            private static final long serialVersionUID = 7118431260383127661L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
    }
}

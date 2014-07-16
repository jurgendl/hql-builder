package org.tools.hqlbuilder.webservice.wicket.forms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.common.icons.WicketIconsResources;
import org.tools.hqlbuilder.webservice.resources.datepicker.JQueryDatePicker;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;
import org.tools.hqlbuilder.webservice.wicket.converter.ModelConverter;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.DefaultFormRowPanel;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.core.utils.LocaleUtils;
import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;

/**
 * @see http://api.jqueryui.com/datepicker/
 * @see http://wicket.apache.org/guide/guide/jsintegration.html
 * @see https://github.com/jquery/jquery-ui
 * @see http://stackoverflow.com/questions/1452681/jquery-datepicker-localization
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DatePickerPanel<X> extends DefaultFormRowPanel {
    private static final long serialVersionUID = -5807168584242557542L;

    public static final String DATE_FORMAT = "dateFormat";

    public static final String APPEND_TEXT = "appendText";

    protected String dateFormat;

    protected String dateFormatClient;

    protected Converter<X, Date> dateConverter;

    public DatePickerPanel(IModel<?> model, Date propertyPath, FormSettings formSettings, FormElementSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
        this.dateConverter = null;
    }

    public DatePickerPanel(IModel<?> model, X propertyPath, Converter<X, Date> dateConverter, FormSettings formSettings,
            FormElementSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
        this.dateConverter = dateConverter;
    }

    @Override
    public IModel<Date> getValueModel() {
        if (dateConverter == null) {
            return super.getValueModel();
        }
        IModel<X> backingModel = new PropertyModel<X>(getDefaultModel(), getPropertyName());
        return new ModelConverter<X, Date>(backingModel, dateConverter);
    }

    @Override
    protected FormComponent createComponent(IModel model, Class valueType) {
        Locale locale = getLocale();
        Options options = new Options();
        dateFormat = dateformat(locale);
        dateFormatClient = dateFormat.toLowerCase().replaceAll("yyyy", "yy");
        options.set(DATE_FORMAT, Options.asString(dateFormatClient));
        // options.set(APPEND_TEXT, Options.asString(new SimpleDateFormat(dateFormat, locale).format(new Date())));
        return new DatePicker(VALUE, model, dateFormat, options) {
            private static final long serialVersionUID = 7118431260383127661L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
    }

    public static String dateformat(Locale locale) {
        return LocaleUtils.getLocaleDatePattern(locale, DateFormat.SHORT);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        if (!isEnabledInHierarchy()) {
            return;
        }

        response.render(JavaScriptHeaderItem.forReference(JQueryDatePicker.cached(getLocale())));
        response.render(JavaScriptHeaderItem.forReference(JQueryDatePicker.DATEPICKER_JS));

        CharSequence urlForIcon = urlFor(WicketIconsResources.REF_CALENDER, new PageParameters());
        String initScript = ";initJQDatepicker('" + getMarkupId() + "', '" + getLocale().getCountry() + "', '" + dateFormatClient + "', " + "'"
                + urlForIcon + "');";
        response.render(OnLoadHeaderItem.forScript(initScript));
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        tag.getAttributes().put(PLACEHOLDER, getPlaceholderText() + " <" + new SimpleDateFormat(dateFormat, getLocale()).format(new Date()) + ">");
    }
}

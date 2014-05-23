package org.tools.hqlbuilder.webservice.wicket.forms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.tools.hqlbuilder.common.icons.WicketIconsResources;
import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.js.WicketJSRoot;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;
import org.tools.hqlbuilder.webservice.wicket.converter.ModelConverter;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.core.utils.LocaleUtils;
import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;

/**
 * @see http://api.jqueryui.com/datepicker/
 * @see http://wicket.apache.org/guide/guide/jsintegration.html
 * @see https://github.com/jquery/jquery-ui
 * @see http://stackoverflow.com/questions/1452681/jquery-datepicker-localization
 */
public class DatePickerPanel<X> extends FormRowPanel<Date, DatePicker> {
    private static final long serialVersionUID = -5807168584242557542L;

    public static final JavaScriptResourceReference JQUERY_DATEPICKER_REFERENCE = new JavaScriptResourceReference(WicketJSRoot.class,
            "JQDatePicker.js");

    public static final String DATE_FORMAT = "dateFormat";

    public static final String APPEND_TEXT = "appendText";

    protected String dateFormat;

    protected String dateFormatClient;

    protected Converter<X, Date> dateConverter;

    public DatePickerPanel(IModel<?> model, String property, Converter<X, Date> dateConverter) {
        super(model, property, Date.class);
        this.dateConverter = dateConverter;
    }

    @Override
    protected IModel<Date> getValueModel() {
        if (dateConverter == null) {
            return super.getValueModel();
        }
        IModel<X> backingModel = new PropertyModel<X>(getDefaultModel(), property);
        return new ModelConverter<X, Date>(backingModel, dateConverter);
    };

    @Override
    protected DatePicker createComponent() {
        IModel<Date> valueModel = getValueModel();
        Locale locale = getLocale();
        Options options = new Options();
        dateFormat = dateformat(locale);
        dateFormatClient = dateFormat.toLowerCase().replaceAll("yyyy", "yy");
        options.set(DATE_FORMAT, Options.asString(dateFormatClient));
        // options.set(APPEND_TEXT, Options.asString(new SimpleDateFormat(dateFormat, locale).format(new Date())));
        return new DatePicker(VALUE, valueModel, dateFormat, options) {
            private static final long serialVersionUID = 7118431260383127661L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
    }

    public static final String RESOURCE_I18N_PATH = "jquery/ui/datepicker/i18n/";

    public static JavaScriptResourceReference DEFAULT = new JavaScriptResourceReference(WicketRoot.class, RESOURCE_I18N_PATH + "datepicker-en-GB.js");

    public static final Map<Locale, JavaScriptResourceReference> cache = new HashMap<Locale, JavaScriptResourceReference>();

    public static JavaScriptResourceReference cached(Locale locale) {
        if (cache.containsKey(locale)) {
            return cache.get(locale);
        }
        JavaScriptResourceReference resourceReference = get(locale);
        cache.put(locale, resourceReference);
        return resourceReference;
    }

    public static JavaScriptResourceReference get(Locale locale) {
        String language = locale.getLanguage();
        JavaScriptResourceReference uiRef = new JavaScriptResourceReference(WicketRoot.class, RESOURCE_I18N_PATH + "datepicker-" + language + "-"
                + locale.getCountry().toUpperCase() + ".js");
        boolean found = true;
        try {
            if (uiRef.getResource().getResourceStream().getInputStream().available() <= 0) {
                found = false;
            }
        } catch (Exception ex) {
            found = false;
        }
        if (found) {
            return uiRef;
        }
        uiRef = new JavaScriptResourceReference(WicketRoot.class, RESOURCE_I18N_PATH + "datepicker-" + language + ".js");
        found = true;
        try {
            if (uiRef.getResource().getResourceStream().getInputStream().available() <= 0) {
                found = false;
            }
        } catch (Exception ex) {
            found = false;
        }
        if (found) {
            return uiRef;
        }
        return DEFAULT;
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

        response.render(JavaScriptHeaderItem.forReference(cached(getLocale())));
        response.render(JavaScriptHeaderItem.forReference(JQUERY_DATEPICKER_REFERENCE));

        CharSequence urlForIcon = urlFor(WicketIconsResources.REF_CALENDER, new PageParameters());
        String initScript = ";initJQDatepicker('" + getMarkupId() + "', '" + getLocale().getCountry() + "', '" + dateFormatClient + "', " + "'"
                + urlForIcon + "');";
        response.render(OnLoadHeaderItem.forScript(initScript));
    }

    /**
     * @see org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel#setPlaceholder(org.apache.wicket.markup.ComponentTag)
     */
    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        tag.getAttributes().put(PLACEHOLDER, getPlaceholderText() + " <" + new SimpleDateFormat(dateFormat, getLocale()).format(new Date()) + ">");
    }
}

package org.tools.hqlbuilder.webservice.wicket.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * floating bar: http://stackoverflow.com/questions/17165915/how-to-build-floating-menu-bar-when-scroll-down
 */
public class LocalesPanel extends Panel implements IChoiceRenderer<Locale> {
    private static final long serialVersionUID = 645790077354220643L;

    public static class LocaleComparator implements Comparator<Locale> {
        @Override
        public int compare(Locale o1, Locale o2) {
            return new CompareToBuilder()//
            .append(o1.getDisplayLanguage(o1).toLowerCase(), o2.getDisplayLanguage(o2).toLowerCase())//
            .append(o1.getDisplayCountry(o1).toLowerCase(), o2.getDisplayCountry(o2).toLowerCase())//
            .append(o1.getDisplayVariant(o1).toLowerCase(), o2.getDisplayVariant(o2).toLowerCase())//
            .append(o1.getDisplayScript(o1).toLowerCase(), o2.getDisplayScript(o2).toLowerCase())//
            .toComparison();//
        }
    }

    public static JavaScriptResourceReference FLAGS_CSS = new JavaScriptResourceReference(LocalesPanel.class, "flags/flags.css");

    protected StatelessForm<Locale> localeForm;

    protected DropDownChoice<Locale> changeLocale;

    protected ListModel<Locale> optionsModel;

    protected IModel<Locale> valueModel;

    public LocalesPanel(String id) {
        this(id, getAvailableLocales());
    }

    public LocalesPanel(String id, List<Locale> locales) {
        super(id);

        Locale sessionLocale = getSession().getLocale();
        locales.remove(sessionLocale);
        locales.add(0, sessionLocale);
        optionsModel = new ListModel<Locale>(locales);
        valueModel = Model.of(sessionLocale);
        this.changeLocale = new DropDownChoice<Locale>("localeOptions", valueModel, optionsModel, this) {
            private static final long serialVersionUID = -4402883308787625917L;

            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }

            @Override
            protected void onSelectionChanged(Locale newSelection) {
                getSession().setLocale(newSelection);
            }
        };
        this.changeLocale.setNullValid(false);
        localeForm = new StatelessForm<Locale>("localeForm") {
            private static final long serialVersionUID = -7717228617856599476L;

            @Override
            protected void onSubmit() {
                getSession().setLocale(changeLocale.getModelObject());
            }
        };
        localeForm.setMarkupId(localeForm.getId());
        add(localeForm.add(changeLocale));
    }

    public static List<Locale> getAvailableLocales() {
        List<Locale> locales = new ArrayList<Locale>(Arrays.asList(Locale.getAvailableLocales()));
        Collections.sort(locales, new LocaleComparator());
        return locales;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(CssHeaderItem.forReference(FLAGS_CSS));
        StringBuilder script = new StringBuilder("var options_").append(changeLocale.getMarkupId()).append(" = new Array(\n");
        boolean add = false;
        for (Locale locale : optionsModel.getObject()) {
            if (add) {
                script.append(",");
            } else {
                add = true;
            }
            script.append("{ label: '").append(getDisplayValue(locale)).append("' , value: '").append(getIdValue(locale, 0)).append("' , country: '")
            .append(locale.getCountry().toLowerCase()).append("' }");
            script.append("\n");
        }
        script.append(");\n");
        script.append("$('#").append(changeLocale.getMarkupId()).append("').text('');\n");
        script.append("$('#").append(changeLocale.getMarkupId()).append("').puidropdown({ filter: true, filterMatchMode: 'contains', data: options_")
        .append(changeLocale.getMarkupId())
                .append(", content: function(option) { return '<img class=\"flag flag-' + option['country'] + '\"/> ' + option['label']; } ")
        .append("});\n");
        response.render(OnLoadHeaderItem.forScript(script.toString()));
    }

    @Override
    public Object getDisplayValue(Locale object) {
        String text = object.getDisplayLanguage(object) + (StringUtils.isBlank(object.getCountry()) ? "" : ", " + object.getDisplayCountry(object))
                + (StringUtils.isBlank(object.getVariant()) ? "" : ", " + object.getDisplayVariant(object))
                + (StringUtils.isBlank(object.getScript()) ? "" : ", " + object.getDisplayScript(object));
        return text;
    }

    @Override
    public String getIdValue(Locale object, int index) {
        return object.toString();
    }
}

package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * floating bar: http://stackoverflow.com/questions/17165915/how-to-build-floating-menu-bar-when-scroll-down
 */
@SuppressWarnings("serial")
public class LocalesPanel extends Panel {
    protected StatelessForm<Object> localeForm;

    protected DropDownChoice<Locale> changeLocale;

    protected List<Locale> locales;

    public LocalesPanel(String id) {
        this(id, getAvailableLocales());
    }

    public LocalesPanel(String id, List<Locale> locales) {
        super(id);

        this.locales = locales;
        Model<Locale> localeModel = new Model<Locale>();
        Locale sessionLocale = getSession().getLocale();
        Locale defaultLocale = null;
        for (Locale locale : locales) {
            if (locale.equals(sessionLocale)) {
                defaultLocale = locale;
            }
        }
        if (defaultLocale == null) {
            for (Locale locale : locales) {
                if (locale.getLanguage().equals(sessionLocale.getLanguage())) {
                    defaultLocale = locale;
                }
            }
        }
        if (defaultLocale == null) {
            defaultLocale = locales.get(0);
        }
        localeModel.setObject(defaultLocale);

        this.changeLocale = new DropDownChoice<Locale>("localeOptions", localeModel, locales, new IChoiceRenderer<Locale>() {
            @Override
            public Object getDisplayValue(Locale object) {
                return object.getDisplayLanguage(object) + ", " + object.getDisplayCountry(object);
            }

            @Override
            public String getIdValue(Locale object, int index) {
                return object.getDisplayLanguage(object) + "_" + object.getDisplayCountry(object);
            }
        }) {
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
        localeForm = new StatelessForm<Object>("localeForm") {
            @Override
            protected void onSubmit() {
                getSession().setLocale(changeLocale.getModelObject());
            }
        };
        localeForm.setMarkupId(localeForm.getId());
        add(localeForm.add(changeLocale));
    }

    public static class LocaleComparator implements Comparator<Locale> {
        @Override
        public int compare(Locale o1, Locale o2) {
            return new CompareToBuilder().append(o1.getDisplayLanguage(o1), o2.getDisplayLanguage(o2))
                    .append(o1.getDisplayCountry(o1), o2.getDisplayCountry(o2)).toComparison();
        }
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
        response.render(OnLoadHeaderItem.forScript("$( \"#" + changeLocale.getMarkupId() + "\" ).puidropdown();"));
    }
}

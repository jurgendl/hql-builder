package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class LocalesPanel extends Panel {
    private static final long serialVersionUID = -4736100030405768191L;

    protected StatelessForm<Object> localeForm;

    protected DropDownChoice<Locale> changeLocale;

    protected List<Locale> locales;

    public LocalesPanel(String id, List<Locale> locales) {
        super(id);

        this.locales = locales;
        Model<Locale> localeModel = new Model<Locale>();
        if (locales.contains(getSession().getLocale())) {
            localeModel.setObject(getSession().getLocale());
        } else {
            localeModel.setObject(locales.get(0));
        }
        this.changeLocale = new DropDownChoice<Locale>("localeOptions", localeModel, locales, new IChoiceRenderer<Locale>() {
            private static final long serialVersionUID = 3647609757885700569L;

            @Override
            public Object getDisplayValue(Locale object) {
                return object.getDisplayLanguage(object);
            }

            @Override
            public String getIdValue(Locale object, int index) {
                return object.getLanguage();
            }
        }) {

            private static final long serialVersionUID = -8172239789623605717L;

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
            private static final long serialVersionUID = 3472974973453278342L;

            @Override
            protected void onSubmit() {
                getSession().setLocale(changeLocale.getModelObject());
            }
        };
        localeForm.setMarkupId(localeForm.getId());
        add(localeForm.add(changeLocale));
    }
}

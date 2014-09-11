package org.tools.hqlbuilder.webservice.wicket.forms;

import java.util.Locale;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.tools.hqlbuilder.webservice.wicket.components.LocalesDropDown;

public class LocaleDropDownPanel extends FormRowPanel<Locale, Locale, LocalesDropDown, FormElementSettings> {
    private static final long serialVersionUID = 4721415841417284882L;

    protected ListModel<Locale> choices;

    protected IChoiceRenderer<Locale> renderer;

    public LocaleDropDownPanel(Locale propertyPath, IModel<Locale> valueModel, FormSettings formSettings, FormElementSettings componentSettings,
            ListModel<Locale> choices, IChoiceRenderer<Locale> renderer) {
        super(propertyPath, valueModel, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = renderer;
    }

    public LocaleDropDownPanel(IModel<?> model, Locale propertyPath, FormSettings formSettings, FormElementSettings componentSettings,
            ListModel<Locale> choices, IChoiceRenderer<Locale> renderer) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = renderer;
    }

    @Override
    public FormRowPanel<Locale, Locale, LocalesDropDown, FormElementSettings> setValueModel(IModel<Locale> model) {
        getComponent().setModel(model);
        return super.setValueModel(model);
    }

    @Override
    public Class<Locale> getPropertyType() {
        return Locale.class;
    }

    @Override
    public IModel<Locale> getValueModel() {
        if (valueModel == null) {
            valueModel = new PropertyModel<Locale>(getDefaultModel(), getPropertyName());
        }
        return valueModel;
    }

    @Override
    protected LocalesDropDown createComponent(IModel<Locale> model, Class<Locale> valueType) {
        LocalesDropDown comp = new LocalesDropDown(VALUE, choices, model, renderer);
        return comp;
    }
}

package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel;

public class RadioButtonsPanel<T> extends FormRowPanel<T, RadioChoice<T>> {
    private static final long serialVersionUID = 1409542083276399035L;

    protected ListModel<T> choices;

    protected IChoiceRenderer<T> renderer;

    public RadioButtonsPanel(IModel<?> model, String property, Class<T> type, FormSettings formSettings, FormElementSettings componentSettings,
            ListModel<T> choices, IChoiceRenderer<T> renderer) {
        super(model, property, type, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = renderer;
    }

    @Override
    protected RadioChoice<T> createComponent() {
        RadioChoice<T> radioChoice = new RadioChoice<T>(VALUE, getValueModel(), choices, renderer) {
            private static final long serialVersionUID = -2906136100384055681L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
        radioChoice.setSuffix("");
        return radioChoice;
    }

    @Override
    protected void setupRequired(ComponentTag tag) {
        //
    }

    @Override
    protected void setupRequired(RadioChoice<T> component) {
        component.setRequired(componentSettings.isRequired());
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }
}

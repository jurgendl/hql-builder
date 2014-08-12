package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;

public class CheckBoxPanel extends DefaultFormRowPanel<Boolean, CheckBox> {
    private static final long serialVersionUID = 7669787482921703670L;

    public CheckBoxPanel(IModel<?> model, Boolean propertyPath, FormSettings formSettings, FormElementSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected CheckBox createComponent(IModel<Boolean> model, Class<Boolean> valueType) {
        return new CheckBox(VALUE, model);
    }

    @Override
    protected void setupRequired(CheckBox component) {
        component.setRequired(componentSettings.isRequired());
    }
}

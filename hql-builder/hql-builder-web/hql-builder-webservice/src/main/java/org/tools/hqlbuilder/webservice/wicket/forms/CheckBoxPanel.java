package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel;

public class CheckBoxPanel extends FormRowPanel<Boolean, CheckBox> {
    private static final long serialVersionUID = 7669787482921703670L;

    public CheckBoxPanel(IModel<?> model, Boolean propertyPath, FormSettings formSettings, FormElementSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected CheckBox createComponent() {
        return new CheckBox(VALUE, getValueModel());
    }

    @Override
    protected void setupRequired(CheckBox component) {
        component.setRequired(componentSettings.isRequired());
    }
}

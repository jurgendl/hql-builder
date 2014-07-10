package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel;

public class DropDownPanel<T> extends FormRowPanel<T, DropDownChoice<T>> {
    private static final long serialVersionUID = -4693793144091792295L;

    protected ListModel<T> choices;

    protected IChoiceRenderer<T> renderer;

    public DropDownPanel(IModel<?> model, String property, Class<T> type, FormSettings formSettings, FormElementSettings componentSettings,
            ListModel<T> choices, IChoiceRenderer<T> renderer) {
        super(model, property, type, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = renderer;
    }

    @Override
    protected DropDownChoice<T> createComponent() {
        DropDownChoice<T> dropDownChoice = new DropDownChoice<T>(VALUE, getValueModel(), choices, renderer) {
            private static final long serialVersionUID = 1143647284311142999L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
        return dropDownChoice;
    }
}

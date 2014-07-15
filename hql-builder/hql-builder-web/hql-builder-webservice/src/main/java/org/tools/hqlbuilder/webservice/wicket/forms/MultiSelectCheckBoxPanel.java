package org.tools.hqlbuilder.webservice.wicket.forms;

import java.util.Collection;

import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.DefaultFormRowPanel;

public class MultiSelectCheckBoxPanel<T> extends DefaultFormRowPanel<Collection<T>, CheckBoxMultipleChoice<T>> {
    private static final long serialVersionUID = -637534401267056720L;

    protected ListModel<T> choices;

    protected IChoiceRenderer<T> renderer;

    public MultiSelectCheckBoxPanel(IModel<?> model, Collection<T> propertyPath, FormSettings formSettings, FormElementSettings componentSettings,
            ListModel<T> choices, IChoiceRenderer<T> renderer) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = renderer;
    }

    @Override
    protected CheckBoxMultipleChoice<T> createComponent(IModel<Collection<T>> model, Class<Collection<T>> valueType) {
        CheckBoxMultipleChoice<T> checkBoxMultipleChoice = new CheckBoxMultipleChoice<T>(VALUE, model, choices, renderer);
        checkBoxMultipleChoice.setSuffix("");
        return checkBoxMultipleChoice;
    }
}

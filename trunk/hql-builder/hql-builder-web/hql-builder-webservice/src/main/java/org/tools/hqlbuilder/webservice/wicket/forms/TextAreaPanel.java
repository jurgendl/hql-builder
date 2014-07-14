package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel;

public class TextAreaPanel<T> extends FormRowPanel<T, TextArea<T>> {
    private static final long serialVersionUID = 7189330022100675150L;

    public TextAreaPanel(final IModel<?> model, final T propertyPath, FormSettings formSettings, TextAreaSettings textAreaSettings) {
        super(model, propertyPath, formSettings, textAreaSettings);
    }

    @Override
    protected TextArea<T> createComponent() {
        TextArea<T> textArea = new TextArea<T>(VALUE, getValueModel()) {
            private static final long serialVersionUID = 4613842350545363891L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                TextAreaSettings textAreaSettings = TextAreaSettings.class.cast(getComponentSettings());
                tag(tag, "cols", textAreaSettings.getCols());
                tag(tag, "rows", textAreaSettings.getRows());
                onFormComponentTag(tag);
            }
        };
        return textArea;
    }
}

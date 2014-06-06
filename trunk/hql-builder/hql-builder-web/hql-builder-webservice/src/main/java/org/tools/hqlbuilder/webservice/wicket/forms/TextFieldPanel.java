package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel;

public class TextFieldPanel<T> extends FormRowPanel<T, TextField<T>> {
    private static final long serialVersionUID = -7993592150932306594L;

    public TextFieldPanel(final IModel<?> model, final String property, final Class<T> type, FormSettings formSettings,
            FormElementSettings componentSettings) {
        super(model, property, type, formSettings, componentSettings);
    }

    @Override
    protected TextField<T> createComponent() {
        return new TextField<T>(VALUE, getValueModel(), type) {
            private static final long serialVersionUID = -3231896888772971388L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
    }
}

package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel;

public class EmailTextFieldPanel extends FormRowPanel<String, EmailTextField> {
    private static final long serialVersionUID = -7993592150932306594L;

    public EmailTextFieldPanel(final IModel<?> model, final String property, final boolean required) {
        super(model, property, String.class, required);
    }

    @Override
    protected EmailTextField createComponent() {
        return new EmailTextField(VALUE, getValueModel()) {
            private static final long serialVersionUID = -8333366326586690978L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
    }
}

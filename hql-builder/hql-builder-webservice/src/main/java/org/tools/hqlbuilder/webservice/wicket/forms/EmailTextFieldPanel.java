package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.model.IModel;

public class EmailTextFieldPanel extends FormRowPanel<String> {
    private static final long serialVersionUID = -7993592150932306594L;

    protected EmailTextField emailTextField;

    public EmailTextFieldPanel(final IModel<?> model, final String property) {
        super(model, property, String.class);
    }

    public EmailTextField getEmailTextField() {
        return this.emailTextField;
    }

    @Override
    protected void addComponent() {
        emailTextField = new EmailTextField(VALUE, getValueModel()) {
            private static final long serialVersionUID = -3814319098009064211L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                setPlaceholder(tag);
            }
        };
        add(emailTextField.setMarkupId(property));
    }
}

package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.extensions.validation.validator.RfcCompliantEmailAddressValidator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.model.IModel;

public class EmailTextFieldPanel extends DefaultFormRowPanel<String, EmailTextField> {
    private static final long serialVersionUID = -7993592150932306594L;

    public EmailTextFieldPanel(final IModel<?> model, final String propertyPath, FormSettings formSettings, FormElementSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected EmailTextField createComponent(IModel<String> model, Class<String> valueType) {
        return new EmailTextField(VALUE, model, RfcCompliantEmailAddressValidator.getInstance()) {
            private static final long serialVersionUID = -8333366326586690978L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
    }
}

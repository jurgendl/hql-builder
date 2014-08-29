package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.IModel;

/**
 * @see http://www.primefaces.org/primeui/password.html
 */
public class PasswordTextFieldPanel extends DefaultFormRowPanel<String, PasswordTextField, FormElementSettings> {
    private static final long serialVersionUID = -7993592150932306594L;

    public PasswordTextFieldPanel(final IModel<?> model, final String propertyPath, FormSettings formSettings, FormElementSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected PasswordTextField createComponent(IModel<String> model, Class<String> valueType) {
        return new PasswordTextField(VALUE, model) {
            private static final long serialVersionUID = -7265008846653720072L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(OnLoadHeaderItem.forScript("$( \"#" + getComponent().getMarkupId() + "\" ).puipassword();"));
    }
}

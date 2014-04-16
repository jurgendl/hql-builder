package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class TextFieldPanel<T> extends FormRowPanel<T> {
    private static final long serialVersionUID = -7993592150932306594L;

    protected TextField<T> textField;

    public TextFieldPanel(final IModel<?> model, final String property, final Class<T> type) {
        super(model, property, type);
    }

    public TextField<T> getTextField() {
        return this.textField;
    }

    @Override
    protected void addComponent() {
        textField = new TextField<T>(VALUE, getValueModel(), type) {
            private static final long serialVersionUID = -3814319098009064211L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                setPlaceholder(tag);
            }
        };
        add(textField.setMarkupId(property));
    }
}

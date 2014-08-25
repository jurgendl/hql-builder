package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class TextFieldPanel<T extends Serializable> extends DefaultFormRowPanel<T, TextField<T>, FormElementSettings> {
    private static final long serialVersionUID = -7993592150932306594L;

    public TextFieldPanel(final IModel<?> model, final T propertyPath, FormSettings formSettings, FormElementSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected TextField<T> createComponent(IModel<T> model, Class<T> valueType) {
        TextField<T> textField = new TextField<T>(VALUE, model, valueType) {
            private static final long serialVersionUID = -3231896888772971388L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
        return textField;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(new JavaScriptContentHeaderItem("$(function() { $( \"#" + getComponent().getMarkupId() + "\" ).puiinputtext(); });", "js_"
                + getComponent().getMarkupId() + "_" + System.currentTimeMillis(), null));
    }
}

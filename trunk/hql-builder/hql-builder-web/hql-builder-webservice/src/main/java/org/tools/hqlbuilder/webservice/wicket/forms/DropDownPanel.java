package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

public class DropDownPanel<T extends Serializable> extends SelectPanel<T, Select<T>, DropDownSettings> {
    private static final long serialVersionUID = -4693793144091792295L;

    public DropDownPanel(IModel<?> model, T propertyPath, FormSettings formSettings, DropDownSettings componentSettings, IOptionRenderer<T> renderer,
            ListModel<T> choices) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices);
    }

    public DropDownPanel(IModel<?> model, T propertyPath, FormSettings formSettings, DropDownSettings componentSettings, IOptionRenderer<T> renderer,
            ListModel<T>[] choices, IModel<String>[] groupLabels) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices, groupLabels);
    }

    @Override
    protected Select<T> createComponent(IModel<T> model) {
        return new Select<T>(VALUE, model) {
            private static final long serialVersionUID = 1143647284311142999L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
    }

    @Override
    protected boolean isNullValid() {
        return getComponentSettings().isNullValid();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(new JavaScriptContentHeaderItem("$(function() { $( \"#" + getComponent().getMarkupId() + "\" ).puidropdown(); });", "js_"
                + getComponent().getMarkupId() + "_" + System.currentTimeMillis(), null));
    }
}

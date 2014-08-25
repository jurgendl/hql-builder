package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;

public class CheckBoxPanel extends DefaultFormRowPanel<Boolean, CheckBox, FormElementSettings> {
    private static final long serialVersionUID = 7669787482921703670L;

    public CheckBoxPanel(IModel<?> model, Boolean propertyPath, FormSettings formSettings, FormElementSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected CheckBox createComponent(IModel<Boolean> model, Class<Boolean> valueType) {
        return new CheckBox(VALUE, model);
    }

    @Override
    protected void setupRequired(CheckBox component) {
        component.setRequired(componentSettings.isRequired());
    }

    @Override
    protected Label getLabel() {
        if (label == null) {
            label = new Label(LABEL, getLabelModel()) {
                private static final long serialVersionUID = 8512361193054906821L;

                @Override
                public boolean isVisible() {
                    return super.isVisible() && (formSettings == null || formSettings.isShowLabel());
                }

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    tag.getAttributes().put(FOR, getComponent().getMarkupId() + "before");
                }
            };
        }
        return label;
    }

    @Override
    protected CheckBoxPanel addComponents() {
        this.add(getLabel());
        this.add(getComponent());
        this.add(new Label("checkboxlabel", getLabelModel()) {
            private static final long serialVersionUID = 7533571573957789273L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.getAttributes().put(FOR, getComponent().getMarkupId());
            }
        });
        this.add(getFeedback());
        return this;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(new JavaScriptContentHeaderItem("$(function() { $( \"#" + getComponent().getMarkupId() + "\" ).button(); });", "js_"
                + getComponent().getMarkupId() + "_" + System.currentTimeMillis(), null));
    }
}

package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.tools.hqlbuilder.webservice.jquery.ui.tristate.TriState;
import org.tools.hqlbuilder.webservice.jquery.ui.tristate.TriStateValue;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://jqueryui.com/button/
 * @see http://vanderlee.github.io/tristate/
 */
public class TriStateCheckBoxPanel extends FormRowPanel<TriStateValue, Boolean, CheckBox, TriStateCheckBoxSettings> {
    private static final long serialVersionUID = 7669787482921703670L;

    public TriStateCheckBoxPanel(IModel<?> model, TriStateValue propertyPath, FormSettings formSettings, TriStateCheckBoxSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    public TriStateCheckBoxPanel(TriStateValue propertyPath, IModel<Boolean> valueModel, FormSettings formSettings,
            TriStateCheckBoxSettings componentSettings) {
        super(propertyPath, valueModel, formSettings, componentSettings);
    }

    @Override
    protected void setupRequired(CheckBox component) {
        //
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
    protected TriStateCheckBoxPanel addComponents() {
        this.add(getLabel());
        this.add(getComponent());
        this.add(getRequiredMarker().setVisible(false));
        this.add(getFeedback());
        return this;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(TriState.TRISTATE_FACTORY_JS));
    }

    @Override
    protected CheckBox createComponent(IModel<Boolean> model, Class<Boolean> valueType) {
        CheckBox checkBox = new CheckBox(VALUE, model);
        checkBox.add(new CssClassNameAppender("tristate"));
        checkBox.add(new AttributeModifier("checkedvalue", Model.of(String.valueOf(TriStateValue.on))));
        checkBox.add(new AttributeModifier("uncheckedvalue", Model.of(String.valueOf(TriStateValue.off))));
        checkBox.add(new AttributeModifier("indeterminatevalue", Model.of(String.valueOf(TriStateValue.indeterminate))));
        return checkBox;
    }
}

package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.Collection;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://jqueryui.com/button/
 */
public class MultiSelectCheckBoxPanel<T extends Serializable> extends
FormRowPanel<Collection<T>, Collection<T>, CheckBoxMultipleChoice<T>, FormElementSettings> {
    private static final long serialVersionUID = -637534401267056720L;

    protected ListModel<T> choices;

    protected IChoiceRenderer<T> renderer;

    public MultiSelectCheckBoxPanel(IModel<?> model, Collection<T> propertyPath, FormSettings formSettings, FormElementSettings componentSettings,
            ListModel<T> choices, IChoiceRenderer<T> renderer) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = renderer;
    }

    @Override
    protected CheckBoxMultipleChoice<T> createComponent(IModel<Collection<T>> model, Class<Collection<T>> valueType) {
        CheckBoxMultipleChoice<T> checkBoxMultipleChoice = new CheckBoxMultipleChoice<T>(VALUE, model, choices, renderer);
        checkBoxMultipleChoice.setPrefix("<span class=\"multiselectchoice\">");
        checkBoxMultipleChoice.setSuffix("</span>");
        checkBoxMultipleChoice.add(new CssClassNameAppender(JQueryUI.jquibuttonset));
        return checkBoxMultipleChoice;
    }

    @Override
    public IModel<Collection<T>> getValueModel() {
        if (valueModel == null) {
            valueModel = new PropertyModel<Collection<T>>(getDefaultModel(), getPropertyName());
        }
        return valueModel;
    }

    @Override
    public Class<Collection<T>> getPropertyType() {
        if (propertyType == null) {
            this.propertyType = WebHelper.type(propertyPath);
        }
        return this.propertyType;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(JQueryUI.JQUERY_UI_FACTORY_JS));
    }
}

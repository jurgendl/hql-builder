package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

/**
 * @see http://jqueryui.com/button/
 */
public class RadioButtonsPanel<T extends Serializable> extends DefaultFormRowPanel<T, RadioChoice<T>, FormElementSettings> {
    private static final long serialVersionUID = 1409542083276399035L;

    protected ListModel<T> choices;

    protected IChoiceRenderer<T> renderer;

    public RadioButtonsPanel(IModel<?> model, T propertyPath, FormSettings formSettings, FormElementSettings componentSettings, ListModel<T> choices,
            IChoiceRenderer<T> renderer) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = renderer;
    }

    @Override
    protected RadioChoice<T> createComponent(IModel<T> model, Class<T> valueType) {
        RadioChoice<T> radioChoice = new RadioChoice<T>(VALUE, model, choices, renderer);
        radioChoice.setPrefix("<span class=\"multiselectchoice\">");
        radioChoice.setSuffix("</span>");
        return radioChoice;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        if (formSettings.isPreferPrime()) {
            response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_JS));
            response.render(JavaScriptHeaderItem.forScript("$(function() { $('#" + getComponent().getMarkupId()
                    + " input[type=\"radio\"]').puiradiobutton(); });", getComponent().getMarkupId()));
        } else {
            response.render(JavaScriptHeaderItem.forReference(JQueryUI.getJQueryUIReference()));
            response.render(JavaScriptHeaderItem.forScript("$(function() { $('.multiselectchoice').buttonset(); });", "js_"
                    + getComponent().getMarkupId()));
        }
    }
}

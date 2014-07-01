package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.Spectrum;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel;

/**
 * @see http://bgrins.github.io/spectrum/#why
 */
public class ColorPickerPanel extends FormRowPanel<String, TextField<String>> {
    private static final long serialVersionUID = 3920158103962799959L;

    public ColorPickerPanel(final IModel<?> model, final String property, FormSettings formSettings, FormElementSettings componentSettings) {
        super(model, property, String.class, formSettings, componentSettings);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);
    }

    @Override
    protected TextField<String> createComponent() {
        return new TextField<String>(VALUE, getValueModel(), type) {
            private static final long serialVersionUID = -989970628058227688L;

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

        response.render(CssHeaderItem.forReference(Spectrum.SPECTRUM_CSS));
        response.render(JavaScriptHeaderItem.forReference(Spectrum.SPECTRUM_JS));
        String def = getValueModel() == null || StringUtils.isBlank(getValueModel().getObject()) ? "#fff" : getValueModel().getObject();
        response.render(OnDomReadyHeaderItem.forScript("$(\"#" + getComponent().getMarkupId() + "\").spectrum({color: \"" + def + "\"});"));
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }
}

package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.DefaultFormRowPanel;

public class NumberFieldPanel<N extends Number & Comparable<N>> extends DefaultFormRowPanel<N, NumberTextField<N>> {
    private static final long serialVersionUID = -3037822852757814685L;

    public NumberFieldPanel(IModel<?> model, N propertyPath, FormSettings formSettings, NumberFieldSettings<N> componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected NumberTextField<N> createComponent(IModel<N> model, Class<N> valueType) {
        return new NumberTextField<N>(VALUE, model, valueType) {
            private static final long serialVersionUID = 8287393178708047572L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
                @SuppressWarnings("unchecked")
                NumberFieldSettings<N> settings = (NumberFieldSettings<N>) getComponentSettings();
                tag(tag, "min", settings.getMinimum());
                tag(tag, "max", settings.getMaximum());
                tag(tag, "step", settings.getStep());
            }
        };
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }
}

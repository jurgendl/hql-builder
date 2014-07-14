package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.RangeTextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel;

public class RangeFieldPanel<N extends Number & Comparable<N>> extends FormRowPanel<N, RangeTextField<N>> {
    private static final long serialVersionUID = 317764716316092786L;

    public RangeFieldPanel(IModel<?> model, N propertyPath, FormSettings formSettings, NumberFieldSettings<N> componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected RangeTextField<N> createComponent() {
        return new RangeTextField<N>(VALUE, getValueModel(), getPropertyType()) {
            private static final long serialVersionUID = 5507304679724490593L;

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

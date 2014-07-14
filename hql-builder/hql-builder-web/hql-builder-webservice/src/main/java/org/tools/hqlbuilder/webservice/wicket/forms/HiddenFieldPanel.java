package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel;

public class HiddenFieldPanel<T> extends FormRowPanel<T, HiddenField<T>> {
    private static final long serialVersionUID = -7993592150932306594L;

    public HiddenFieldPanel(final IModel<?> model, final T propertyPath) {
        super(model, propertyPath, null, null);
    }

    @Override
    protected HiddenField<T> createComponent() {
        return new HiddenField<T>(VALUE, getValueModel(), getPropertyType());
    }

    @Override
    protected HiddenField<T> addComponentsTo(RepeatingView repeater) {
        HiddenField<T> comp = getComponent();
        this.add(comp);
        WebMarkupContainer rowContainer = getContainer(repeater);
        repeater.add(rowContainer);
        rowContainer.add(this);
        return comp;
    }

    @Override
    protected void setupRequired(HiddenField<T> component) {
        //
    }
}

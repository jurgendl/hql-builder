package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.DefaultFormRowPanel;

public class HiddenFieldPanel<T> extends DefaultFormRowPanel<T, HiddenField<T>> {
    private static final long serialVersionUID = -7993592150932306594L;

    public HiddenFieldPanel(final IModel<?> model, final T propertyPath) {
        super(model, propertyPath, null, null);
    }

    @Override
    protected HiddenField<T> createComponent(IModel<T> model, Class<T> valueType) {
        return new HiddenField<T>(VALUE, model, valueType);
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

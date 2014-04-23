package org.tools.hqlbuilder.webservice.wicket.forms;

import java.util.Date;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel;

import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;

public class AjaxDatePickerPanel extends FormRowPanel<Date, AjaxDatePicker> {
    private static final long serialVersionUID = -5807168584242557542L;

    public AjaxDatePickerPanel(IModel<?> model, String property, boolean required) {
        super(model, property, Date.class, required);
    }

    @Override
    protected AjaxDatePicker createComponent() {
        return new AjaxDatePicker(VALUE, getValueModel()) {
            private static final long serialVersionUID = 7118431260383127661L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
    }
}

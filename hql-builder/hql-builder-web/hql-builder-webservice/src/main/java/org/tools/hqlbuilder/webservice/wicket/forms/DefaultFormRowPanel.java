package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.tools.hqlbuilder.common.CommonUtils;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;

public abstract class DefaultFormRowPanel<T extends Serializable, C extends FormComponent<T>, S extends FormElementSettings> extends
FormRowPanel<T, T, C, S> {
    private static final long serialVersionUID = -3609764520190287373L;

    public DefaultFormRowPanel(IModel<?> model, T propertyPath, FormSettings formSettings, S componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    public void setValueModel(Model<T> model) {
        valueModel = model;
        getComponent().setModel(model);
    }

    @Override
    public IModel<T> getValueModel() {
        if (valueModel == null) {
            valueModel = new PropertyModel<T>(getDefaultModel(), getPropertyName());
        }
        return valueModel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getPropertyType() {
        if (propertyType == null) {
            try {
                this.propertyType = WebHelper.type(propertyPath);
            } catch (ch.lambdaj.function.argument.ArgumentConversionException ex) {
                try {
                    this.propertyType = CommonUtils.<T> getImplementation(this, DefaultFormRowPanel.class);
                } catch (IllegalArgumentException ex2) {
                    this.propertyType = (Class<T>) Serializable.class;
                }
            }
        }
        return this.propertyType;
    }
}
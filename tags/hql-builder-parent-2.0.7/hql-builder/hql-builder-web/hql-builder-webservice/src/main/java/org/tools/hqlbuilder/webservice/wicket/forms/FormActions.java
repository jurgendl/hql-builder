package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public interface FormActions<T extends Serializable> extends Serializable {
    public abstract T loadObject() throws UnsupportedOperationException;

    public abstract IModel<T> loadModel();

    public abstract void afterCancel(AjaxRequestTarget target, Form<T> form, IModel<T> model);

    public abstract void submitModel(IModel<T> model);

    public abstract void submitObject(T object) throws UnsupportedOperationException;

    public abstract void afterSubmit(AjaxRequestTarget target, Form<T> form, IModel<T> model);

    public abstract Class<T> forObjectClass();
}
package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public interface FormActions<T> extends Serializable {
    public abstract void afterCancel(AjaxRequestTarget target, Form<T> form, IModel<T> model);

    public abstract void submit(IModel<T> model);

    public abstract boolean isAjax();

    public abstract boolean isCancelable();

    public abstract void afterSubmit(AjaxRequestTarget target, Form<T> form, IModel<T> model);
}
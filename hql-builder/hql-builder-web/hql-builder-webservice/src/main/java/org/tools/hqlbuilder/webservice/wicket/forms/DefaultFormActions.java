package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public class DefaultFormActions<T> implements FormActions<T> {
    private static final long serialVersionUID = 555158530492799693L;

    @Override
    public void afterSubmit(AjaxRequestTarget target, Form<T> form, IModel<T> model) {
        if (target != null) {
            target.add(form);
        }
    }

    @Override
    public boolean isAjax() {
        return true;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    @Override
    public void afterCancel(AjaxRequestTarget target, Form<T> form, IModel<T> model) {
        afterSubmit(target, form, model);
    }

    @Override
    public void submit(IModel<T> model) {
        //
    }
}
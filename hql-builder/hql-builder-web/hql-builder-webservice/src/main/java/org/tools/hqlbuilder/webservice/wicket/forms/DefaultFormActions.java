package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public class DefaultFormActions<T> implements FormActions<T> {
    private static final long serialVersionUID = 555158530492799693L;

    protected boolean ajax = true;

    protected boolean cancelable = false;

    public DefaultFormActions() {
        super();
    }

    @Override
    public void afterSubmit(AjaxRequestTarget target, Form<T> form, IModel<T> model) {
        if (target != null) {
            target.add(form);
        }
    }

    @Override
    public boolean isAjax() {
        return ajax;
    }

    @Override
    public boolean isCancelable() {
        return cancelable;
    }

    @Override
    public void afterCancel(AjaxRequestTarget target, Form<T> form, IModel<T> model) {
        if (target != null) {
            target.add(form);
        }
    }

    @Override
    public void submit(IModel<T> model) {
        //
    }

    public DefaultFormActions<T> setAjax(boolean ajax) {
        this.ajax = ajax;
        return this;
    }

    public DefaultFormActions<T> setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }
}
package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.tools.hqlbuilder.common.CommonUtils;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;

public class DefaultFormActions<T extends Serializable> implements FormActions<T> {
    private static final long serialVersionUID = 555158530492799693L;

    public DefaultFormActions() {
        super();
    }

    public void ajaxRefreshForm(AjaxRequestTarget target, Form<T> form) {
        if (target != null) {
            target.add(form);
        }
    }

    /**
     * @see org.tools.hqlbuilder.webservice.wicket.forms.FormActions#afterSubmit(org.apache.wicket.ajax.AjaxRequestTarget,
     *      org.apache.wicket.markup.html.form.Form, org.apache.wicket.model.IModel)
     */
    @Override
    public void afterSubmit(AjaxRequestTarget target, Form<T> form, IModel<T> model) {
        ajaxRefreshForm(target, form);
    }

    /**
     * @see org.tools.hqlbuilder.webservice.wicket.forms.FormActions#afterCancel(org.apache.wicket.ajax.AjaxRequestTarget,
     *      org.apache.wicket.markup.html.form.Form, org.apache.wicket.model.IModel)
     */
    @Override
    public void afterCancel(AjaxRequestTarget target, Form<T> form, IModel<T> model) {
        ajaxRefreshForm(target, form);
    }

    /**
     *
     * @see org.tools.hqlbuilder.webservice.wicket.forms.FormActions#submitModel(org.apache.wicket.model.IModel)
     */
    @Override
    public void submitModel(IModel<T> model) {
        try {
            submitObject(model.getObject());
        } catch (UnsupportedOperationException ex) {
            ex.printStackTrace(System.out);
        }
    }

    /**
     *
     * @see org.tools.hqlbuilder.webservice.wicket.forms.FormActions#submitObject(java.lang.Object)
     */
    @Override
    public void submitObject(T object) {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @see org.tools.hqlbuilder.webservice.wicket.forms.FormActions#loadModel()
     */
    @Override
    public IModel<T> loadModel() {
        try {
            return WebHelper.model(loadObject());
        } catch (UnsupportedOperationException ex) {
            ex.printStackTrace(System.out);
            return Model.<T> of();
        }
    }

    /**
     *
     * @see org.tools.hqlbuilder.webservice.wicket.forms.FormActions#loadObject()
     */
    @Override
    public T loadObject() {
        try {
            return WebHelper.create(forObjectClass());
        } catch (org.springframework.beans.BeanInstantiationException ex) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     *
     * @see org.tools.hqlbuilder.webservice.wicket.forms.FormActions#forObjectClass()
     */
    @Override
    public Class<T> forObjectClass() {
        return CommonUtils.<T> getImplementation(this, DefaultFormActions.class);
        // return (Class<T>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
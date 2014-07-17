package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;

public class DefaultFormActions<T> implements FormActions<T> {
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
        submitObject(model.getObject());
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
        return WebHelper.model(loadObject());
    }

    /**
     *
     * @see org.tools.hqlbuilder.webservice.wicket.forms.FormActions#loadObject()
     */
    @Override
    public T loadObject() {
        return WebHelper.create(forObjectClass());
    }

    /**
     *
     * @see org.tools.hqlbuilder.webservice.wicket.forms.FormActions#forObjectClass()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Class<T> forObjectClass() {
        return (Class<T>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
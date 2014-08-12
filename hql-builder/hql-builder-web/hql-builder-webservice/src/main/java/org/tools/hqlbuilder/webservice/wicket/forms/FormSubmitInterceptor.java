package org.tools.hqlbuilder.webservice.wicket.forms;

public interface FormSubmitInterceptor {
    public void onBeforeSubmit();

    public void onAfterSubmit();
}
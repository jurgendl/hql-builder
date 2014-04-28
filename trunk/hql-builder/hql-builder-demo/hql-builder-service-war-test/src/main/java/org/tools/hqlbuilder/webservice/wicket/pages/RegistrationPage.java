package org.tools.hqlbuilder.webservice.wicket.pages;

import java.io.Serializable;
import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.tools.hqlbuilder.test.Registration;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;

import ch.lambdaj.Lambda;

@MountedPage("/form/registration")
public class RegistrationPage extends DefaultWebPage {
    private static final long serialVersionUID = 264876407045636533L;

    @SpringBean
    protected transient HqlWebServiceClient hqlWebClient;

    public RegistrationPage(PageParameters parameters) {
        super(parameters);

        FormPanel<Registration> formPanel = new FormPanel<Registration>("userdata.form", Registration.class) {
            private static final long serialVersionUID = -2653547660762438431L;

            @Override
            protected void submit(IModel<Registration> model) {
                Registration object = model.getObject();
                object.setVerification(new Date());
                Serializable id = hqlWebClient.save(object);
                object = hqlWebClient.get(object.getClass(), id);
                model.setObject(object);
            }
        };
        add(formPanel);
        Registration registration = create(Registration.class);

        formPanel.addTextField(name(registration.getUsername()), true);
        formPanel.addTextField(name(registration.getFirstName()), true);
        formPanel.addTextField(name(registration.getLastName()), true);
        formPanel.addEmailTextField(name(registration.getEmail()), true);
        formPanel.addPasswordTextField(name(registration.getPassword()), true);
        add(new Label("registration.date", new PropertyModel<Date>(formPanel.getDefaultModelObject(), name(registration.getVerification()))) {
            private static final long serialVersionUID = 3305246087333291118L;

            @Override
            public boolean isVisible() {
                return super.isVisible() && getDefaultModel().getObject() != null;
            }

        });
        // formPanel.liveValidation();
    }

    public static <T> T create(Class<T> type) {
        return Lambda.on(type);
    }

    public static <A> String name(A arg) {
        return Lambda.argument(arg).getInkvokedPropertyName();
    }
}

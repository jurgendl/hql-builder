package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.tools.hqlbuilder.test.Registration;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;

import ch.lambdaj.Lambda;

@MountedPage("/form/registration")
public class FormPage extends DefaultWebPage {
    private static final long serialVersionUID = 264876407045636533L;

    @SpringBean
    protected transient HqlWebServiceClient hqlWebClient;

    public FormPage(PageParameters parameters) {
        super(parameters);

        FormPanel<Registration> formPanel = new FormPanel<Registration>("userdata.form", Registration.class) {
            private static final long serialVersionUID = -2653547660762438431L;

            @Override
            protected void submit(IModel<Registration> model) {
                Registration object = model.getObject();
                System.out.println(hqlWebClient);
                System.out.println(object.getFirstName());
                System.out.println(object.getLastName());
                System.out.println(object.getEmail());
            }
        };
        add(formPanel);
        Registration registration = create(Registration.class);

        formPanel.addTextField(name(registration.getUsername()), true);
        formPanel.addTextField(name(registration.getFirstName()), true);
        formPanel.addTextField(name(registration.getLastName()), true);
        formPanel.addEmailTextField(name(registration.getEmail()), true);
        formPanel.addPasswordTextField(name(registration.getPassword()), true);
        // formPanel.liveValidation();
    }

    public static <T> T create(Class<T> type) {
        return Lambda.on(type);
    }

    public static <A> String name(A arg) {
        return Lambda.argument(arg).getInkvokedPropertyName();
    }
}

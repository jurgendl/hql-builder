package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.UserData;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;

@MountedPage("/form")
public class FormPage extends DefaultWebPage {
    private static final long serialVersionUID = 264876407045636533L;

    public FormPage(PageParameters parameters) {
        super(parameters);

        FormPanel<UserData> formPanel = new FormPanel<UserData>("userdata.form", UserData.class) {
            private static final long serialVersionUID = -2653547660762438431L;

            @Override
            protected void submit(IModel<UserData> model) {
                UserData object = model.getObject();
                System.out.println(object.getFirstName());
                System.out.println(object.getLastName());
                System.out.println(object.getEmail());
            }
        };
        add(formPanel);
        formPanel.addTextField("firstName");
        formPanel.addTextField("lastName");
        // formPanel.addEmailTextField("email");
        // formPanel.addPasswordTextField("password");
    }
}

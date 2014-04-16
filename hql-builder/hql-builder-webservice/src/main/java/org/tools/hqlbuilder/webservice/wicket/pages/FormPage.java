package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.UserData;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;

@MountedPage("/public/form")
public class FormPage extends DefaultWebPage {
    private static final long serialVersionUID = 264876407045636533L;

    public FormPage(PageParameters parameters) {
        super(parameters);

        FormPanel<UserData> formPanel = new FormPanel<UserData>("userdata.form", Model.of(new UserData()));
        add(formPanel);
        formPanel.addTextField("firstName");
        formPanel.addTextField("lastName");
        formPanel.addEmailTextField("email");
    }
}

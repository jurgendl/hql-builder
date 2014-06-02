package org.tools.hqlbuilder.webservice.wicket.pages;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.create;
import static org.tools.hqlbuilder.webservice.wicket.WebHelper.name;

import java.io.Serializable;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tools.hqlbuilder.test.Registration;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormComponentSettings;

@MountedPage("/form/registration")
public class RegistrationPage extends DefaultWebPage {
    private static final long serialVersionUID = 264876407045636533L;

    @SpringBean
    protected transient HqlWebServiceClient hqlWebClient;

    @SpringBean
    protected transient PasswordEncoder passwordEncoder;

    public RegistrationPage(PageParameters parameters) {
        super(parameters);

        DefaultFormActions<Registration> formActions = new DefaultFormActions<Registration>() {
            private static final long serialVersionUID = 8800675930559925368L;

            @Override
            public void submit(IModel<Registration> model) {
                Registration object = model.getObject();
                object.setPassword(passwordEncoder.encode(object.getPassword()));
                object.setVerification(new LocalDateTime());
                Serializable id = hqlWebClient.save(object);
                object = hqlWebClient.get(object.getClass(), id);
                model.setObject(object);
            }

            @Override
            public boolean isAjax() {
                return false;
            }
        };
        FormPanel<Registration> formPanel = new FormPanel<Registration>("userdata.form", Registration.class, true, formActions);
        add(formPanel);

        Registration proxy = create(Registration.class);

        formPanel.addTextField(name(proxy.getUsername()), new FormComponentSettings(true));
        formPanel.addTextField(name(proxy.getFirstName()), new FormComponentSettings(true));
        formPanel.addTextField(name(proxy.getLastName()), new FormComponentSettings(true));
        formPanel.addEmailTextField(name(proxy.getEmail()), new FormComponentSettings(true));
        formPanel.addDatePicker(name(proxy.getDateOfBirth()), new FormComponentSettings(false));
        formPanel.addPasswordTextField(name(proxy.getPassword()), new FormComponentSettings(true));
    }
}

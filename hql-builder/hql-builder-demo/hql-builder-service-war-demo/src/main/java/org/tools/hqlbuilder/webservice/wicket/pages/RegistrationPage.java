package org.tools.hqlbuilder.webservice.wicket.pages;

import static org.tools.hqlbuilder.common.CommonUtils.proxy;

import java.io.Serializable;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tools.hqlbuilder.demo.Registration;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;
import org.tools.hqlbuilder.webservice.wicket.forms.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormSettings;
import org.wicketstuff.annotation.mount.MountPath;

@SuppressWarnings("serial")
@MountPath("/form/registration")
public class RegistrationPage extends BasePage {
    private static final long serialVersionUID = 1L;

	@SpringBean
    protected transient HqlWebServiceClient hqlWebClient;

    @SpringBean
    protected transient PasswordEncoder passwordEncoder;

    public RegistrationPage(PageParameters parameters) {
        super(parameters);

        DefaultFormActions<Registration> formActions = new DefaultFormActions<Registration>() {
            private static final long serialVersionUID = 1L;

			@Override
			public Registration submitModel(IModel<Registration> model) {
                Registration object = model.getObject();
                object.setPassword(passwordEncoder.encode(object.getPassword()));
                object.setVerification(new LocalDateTime());
                Serializable id = hqlWebClient.save(object);
                object = hqlWebClient.get(object.getClass(), id);
                model.setObject(object);
				return object;
            }
        };
        FormPanel<Registration> formPanel = new FormPanel<>("registrationform", formActions, new FormSettings().setInheritId(true));
        add(formPanel);

        Registration proxy = proxy(Registration.class);
        formPanel.addTextField(proxy.getUsername(), new FormElementSettings(true));
        formPanel.addTextField(proxy.getFirstName(), new FormElementSettings(true));
        formPanel.addTextField(proxy.getLastName(), new FormElementSettings(true));
        formPanel.addEmailTextField(proxy.getEmail(), new FormElementSettings(true));
        formPanel.addDatePicker(proxy.getDateOfBirth(), new FormElementSettings(false));
        formPanel.addPasswordTextField(proxy.getPassword(), new FormElementSettings(true));
    }
}

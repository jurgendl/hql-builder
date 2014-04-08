package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.security.core.Authentication;
import org.tools.hqlbuilder.webservice.security.SecurityConstants;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

public class LogInPage extends DefaultWebPage {
    private static final long serialVersionUID = -959095871171401454L;

    public LogInPage(PageParameters parameters) {
        super(parameters);

        Authentication authentication = WicketApplication.getSecurityContext().getAuthentication();

        LoginForm loginFormDTO = new LoginForm();
        Model<LoginForm> loginFormModel = Model.of(loginFormDTO);
        StatelessForm<LoginForm> loginForm = new StatelessForm<LoginForm>("f", loginFormModel) {
            private static final long serialVersionUID = 7477208925532392960L;

            @Override
            protected CharSequence getActionUrl() {
                return getRequest().getContextPath() + SecurityConstants.$LOGIN$;
            }
        };

        {
            TextField<LoginForm> username = new TextField<LoginForm>("username");
            PasswordTextField password = new PasswordTextField("password");
            Button login = new Button("login");

            loginForm.add(username.setMarkupId(username.getId()));
            loginForm.add(password.setMarkupId(password.getId()));
            loginForm.add(login.setMarkupId(login.getId()));
            add(loginForm.setMarkupId(loginForm.getId()));

            loginForm.setVisible(authentication == null);
        }

        {
            ExternalLink logout = new ExternalLink("logout", getRequest().getContextPath() + SecurityConstants.$LOGOUT$);
            add(logout);

            Label username = new Label("username", authentication == null ? "?" : authentication.getName());
            add(username);

            logout.setVisible(authentication != null);
        }
    }
}

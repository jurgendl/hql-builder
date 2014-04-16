package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.Properties;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.springframework.security.core.Authentication;
import org.tools.hqlbuilder.webservice.wicket.UserData;

public class LogInPanel extends Panel {
    private static final long serialVersionUID = -3020334357804419643L;

    public LogInPanel(final Authentication authentication, final Properties securityProperties) {
        this("loginpanel", Model.of(new UserData()), authentication, securityProperties);
        setOutputMarkupPlaceholderTag(false);
        setRenderBodyOnly(true);
        setOutputMarkupId(false);
    }

    public LogInPanel(final String id, final IModel<UserData> model, final Authentication authentication, final Properties securityProperties) {
        super(id, model);
        add(new LogInForm(authentication, securityProperties));
        setVisible(authentication == null || authentication.getPrincipal().equals(securityProperties.getProperty("anonymous.user")));
    }

    public class LogInForm extends StatelessForm<UserData> {
        private static final long serialVersionUID = -201240593887067454L;

        protected final Properties securityProperties;

        public LogInForm(@SuppressWarnings("unused") final Authentication authentication, final Properties securityProperties) {
            super("loginform", Model.of(new UserData()));
            this.securityProperties = securityProperties;
            TextField<String> username = new TextField<String>("username") {
                private static final long serialVersionUID = -3917878971011538022L;

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    tag.getAttributes().put("placeholder", getString("username.placeholder"));
                }
            };
            PasswordTextField password = new PasswordTextField("password") {
                private static final long serialVersionUID = -3917878971011538022L;

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    tag.getAttributes().put("placeholder", getString("password.placeholder"));
                }
            };
            Button login = new Button("login", new ResourceModel("login.label"));
            Button reset = new Button("reset", new ResourceModel("reset.label"));
            add(new Label("username.label", new ResourceModel("username.label")));
            add(new Label("password.label", new ResourceModel("password.label")));
            add(username.setMarkupId(username.getId()));
            add(password.setMarkupId(password.getId()));
            add(login.setMarkupId(login.getId()));
            add(reset.setMarkupId(reset.getId()));
            // Label usernamelabel = new Label("knownusername", authentication == null ? securityProperties.getProperty("anonymous.user") :
            // authentication.getName());
            // usernamelabel.setVisible(authentication != null);
            // add(usernamelabel);
            BookmarkablePageLink<String> register = new BookmarkablePageLink<String>("register", RegisterPage.class);
            register.add(new Label("register.label", new ResourceModel("register.label")));
            add(register.setMarkupId(register.getId()));
            setMarkupId(getId());
        }

        @Override
        protected CharSequence getActionUrl() {
            return getRequest().getContextPath() + securityProperties.getProperty("login")
            // + "?"
            // + securityProperties.getProperty("returnto.propertyname") + "="
            // + ((org.apache.wicket.protocol.http.servlet.ServletWebRequest) getRequest()).getContainerRequest().getRequestURL()
            // + super.getActionUrl()
            ;
        }
    }
}
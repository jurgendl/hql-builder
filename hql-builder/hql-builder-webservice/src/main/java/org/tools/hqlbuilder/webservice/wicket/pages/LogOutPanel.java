package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.Properties;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.springframework.security.core.Authentication;

public class LogOutPanel extends Panel {
    private static final long serialVersionUID = -6245651530312025190L;

    public LogOutPanel(final Authentication authentication, final Properties securityProperties) {
        this("logoutpanel", Model.of(new UserData()), authentication, securityProperties);
    }

    public LogOutPanel(final String id, final IModel<UserData> model, final Authentication authentication, final Properties securityProperties) {
        super(id, model);
        add(new LogOutForm(authentication, securityProperties));
    }

    public class LogOutForm extends StatelessForm<UserData> {
        private static final long serialVersionUID = -8305944845978935182L;

        protected final Properties securityProperties;

        public LogOutForm(final Authentication authentication, final Properties securityProperties) {
            super("logoutform", Model.of(new UserData()));
            this.securityProperties = securityProperties;
            Button logout = new Button("logout", new ResourceModel("logout.label"));
            Label logoutQuestion = new Label("logout.question", new AbstractReadOnlyModel<String>() {
                private static final long serialVersionUID = 40702564365319274L;

                @Override
                public String getObject() {
                    return String.format(getString("logout.question"), authentication == null ? "?" : authentication.getName());
                }
            });
            add(logoutQuestion);
            add(logout.setMarkupId(logout.getId()));
            setMarkupId(getId());
            setVisible(authentication != null);
        }

        @Override
        protected CharSequence getActionUrl() {
            return getRequest().getContextPath() + securityProperties.getProperty("logout");
        }
    }
}
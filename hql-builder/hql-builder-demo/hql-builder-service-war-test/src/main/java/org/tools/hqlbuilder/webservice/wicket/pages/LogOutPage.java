package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.security.core.Authentication;
import org.tools.hqlbuilder.webservice.security.SecurityConstants;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

@MountedPage(SecurityConstants.$LOGOUTURL$)
public class LogOutPage extends DefaultWebPage {
    private static final long serialVersionUID = -1844173741599209281L;

    public LogOutPage(PageParameters parameters) {
        super(parameters);

        final Authentication authentication = WicketApplication.getSecurityContext().getAuthentication();

        ExternalLink logout = new ExternalLink("logout", getRequest().getContextPath() + SecurityConstants.$LOGOUT$, getString("logout.label"));
        add(logout);

        Label username = new Label("logout.question", new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = 40702564365319274L;

            @Override
            public String getObject() {
                return String.format(getString("logout.question"), authentication == null ? SecurityConstants.$ANON$ : authentication.getName());
            }
        });
        add(username);
    }
}

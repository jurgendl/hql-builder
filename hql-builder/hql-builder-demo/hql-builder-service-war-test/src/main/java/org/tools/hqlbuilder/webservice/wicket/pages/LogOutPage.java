package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.Properties;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.Authentication;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

@MountedPage("${wicket.logout.mount}")
public class LogOutPage extends DefaultWebPage {
    private static final long serialVersionUID = -1844173741599209281L;

    @SpringBean(name = "securityProperties")
    private Properties securityProperties;

    public LogOutPage(PageParameters parameters) {
        super(parameters);
        addLogoutComponents(securityProperties, this);
    }

    public static ExternalLink addLogoutComponents(Properties securityProperties, final WebPage page) {
        final Authentication authentication = WicketApplication.getSecurityContext().getAuthentication();

        ExternalLink logout = new ExternalLink("logout", page.getRequest().getContextPath() + securityProperties.getProperty("logout"),
                page.getString("logout.label"));
        page.add(logout);

        Label username = new Label("logout.question", new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = 40702564365319274L;

            @Override
            public String getObject() {
                return String.format(page.getString("logout.question"), authentication == null ? "?" : authentication.getName());
            }
        });
        page.add(username);

        return logout;
    }
}

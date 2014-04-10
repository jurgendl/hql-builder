package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.Properties;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.Authentication;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

@MountedPage("${wicket.login.mount}")
public class LogInPage extends DefaultWebPage {
    private static final long serialVersionUID = -959095871171401454L;

    @SpringBean(name = "securityProperties")
    private Properties securityProperties;

    public LogInPage(PageParameters parameters) {
        super(parameters);
        Authentication authentication = WicketApplication.getSecurityContext().getAuthentication();
        add(new LogInPanel(authentication, securityProperties));
        add(new LogOutPanel(authentication, securityProperties));
    }
}

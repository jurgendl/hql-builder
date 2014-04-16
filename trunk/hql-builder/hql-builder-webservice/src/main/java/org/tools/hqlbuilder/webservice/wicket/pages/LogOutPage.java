package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.security.core.Authentication;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

@MountedPage("${wicket.logout.mount}")
public class LogOutPage extends DefaultWebPage {
    private static final long serialVersionUID = -1844173741599209281L;

    public LogOutPage(PageParameters parameters) {
        super(parameters);
        setStatelessHint(true);
    }

    @Override
    protected void addComponents() {
        super.addComponents();
        Authentication authentication = WicketApplication.getSecurityContext().getAuthentication();
        add(new LogOutPanel(authentication, securityProperties));
    }

    @Override
    public boolean showAuthLinks() {
        return false;
    }
}

package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.Properties;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
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
        add(new LogOutPanel(WicketApplication.getSecurityContext().getAuthentication(), securityProperties));
    }
}

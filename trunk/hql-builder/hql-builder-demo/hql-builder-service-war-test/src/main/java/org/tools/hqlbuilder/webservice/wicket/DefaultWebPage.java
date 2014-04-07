package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.security.core.Authentication;

public class DefaultWebPage extends WebPage {
    private static final long serialVersionUID = -9203251110723359467L;

    public DefaultWebPage(PageParameters parameters) {
        super(parameters);

        Authentication authentication = WicketApplication.getSecurityContext().getAuthentication();
        add(new Label("username", authentication.getName()));
        add(new Label("authorities", String.valueOf(authentication.getAuthorities())));
        add(new ExternalLink("logout", getRequest().getContextPath() + WicketApplication.getSpringContext().getBean("logOutUrl")));
    }
}
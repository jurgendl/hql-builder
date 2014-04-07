package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class WicketApplication extends WebApplication {
    public static WicketApplication get() {
        return (WicketApplication) WebApplication.get();
    }

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    @Override
    public Class<DefaultWebPage> getHomePage() {
        return DefaultWebPage.class;
    }

    @Override
    protected void init() {
        super.init();
    }
}
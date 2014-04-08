package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.tools.hqlbuilder.webservice.security.SecurityConstants;
import org.tools.hqlbuilder.webservice.wicket.pages.LogInPage;
import org.tools.hqlbuilder.webservice.wicket.pages.LogOutPage;

public class WicketApplication extends WebApplication {
    public static WicketApplication get() {
        return WicketApplication.class.cast(WebApplication.get());
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
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        mountPage(SecurityConstants.$LOGOUTURL$, LogOutPage.class);
        mountPage(SecurityConstants.$LOGINURL$, LogInPage.class);
    }
}
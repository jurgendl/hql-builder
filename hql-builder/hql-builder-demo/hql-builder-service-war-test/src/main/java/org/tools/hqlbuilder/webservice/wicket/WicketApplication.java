package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class WicketApplication extends WebApplication implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public static WicketApplication get() {
        return WicketApplication.class.cast(WebApplication.get());
    }

    public static ApplicationContext getSpringContext() {
        return get().getApplicationContext();
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }
}
package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.settings.IMarkupSettings;
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

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.request.Request, org.apache.wicket.request.Response)
     */
    @Override
    public Session newSession(Request request, Response response) {
        return new WicketSession(request);
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<DefaultWebPage> getHomePage() {
        return DefaultWebPage.class;
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#init()
     */
    @Override
    protected void init() {
        super.init();

        getComponentInstantiationListeners().add(new SpringComponentInjector(this));

        IMarkupSettings markupSettings = getMarkupSettings();
        markupSettings.setStripComments(getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT ? true : false);
        markupSettings.setCompressWhitespace(getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT ? true : false);

        mountPage(SecurityConstants.$LOGOUTURL$, LogOutPage.class);
        mountPage(SecurityConstants.$LOGINURL$, LogInPage.class);
    }
}
package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.protocol.http.WebApplication;

public class WicketApplication extends WebApplication {
    @Override
    public Class<DefaultWebPage> getHomePage() {
        return DefaultWebPage.class;
    }

    @Override
    protected void init() {
        super.init();
    }
}
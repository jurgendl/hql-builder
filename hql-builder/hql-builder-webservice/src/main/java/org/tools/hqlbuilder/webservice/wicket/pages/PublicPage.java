package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;

@MountedPage("/public")
public class PublicPage extends DefaultWebPage {
    private static final long serialVersionUID = 9191797529541011553L;

    public PublicPage(PageParameters parameters) {
        super(parameters);
        setStatelessHint(true);
    }
}

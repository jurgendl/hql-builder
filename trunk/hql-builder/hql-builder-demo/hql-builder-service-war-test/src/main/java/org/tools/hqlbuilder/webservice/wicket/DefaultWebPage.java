package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class DefaultWebPage extends WebPage {
    private static final long serialVersionUID = -9203251110723359467L;

    public DefaultWebPage(PageParameters parameters) {
        super(parameters);

        add(new Label("username", WicketApplication.getSecurityContext().getAuthentication().getName()));
    }
}
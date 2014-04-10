package org.tools.hqlbuilder.webservice.wicket;

import java.util.Properties;

import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.Authentication;
import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.wicket.pages.LogInPanel;
import org.tools.hqlbuilder.webservice.wicket.pages.LogOutPanel;

import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;

public class DefaultWebPage extends WebPage {
    private static final long serialVersionUID = -9203251110723359467L;

    @SpringBean(name = "securityProperties")
    private Properties securityProperties;

    public static WicketSession getWebSession() {
        return WicketSession.class.cast(Session.get());
    }

    public DefaultWebPage(PageParameters parameters) {
        super(parameters);
        Injector.get().inject(this);

        Authentication authentication = WicketApplication.getSecurityContext().getAuthentication();
        add(new LogInPanel(authentication, securityProperties));
        add(new LogOutPanel(authentication, securityProperties));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        {
            IJQueryLibrarySettings javaScriptSettings = (IJQueryLibrarySettings) getApplication().getJavaScriptLibrarySettings();
            response.render(JavaScriptHeaderItem.forReference(javaScriptSettings.getJQueryGlobalizeReference()));
            response.render(JavaScriptHeaderItem.forReference(javaScriptSettings.getJQueryReference()));
            response.render(JavaScriptHeaderItem.forReference(javaScriptSettings.getJQueryUIReference()));
            response.render(JavaScriptHeaderItem.forReference(javaScriptSettings.getWicketAjaxDebugReference()));
            response.render(JavaScriptHeaderItem.forReference(javaScriptSettings.getWicketAjaxReference()));
            response.render(JavaScriptHeaderItem.forReference(javaScriptSettings.getWicketEventReference()));
        }
        {
            response.render(CssHeaderItem.forReference(new CssResourceReference(WicketRoot.class, "css/hqlbuilder.css")));
            response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(WicketRoot.class, "js/hqlbuilder.js")));
        }
    }
}
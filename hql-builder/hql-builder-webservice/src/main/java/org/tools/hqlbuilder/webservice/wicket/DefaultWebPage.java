package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.Session;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.WicketRoot;

import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;

public class DefaultWebPage extends WebPage {
    private static final long serialVersionUID = -9203251110723359467L;

    public static WicketSession getWebSession() {
        return WicketSession.class.cast(Session.get());
    }

    public DefaultWebPage(PageParameters parameters) {
        super(parameters);
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
            response.render(CssHeaderItem.forReference(new CssResourceReference(WicketRoot.class, "css/main.css")));
            response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(WicketRoot.class, "js/main.js")));
        }
    }
}
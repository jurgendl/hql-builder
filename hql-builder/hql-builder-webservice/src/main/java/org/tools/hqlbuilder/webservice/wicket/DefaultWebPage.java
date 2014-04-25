package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.settings.IJavaScriptLibrarySettings;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.WicketRoot;

import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;

public class DefaultWebPage extends WebPage {
    private static final long serialVersionUID = -9203251110723359467L;

    protected transient final Logger logger;

    public DefaultWebPage(PageParameters parameters) {
        super(parameters);
        logger = LoggerFactory.getLogger(getClass());
        Injector.get().inject(this);
        addComponents();
    }

    protected void addComponents() {
        if (getApplication().getDebugSettings().isDevelopmentUtilitiesEnabled()) {
            add(new DebugBar("debug"));
        } else {
            add(new EmptyPanel("debug").setVisible(false));
        }
        // add(new LocalesPanel("localespanel", Arrays.asList(Locale.ENGLISH, new Locale("nl"))));
        // add(new LogInOutLinksPanel("authlinkspanel", securityProperties, showAuthLinks()));
    }

    public boolean showAuthLinks() {
        return true;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        addDefaultResources(response);
        addResources(response);
    }

    protected void addResources(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(new CssResourceReference(WicketRoot.class, "css/hqlbuilder.css")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(WicketRoot.class, "js/hqlbuilder.js")));
    }

    protected void addDefaultResources(IHeaderResponse response) {
        IJavaScriptLibrarySettings javaScriptLibrarySettings = getApplication().getJavaScriptLibrarySettings();
        response.render(JavaScriptHeaderItem.forReference(javaScriptLibrarySettings.getJQueryReference()));
        response.render(JavaScriptHeaderItem.forReference(javaScriptLibrarySettings.getWicketAjaxDebugReference()));
        response.render(JavaScriptHeaderItem.forReference(javaScriptLibrarySettings.getWicketAjaxReference()));
        response.render(JavaScriptHeaderItem.forReference(javaScriptLibrarySettings.getWicketEventReference()));
        if (javaScriptLibrarySettings instanceof IJQueryLibrarySettings) {
            IJQueryLibrarySettings javaScriptSettings = (IJQueryLibrarySettings) javaScriptLibrarySettings;
            response.render(JavaScriptHeaderItem.forReference(javaScriptSettings.getJQueryGlobalizeReference()));
            response.render(JavaScriptHeaderItem.forReference(javaScriptSettings.getJQueryUIReference()));
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (getStatelessHint()) {
            visitChildren(new IVisitor<Component, Void>() {
                @Override
                public void component(Component component, IVisit<Void> arg1) {
                    if (!component.isStateless()) {
                        logger.error("Component " + component.getId() + " is not stateless");
                    }
                }
            });
        }
    }
}
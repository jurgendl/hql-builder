package org.tools.hqlbuilder.webservice.wicket;

import java.util.Arrays;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.wicket.pages.LocalesPanel;

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
        if (WicketApplication.get().usesDevelopmentConfig()) {
            add(new DebugBar("debug"));
        } else {
            add(new EmptyPanel("debug").setVisible(false));
        }
        add(new LocalesPanel("localespanel", Arrays.asList(Locale.ENGLISH, new Locale("nl"))));
        // add(new LogInOutLinksPanel("authlinkspanel", webProperties, showAuthLinks()));
        add(new HeaderResponseContainer("footer-container", "footer-bucket"));
    }

    public boolean showAuthLinks() {
        return true;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        addDefaultResources(response);
        addThemeResources(response);
        addPageResources(response);
        addUserResources(response);
    }

    protected void addUserResources(@SuppressWarnings("unused") IHeaderResponse response) {
        // none by default
    }

    protected void addPageResources(@SuppressWarnings("unused") IHeaderResponse response) {
        // none by default
    }

    protected void addThemeResources(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(new CssResourceReference(WicketRoot.class, "jquery/ui/jquery-ui-themes-1.10.4/themes/"
                + WicketSession.get().getJQueryUITheme() + "/jquery-ui.css")));
        response.render(CssHeaderItem.forReference(new CssResourceReference(WicketRoot.class, "jquery/ui/jquery-ui-themes-1.10.4/themes/"
                + WicketSession.get().getJQueryUITheme() + "/jquery.ui.theme.css")));
    }

    protected void addDefaultResources(IHeaderResponse response) {
        for (ResourceReference resource : WicketApplication.get().getCssResources()) {
            response.render(CssHeaderItem.forReference(CssResourceReference.class.cast(resource)));
        }
        for (ResourceReference resource : WicketApplication.get().getJsResources()) {
            response.render(JavaScriptHeaderItem.forReference(JavaScriptResourceReference.class.cast(resource)));
        }
        // if (WicketApplication.get().getJsBundleReference() != null) {
        // response.render(WicketApplication.get().getJsBundleReference());
        // }
        // if (WicketApplication.get().getCssBundleReference() != null) {
        // response.render(WicketApplication.get().getCssBundleReference());
        // }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (getStatelessHint() && WicketApplication.get().usesDevelopmentConfig()) {
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

    @Override
    protected void setHeaders(WebResponse response) {
        if (isPageStateless()) {
            response.enableCaching(Duration.ONE_DAY, WebResponse.CacheScope.PUBLIC);
        } else {
            response.disableCaching();
        }
    }
}
package org.tools.hqlbuilder.webservice.wicket;

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
import org.tools.hqlbuilder.webservice.jquery.WicketJQueryRoot;

public class DefaultWebPage extends WebPage {
    private static final long serialVersionUID = -9203251110723359467L;

    protected transient final Logger logger;

    protected Duration defaultCacheDuration = Duration.ONE_DAY;

    protected boolean debugbar = false;

    public DefaultWebPage(PageParameters parameters) {
        super(parameters);

        setStatelessHint(false);

        if (getClass().equals(DefaultWebPage.class)) {
            setResponsePage(EmptyPage.class, parameters);
        }

        logger = LoggerFactory.getLogger(getClass());
        Injector.get().inject(this);
        addComponents();
    }

    protected void addComponents() {
        if (debugbar && WicketApplication.get().usesDevelopmentConfig()) {
            add(new DebugBar("debug"));
        } else {
            add(new EmptyPanel("debug").setVisible(false));
        }
        add(new HeaderResponseContainer("footer-container", "footer-bucket"));
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
        addDynamicResources(response);
    }

    protected void addUserResources(@SuppressWarnings("unused") IHeaderResponse response) {
        // none by default
    }

    protected void addPageResources(@SuppressWarnings("unused") IHeaderResponse response) {
        // none by default
    }

    /**
     * add jquery theme resources
     *
     * @see {@link WicketSession#getJQueryUITheme()}
     */
    protected void addThemeResources(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(new CssResourceReference(WicketJQueryRoot.class, "ui/jquery-ui-themes-1.10.4/themes/"
                + WicketSession.get().getJQueryUITheme() + "/jquery-ui.css")));
        response.render(CssHeaderItem.forReference(new CssResourceReference(WicketJQueryRoot.class, "ui/jquery-ui-themes-1.10.4/themes/"
                + WicketSession.get().getJQueryUITheme() + "/jquery.ui.theme.css")));
    }

    protected void addDefaultResources(IHeaderResponse response) {
        for (ResourceReference resource : WicketApplication.get().getCssResources()) {
            response.render(CssHeaderItem.forReference(CssResourceReference.class.cast(resource)));
        }
        for (ResourceReference resource : WicketApplication.get().getJsResources()) {
            response.render(JavaScriptHeaderItem.forReference(JavaScriptResourceReference.class.cast(resource)));
        }
        if (WicketApplication.get().getJsBundleReference() != null) {
            response.render(WicketApplication.get().getJsBundleReference());
        }
        if (WicketApplication.get().getCssBundleReference() != null) {
            response.render(WicketApplication.get().getCssBundleReference());
        }
    }

    protected void addDynamicResources(@SuppressWarnings("unused") IHeaderResponse response) {
        //
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        statelessCheck();
    }

    protected void statelessCheck() {
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
        super.setHeaders(response);

        // if page is stateless en in deployment mode, we enable caching
        if (isPageStateless() && WicketApplication.get().usesDeploymentConfig()) {
            enableCaching(response);
        } else {
            disableCaching(response);
        }
    }

    protected void disableCaching(WebResponse response) {
        response.disableCaching();
    }

    protected void enableCaching(WebResponse response) {
        response.enableCaching(defaultCacheDuration, WebResponse.CacheScope.PUBLIC);
    }

    public Duration getDefaultCacheDuration() {
        return this.defaultCacheDuration;
    }

    public void setDefaultCacheDuration(Duration defaultCacheDuration) {
        this.defaultCacheDuration = defaultCacheDuration;
    }
}
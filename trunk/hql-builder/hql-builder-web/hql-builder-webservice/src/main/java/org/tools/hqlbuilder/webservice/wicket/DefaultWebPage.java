package org.tools.hqlbuilder.webservice.wicket;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryuithemes.JQueryUIThemes;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

public class DefaultWebPage extends WebPage {
    private static final long serialVersionUID = -9203251110723359467L;

    protected transient final Logger logger;

    /** default cache duration when deployed: 1 day */
    protected Duration defaultCacheDuration = Duration.ONE_DAY;

    public DefaultWebPage(PageParameters parameters) {
        super(parameters);
        setStatelessHint(false);
        logger = LoggerFactory.getLogger(getClass());
        Injector.get().inject(this);
        if (getClass().equals(DefaultWebPage.class)) {
            setResponsePage(EmptyPage.class, parameters);
        }
        addComponents();
    }

    protected void addComponents() {
        // site uses cookies info (asked when user choice not known)
        WebMarkupContainer cookiesQ = new WebMarkupContainer("nocookies");
        cookiesQ.add(new Link<String>("allowCookies") {
            private static final long serialVersionUID = -8778073423020169707L;

            @Override
            public void onClick() {
                WicketSession.get().getCookies().setUserAllowedCookies(true);
                DefaultWebPage.this.get("nocookies").setVisible(false);
            }
        });
        cookiesQ.add(new Link<String>("disallowCookies") {
            private static final long serialVersionUID = 4600682011663940306L;

            @Override
            public void onClick() {
                WicketSession.get().getCookies().setUserAllowedCookies(false);
                DefaultWebPage.this.get("nocookies").setVisible(false);
            }
        });
        add(cookiesQ.setVisible(WicketSession.get().getCookies().getUserAllowedCookies() == null));
        // wicket/ajav debug bars
        add(WicketApplication.get().isShowDebugbars() && WicketApplication.get().usesDevelopmentConfig() ? new DebugBar("debug") : new EmptyPanel(
                "debug").setVisible(false));
        // add header response (javascript) down below on page
        add(new HeaderResponseContainer("footer-container", "footer-bucket"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(WicketCSSRoot.NORMALIZE));
        super.renderHead(response);

        if (!isEnabledInHierarchy()) {
            return;
        }

        response.render(CssHeaderItem.forReference(WicketCSSRoot.CLEARFIX));
        response.render(CssHeaderItem.forReference(WicketCSSRoot.GENERAL));

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
     * add jquery-ui/prime-ui theme resources
     *
     * @see {@link WicketSession#getJQueryUITheme()}
     */
    protected void addThemeResources(IHeaderResponse response) {
        String jQueryUITheme = WicketSession.get().getJQueryUITheme();
        if (StringUtils.isNotBlank(jQueryUITheme) && JQueryUIThemes.getThemes().contains(jQueryUITheme)) {
            response.render(CssHeaderItem.forReference(JQueryUIThemes.base(jQueryUITheme)));
            response.render(CssHeaderItem.forReference(JQueryUIThemes.theme(jQueryUITheme)));
        } else {
            response.render(CssHeaderItem.forReference(JQueryUI.JQUERY_UI_CSS));
            response.render(CssHeaderItem.forReference(JQueryUI.JQUERY_UI_THEME_CSS));
        }
        {
            response.render(CssHeaderItem.forReference(PrimeUI.PRIME_UI_CSS));
            if (StringUtils.isNotBlank(jQueryUITheme)) {
                response.render(CssHeaderItem.forReference(PrimeUI.forJQueryUITheme(jQueryUITheme)));
            }
        }
    }

    protected void addDefaultResources(@SuppressWarnings("unused") IHeaderResponse response) {
        //
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
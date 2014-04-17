package org.tools.hqlbuilder.webservice.wicket;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.tools.hqlbuilder.webservice.WicketRoot;

import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;

public class DefaultWebPage extends WebPage {
    private static final long serialVersionUID = -9203251110723359467L;

    public DefaultWebPage(PageParameters parameters) {
        super(parameters);
        Injector.get().inject(this);
        addComponents();
    }

    protected void addComponents() {
        // add(new DebugBar("debug"));
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
        IJQueryLibrarySettings javaScriptSettings = (IJQueryLibrarySettings) getApplication().getJavaScriptLibrarySettings();
        response.render(JavaScriptHeaderItem.forReference(javaScriptSettings.getJQueryGlobalizeReference()));
        response.render(JavaScriptHeaderItem.forReference(javaScriptSettings.getJQueryReference()));
        response.render(JavaScriptHeaderItem.forReference(javaScriptSettings.getJQueryUIReference()));
        response.render(JavaScriptHeaderItem.forReference(javaScriptSettings.getWicketAjaxDebugReference()));
        response.render(JavaScriptHeaderItem.forReference(javaScriptSettings.getWicketAjaxReference()));
        response.render(JavaScriptHeaderItem.forReference(javaScriptSettings.getWicketEventReference()));
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        statelessCheck();
    }

    protected void statelessCheck() {
        if (!getSession().isTemporary()) {
            visitChildren(Component.class, new IVisitor<Component, String>() {
                @Override
                public void component(Component component, IVisit<String> visit) {
                    if (!component.isStateless()) {
                        String msg = getPage().getClass().getName() + " is stateful because of stateful component " + component.getClass().getName()
                                + " with id " + component.getId() + ".";
                        List<? extends Behavior> behaviourList = component.getBehaviors();
                        for (Behavior iBehavior : behaviourList) {
                            if (!iBehavior.getStatelessHint(component)) {
                                msg += "\n\t" + "The component has stateful behaviour: " + iBehavior.getClass().getName();
                            }
                        }
                        throw new RuntimeException(msg);
                    }
                }
            });
        }
    }
}
package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.jquery.ui.stickynavbar.StickyNavbar;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.WeLoveIcons;
import org.tools.hqlbuilder.webservice.wicket.less.LessResourceReference;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/layout")
public class LayoutPage extends BasePage {
    private static final long serialVersionUID = 3459253460686535846L;

    public LayoutPage(PageParameters parameters) {
        super(parameters);
        add(new BookmarkablePageLink<String>("link1", getClass()));
        add(new BookmarkablePageLink<String>("link2", getClass()));
    }

    @Override
    protected Component addMenu() {
        return super.addMenu().setVisible(false);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(JQueryUI.JQUERY_UI_FACTORY_JS));
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_FACTORY_JS));
        response.render(CssHeaderItem.forReference(new LessResourceReference(WicketCSSRoot.class, "layout.css")));
        response.render(JavaScriptHeaderItem.forReference(StickyNavbar.STICKY_NAVBAR_JS));
        // response.render(OnDomReadyHeaderItem.forScript("$('.stickied').stickyNavbar();"));
        response.render(CssHeaderItem.forReference(WeLoveIcons.WE_LOVE_ICONS_SOCIAL_CSS));
    }
}

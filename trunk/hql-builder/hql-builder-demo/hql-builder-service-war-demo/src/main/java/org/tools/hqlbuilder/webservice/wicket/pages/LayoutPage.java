package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;
import org.tools.hqlbuilder.webservice.wicket.less.LessResourceReference;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/layout")
public class LayoutPage extends BasePage {
    private static final long serialVersionUID = 3459253460686535846L;

    public LayoutPage(PageParameters parameters) {
        super(parameters);
        add(new BookmarkablePageLink<String>("link", getClass()));
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
        response.render(CssHeaderItem.forReference(new LessResourceReference(WicketCSSRoot.class, "layout.css")));
    }
}

package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/layout")
public class LayoutPage extends BasePage {
    private static final long serialVersionUID = 3459253460686535846L;

    public LayoutPage(PageParameters parameters) {
        super(parameters);
        add(new BookmarkablePageLink<String>("link", getClass()));
    }
}

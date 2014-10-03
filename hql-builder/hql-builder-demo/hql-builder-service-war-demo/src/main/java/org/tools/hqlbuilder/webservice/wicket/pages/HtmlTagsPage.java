package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/htmltags")
public class HtmlTagsPage extends BasePage {
    private static final long serialVersionUID = 7581477788877598725L;

    public HtmlTagsPage(PageParameters parameters) {
        super(parameters);
        add(new HtmlTagsPanel("htmltags"));
    }
}

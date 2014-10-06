package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.WeLoveIcons;

public class HtmlTagsPanel extends Panel {
    private static final long serialVersionUID = -4579037332013628793L;

    public HtmlTagsPanel(String id) {
        super(id);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(CssHeaderItem.forReference(WeLoveIcons.WE_LOVE_ICONS_CSS));
    }
}

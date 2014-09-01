package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.components.SocialPanel;
import org.tools.hqlbuilder.webservice.wicket.components.SocialPanel.Social;
import org.tools.hqlbuilder.webservice.wicket.components.SocialPanelSettings;

@MountedPage("/social")
@SuppressWarnings("serial")
public class SocialPage extends DefaultWebPage {
    public SocialPage(PageParameters parameters) {
        super(parameters);
        ArrayList<Social> options = new ArrayList<Social>(new TreeSet<Social>(Arrays.asList(Social.values())));
        add(new ListView<Social>("socialbuttons", options) {
            private static final long serialVersionUID = -7495456081110874114L;

            @Override
            protected void populateItem(ListItem<Social> item) {
                item.add(new SocialPanel("socialbutton", item.getModel(), new SocialPanelSettings().setBarForm(false)));
            }
        });
        add(new ListView<Social>("socialbars", options) {
            private static final long serialVersionUID = -2422255718832136362L;

            @Override
            protected void populateItem(ListItem<Social> item) {
                item.add(new SocialPanel("socialbar", item.getModel(), new SocialPanelSettings().setBarForm(true)));
            }
        });
    }
}

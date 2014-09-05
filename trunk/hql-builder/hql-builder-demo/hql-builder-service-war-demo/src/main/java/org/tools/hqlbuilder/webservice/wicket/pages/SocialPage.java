package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.WeLoveIcons.WLIFIcons;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.WeLoveIcons.WLIFont;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.components.SocialPanel;
import org.tools.hqlbuilder.webservice.wicket.components.SocialPanel.Social;
import org.tools.hqlbuilder.webservice.wicket.components.SocialPanelSettings;

@MountedPage("/social")
@SuppressWarnings("serial")
public class SocialPage extends BasePage {
    public SocialPage(PageParameters parameters) {
        super(parameters);
        ArrayList<Social> options = new ArrayList<Social>(new TreeSet<Social>(Arrays.asList(Social.values())));
        add(new ListView<Social>("socialbuttons", options) {
            @Override
            protected void populateItem(ListItem<Social> item) {
                item.add(new SocialPanel("socialbutton", item.getModel(), new SocialPanelSettings().setBarForm(false)));
            }
        });
        add(new ListView<Social>("socialbars", options) {
            @Override
            protected void populateItem(ListItem<Social> item) {
                item.add(new SocialPanel("socialbar", item.getModel(), new SocialPanelSettings().setBarForm(true)));
            }
        });
        add(new ListView<WLIFont>("icongroupparent", Arrays.asList(WLIFont.values())) {
            @Override
            protected void populateItem(ListItem<WLIFont> icongroupparent) {
                icongroupparent.add(new Label("icongroupname", icongroupparent.getModel().getObject().toString()));
                WebMarkupContainer icongroup = new WebMarkupContainer("icongroup");
                icongroupparent.add(icongroup);
                icongroup.add(new ListView<WLIFIcons>("icon", Arrays.asList(icongroupparent.getModel().getObject().getIcons())) {
                    @Override
                    protected void populateItem(ListItem<WLIFIcons> icon) {
                        icon.add(new AttributeModifier("class", icon.getModel().getObject().getCode()));
                        icon.add(new AttributeModifier("title", icon.getModel().getObject().toString()));
                    }
                });
            }
        });
    }
}

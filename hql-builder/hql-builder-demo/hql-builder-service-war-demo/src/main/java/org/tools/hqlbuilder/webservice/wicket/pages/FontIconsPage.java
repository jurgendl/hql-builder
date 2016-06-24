package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeSet;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.WeLoveIcons.WLIFIcons;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.WeLoveIcons.WLIFont;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.components.SocialPanel;
import org.tools.hqlbuilder.webservice.wicket.components.SocialPanel.Social;
import org.tools.hqlbuilder.webservice.wicket.components.SocialPanelSettings;

@MountedPage("/fonticons")
@SuppressWarnings("serial")
public class FontIconsPage extends BasePage {
    private static final long serialVersionUID = 1L;

	public FontIconsPage(PageParameters parameters) {
        super(parameters);
        Social soc = Social.values()[new Random().nextInt(Social.values().length)];
        for (int i = 1; i <= 4; i++) {
            add(new SocialPanel("socialbutton" + i, Model.of(soc), new SocialPanelSettings().setForm(SocialPanelSettings.SocialForm.button)));
            add(new SocialPanel("socialpin" + i, Model.of(soc), new SocialPanelSettings().setForm(SocialPanelSettings.SocialForm.pin)));
            add(new SocialPanel("socialbar" + i, Model.of(soc), new SocialPanelSettings().setForm(SocialPanelSettings.SocialForm.bar)));
        }
        ArrayList<Social> options = new ArrayList<>(new TreeSet<>(Arrays.asList(Social.values())));
        add(new ListView<Social>("socialbuttons", options) {
            private static final long serialVersionUID = 1L;

			@Override
            protected void populateItem(ListItem<Social> item) {
                item.add(new SocialPanel("socialbutton", item.getModel(), new SocialPanelSettings().setForm(SocialPanelSettings.SocialForm.button)));
            }
        });
        add(new ListView<Social>("socialbars", options) {
            private static final long serialVersionUID = 1L;

			@Override
            protected void populateItem(ListItem<Social> item) {
                item.add(new SocialPanel("socialbar", item.getModel(), new SocialPanelSettings().setForm(SocialPanelSettings.SocialForm.bar)));
            }
        });
        add(new ListView<Social>("socialpins", options) {
            private static final long serialVersionUID = 1L;

			@Override
            protected void populateItem(ListItem<Social> item) {
                item.add(new SocialPanel("socialpin", item.getModel(), new SocialPanelSettings().setForm(SocialPanelSettings.SocialForm.pin)));
            }
        });
        add(new ListView<WLIFont>("icongroupparent", Arrays.asList(WLIFont.values())) {
            private static final long serialVersionUID = 1L;

			@Override
            protected void populateItem(ListItem<WLIFont> icongroupparent) {
                icongroupparent.add(new Label("icongroupname", icongroupparent.getModel().getObject().toString()));
                WebMarkupContainer icongroup = new WebMarkupContainer("icongroup");
                icongroupparent.add(icongroup);
                icongroup.add(new ListView<WLIFIcons>("icon", Arrays.asList(icongroupparent.getModel().getObject().getIcons())) {
                    private static final long serialVersionUID = 1L;

					@Override
                    protected void populateItem(ListItem<WLIFIcons> icon) {
                        icon.add(new AttributeModifier("class", icon.getModel().getObject().getCode()));
                        icon.add(new AttributeModifier("title", icon.getModel().getObject().toString()));
                    }
                });
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
    }
}

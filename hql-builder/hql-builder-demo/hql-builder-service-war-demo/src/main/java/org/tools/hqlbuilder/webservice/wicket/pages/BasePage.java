package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;

@SuppressWarnings("serial")
public class BasePage extends DefaultWebPage {
    public BasePage(final PageParameters parameters) {
        super(parameters);

        IModel<? extends List<? extends Class<? extends DefaultWebPage>>> pagesModel = new LoadableDetachableModel<List<? extends Class<? extends DefaultWebPage>>>() {
            @Override
            protected List<? extends Class<? extends DefaultWebPage>> load() {
                List<Class<? extends DefaultWebPage>> pages = new ArrayList<Class<? extends DefaultWebPage>>();
                pages.add(RegistrationPage.class);
                pages.add(RegistrationsPage.class);
                pages.add(SettingsPage.class);
                pages.add(StylingPage.class);
                pages.add(MultiColumnFormPage.class);
                return pages;
            }
        };
        ListView<Class<? extends DefaultWebPage>> menu = new ListView<Class<? extends DefaultWebPage>>("menu", pagesModel) {
            @Override
            protected void populateItem(ListItem<Class<? extends DefaultWebPage>> item) {
                WebMarkupContainer menuitem = new WebMarkupContainer("menuitem");
                menuitem.add(new AttributeModifier("title", new Model<String>(item.getModelObject().getSimpleName())));
                boolean active = item.getModelObject().equals(BasePage.this);
                if (active) {
                    menuitem.add(new AttributeModifier("class", "active"));
                }
                item.add(menuitem);
                BookmarkablePageLink<String> link = new BookmarkablePageLink<String>("menulink", item.getModelObject());
                menuitem.add(link);
                link.add(new Label("menulinklabel", new Model<String>(item.getModelObject().getSimpleName())));
            }
        };
        add(menu);
        // menu.setVisible(false);
    }
}

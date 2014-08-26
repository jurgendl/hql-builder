package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
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
    protected WebMarkupContainer menu;

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

        menu = new WebMarkupContainer("menu");
        add(menu);

        ListView<Class<? extends DefaultWebPage>> menuitem = new ListView<Class<? extends DefaultWebPage>>("menuitem", pagesModel) {
            @Override
            protected void populateItem(ListItem<Class<? extends DefaultWebPage>> item) {
                item.add(new AttributeModifier("title", new Model<String>(item.getModelObject().getSimpleName())));
                boolean active = item.getModelObject().equals(BasePage.this);
                if (active) {
                    item.add(new AttributeModifier("class", "active"));
                }
                BookmarkablePageLink<String> link = new BookmarkablePageLink<String>("menulink", item.getModelObject());
                item.add(link);
                link.add(new Label("menulinklabel", new Model<String>(item.getModelObject().getSimpleName())));
            }
        };
        menu.add(menuitem);

        menu.setOutputMarkupId(true);
        add(menu);
        // menu.setVisible(false);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(new JavaScriptContentHeaderItem("$(function() { $( \"#" + menu.getMarkupId() + "\" ).puimenubar(); });", "js_"
                + menu.getMarkupId() + "_" + System.currentTimeMillis(), null));
        // response.render(CssHeaderItem.forReference(new ZussResourceReference(WicketCSSRoot.class, "horizontalmenu.css")));
    }
}

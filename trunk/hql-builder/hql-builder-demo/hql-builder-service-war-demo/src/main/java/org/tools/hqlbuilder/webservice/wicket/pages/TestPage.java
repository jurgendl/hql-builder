package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.resources.prime.PrimeUI;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.components.SocialPanel;
import org.tools.hqlbuilder.webservice.wicket.components.SocialPanel.Social;
import org.tools.hqlbuilder.webservice.wicket.forms.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormSettings;

@MountedPage("/test")
public class TestPage extends DefaultWebPage {
    private static final long serialVersionUID = 1679221119306658924L;

    public TestPage(PageParameters parameters) {
        super(parameters);

        setStatelessHint(false);

        add(new BookmarkablePageLink<String>("link", TestPage.class));

        FormSettings formSettings = new FormSettings().setAjax(false).setClientsideRequiredValidation(false);
        DefaultFormActions<Example> actions = new DefaultFormActions<Example>() {
            private static final long serialVersionUID = -7243583927279090086L;

            Example example = new Example();

            @Override
            public void submitObject(Example object) {
                this.example = object;
                System.out.println(example);
            }

            @Override
            public Example loadObject() {
                return example;
            }
        };
        final FormPanel<Example> form = new FormPanel<Example>("testform", actions, formSettings);
        form.getForm();
        add(form);
        form.setVisible(false);

        // List<String> opt1 = new ArrayList<String>();
        // for (int i = 1; i <= 10; i++) {
        // opt1.add("option " + (i == 10 ? 0 : i));
        // }
        // List<String> opt2 = new ArrayList<String>();
        // for (int i = 0; i < 26; i++) {
        // opt2.add("option " + (char) ('A' + i));
        // }
        // IModel<String>[] groupLabels = new IModel[] { Model.of("numbers"), Model.of("letters") };
        // IOptionRenderer<String> optionRenderer = new IOptionRenderer<String>() {
        // @Override
        // public String getDisplayValue(String object) {
        // return object;
        // }
        //
        // @Override
        // public IModel<String> getModel(String value) {
        // return Model.of(value);
        // }
        // };
        //
        // ListModel<String>[] opt = new ListModel[] { new ListModel<String>(opt1), new ListModel<String>(opt2) };
        //
        // groupLabels = null;
        //
        // form.addDropDown(WebHelper.proxy(Example.class).getText(), new DropDownSettings().setNullValid(true), optionRenderer, opt, groupLabels);
        // form.addList(WebHelper.proxy(Example.class).getTextExtra(), new ListSettings().setSize(10), optionRenderer, opt, groupLabels);

        // form.addTextField(WebHelper.proxy(Example.class).getText(), new FormElementSettings(true));
        // form.addTextField(WebHelper.proxy(Example.class).getTextExtra(), new FormElementSettings(true));

        // form.addCheckBox(WebHelper.proxy(Example.class).getCheck(), new FormElementSettings(true));

        ArrayList<Social> options = new ArrayList<Social>(new TreeSet<Social>(Arrays.asList(Social.values())));
        add(new ListView<Social>("repeater1", options) {
            private static final long serialVersionUID = -7495456081110874114L;

            @Override
            protected void populateItem(ListItem<Social> item) {
                item.add(new SocialPanel("sb1", item.getModel()));
            }
        });
        add(new ListView<Social>("repeater2", options) {
            private static final long serialVersionUID = -2422255718832136362L;

            @Override
            protected void populateItem(ListItem<Social> item) {
                item.add(new SocialPanel("sb2", item.getModel(), true));
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_JS));
        response.render(CssHeaderItem.forReference(PrimeUI.PRIME_UI_CSS));
    }
}

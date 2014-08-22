package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;
import org.tools.hqlbuilder.webservice.wicket.forms.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormSettings;

@SuppressWarnings({ "serial", "unchecked" })
@MountedPage("/test")
public class TestPage extends DefaultWebPage {
    public TestPage(PageParameters parameters) {
        super(parameters);

        setStatelessHint(false);

        FormSettings formSettings = new FormSettings().setColumns(2).setShowLabel(false).setAjax(false).setClientsideRequiredValidation(false);
        DefaultFormActions<Example> actions = new DefaultFormActions<Example>() {
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

        form.addTextField(WebHelper.proxy(Example.class).getText(), new FormElementSettings(true));
        form.addTextField(WebHelper.proxy(Example.class).getTextExtra(), new FormElementSettings(true));
    }
}

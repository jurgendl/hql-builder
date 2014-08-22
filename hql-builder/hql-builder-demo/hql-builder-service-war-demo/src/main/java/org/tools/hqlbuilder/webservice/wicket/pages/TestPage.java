package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;
import org.tools.hqlbuilder.webservice.wicket.forms.DropDownSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.ListSettings;

@SuppressWarnings({ "serial", "unchecked" })
@MountedPage("/test")
public class TestPage extends DefaultWebPage {
    public TestPage(PageParameters parameters) {
        super(parameters);

        setStatelessHint(false);

        FormSettings formSettings = new FormSettings();
        FormPanel<Example> multicolform = new FormPanel<Example>("testform", null, formSettings) {
            /***/
        };
        multicolform.getForm();
        add(multicolform);

        List<String> opt1 = new ArrayList<String>();
        for (int i = 1; i <= 10; i++) {
            opt1.add("option " + (i == 10 ? 0 : i));
        }
        List<String> opt2 = new ArrayList<String>();
        for (int i = 0; i < 26; i++) {
            opt2.add("option " + (char) ('A' + i));
        }
        IModel<String>[] groupLabels = new IModel[] { Model.of("numbers"), Model.of("letters") };
        IOptionRenderer<String> optionRenderer = new IOptionRenderer<String>() {
            @Override
            public String getDisplayValue(String object) {
                return object;
            }

            @Override
            public IModel<String> getModel(String value) {
                return Model.of(value);
            }
        };

        ListModel<String>[] opt = new ListModel[] { new ListModel<String>(opt1), new ListModel<String>(opt2) };

        groupLabels = null;

        multicolform.addDropDown(WebHelper.proxy(Example.class).getText(), new DropDownSettings().setNullValid(true), optionRenderer, opt,
                groupLabels);
        multicolform.addList(WebHelper.proxy(Example.class).getTextExtra(), new ListSettings().setSize(10), optionRenderer, opt, groupLabels);
    }
}

package org.tools.hqlbuilder.webservice.wicket.pages;

import java.io.Serializable;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.forms.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.TextFieldPanel;

@SuppressWarnings("serial")
@MountedPage("/multi")
public class MultiColumnFormPage extends DefaultWebPage {
    public MultiColumnFormPage(PageParameters parameters) {
        super(parameters);

        setStatelessHint(false);

        FormSettings formSettings = new FormSettings();
        FormPanel<Serializable> multicolform = new FormPanel<Serializable>("multicolform", null, formSettings) {
            /***/
        };
        multicolform.getForm();
        add(multicolform);

        for (int i = 1; i <= 5; i++) {
            TextFieldPanel<String> textField = multicolform.addTextField("label " + (i + 1), new FormElementSettings());
            textField.setValueModel(new Model<String>("value " + (i + 1)));
        }

        formSettings.setColumns(2);

        for (int i = 6; i <= 16; i++) {
            TextFieldPanel<String> textField = multicolform.addTextField("label " + (i + 1), new FormElementSettings());
            textField.setValueModel(new Model<String>("value " + (i + 1)));
        }

        formSettings.setColumns(3);

        for (int i = 17; i <= 29; i++) {
            TextFieldPanel<String> textField = multicolform.addTextField("label " + (i + 1), new FormElementSettings());
            textField.setValueModel(new Model<String>("value " + (i + 1)));
        }
    }
}

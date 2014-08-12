package org.tools.hqlbuilder.webservice.wicket.pages;

import java.io.Serializable;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.forms.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.TextFieldPanel;

@SuppressWarnings("serial")
@MountedPage("/multi")
public class MultiColumnFormPage extends DefaultWebPage {
    public MultiColumnFormPage(PageParameters parameters) {
        super(parameters);

        FormPanel<Serializable> multicolform = new FormPanel<Serializable>("multicolform", new DefaultFormActions<Serializable>(), new FormSettings());
        add(multicolform);

        for (int i = 0; i < 10; i++) {
            TextFieldPanel<String> textField = multicolform.addTextField("label " + (i + 1), new FormElementSettings());
            textField.setValueModel(new Model<String>("value " + (i + 1)));
        }
    }
}

package org.tools.hqlbuilder.webservice.wicket.pages;

import java.io.Serializable;
import java.util.Date;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;
import org.tools.hqlbuilder.webservice.wicket.forms.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormSettings;

@SuppressWarnings("serial")
@MountedPage("/test")
public class TestPage extends DefaultWebPage {
    public TestPage(PageParameters parameters) {
        super(parameters);

        setStatelessHint(false);

        FormSettings formSettings = new FormSettings();
        FormPanel<TestDTO> multicolform = new FormPanel<TestDTO>("testform", null, formSettings) {
            /***/
        };
        multicolform.getForm();
        add(multicolform);

        multicolform.addDatePicker(WebHelper.proxy(TestDTO.class).getDate(), new FormElementSettings());
    }

    public static class TestDTO implements Serializable {
        private Date date;

        public Date getDate() {
            return this.date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}

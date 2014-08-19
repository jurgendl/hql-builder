package org.tools.hqlbuilder.webservice.wicket.pages;

import java.io.Serializable;
import java.util.Date;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;

import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;

@SuppressWarnings("serial")
@MountedPage("/test")
public class TestPage extends DefaultWebPage {
    public TestPage(PageParameters parameters) {
        super(parameters);

        setStatelessHint(false);

        Form testform = new Form("testform");
        add(testform);

        testform.add(new DatePicker("dp"));
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

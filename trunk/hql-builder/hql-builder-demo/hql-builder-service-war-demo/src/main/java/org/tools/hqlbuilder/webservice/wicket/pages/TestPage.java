package org.tools.hqlbuilder.webservice.wicket.pages;

import java.io.Serializable;
import java.util.Date;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;

import com.googlecode.wicket.kendo.ui.panel.KendoFeedbackPanel;
import com.googlecode.wicket.kendo.ui.widget.editor.Editor;

@SuppressWarnings("serial")
@MountedPage("/test")
public class TestPage extends DefaultWebPage {
    public TestPage(PageParameters parameters) {
        super(parameters);

        setStatelessHint(false);

        // FormSettings formSettings = new FormSettings();
        // FormPanel<TestDTO> multicolform = new FormPanel<TestDTO>("testform", null, formSettings) {
        // /***/
        // };
        // multicolform.getForm();
        // add(multicolform);
        //
        // multicolform.addDatePicker(WebHelper.proxy(TestDTO.class).getDate(), new FormElementSettings());

        // ============================================================

        Form<Void> form = new Form<Void>("form");
        this.add(form);

        // FeedbackPanel //
        final KendoFeedbackPanel feedback = new KendoFeedbackPanel("feedback");
        form.add(feedback);

        // ComboBox //
        final Editor<String> editor = new Editor<String>("editor", Model.of("<p>test</p>"));
        form.add(editor.setEscapeModelStrings(false));

        // Buttons //
        form.add(new Button("button") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                String html = editor.getModelObject();

                this.info(html != null ? html : "empty");
            }
        });
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

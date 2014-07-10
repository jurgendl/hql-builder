package org.tools.hqlbuilder.webservice.wicket.pages;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.create;
import static org.tools.hqlbuilder.webservice.wicket.WebHelper.name;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.tools.hqlbuilder.webservice.wicket.WicketSession;
import org.tools.hqlbuilder.webservice.wicket.forms.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.pages.ExampleForm.Styling;

@SuppressWarnings("serial")
public class ExampleForm extends FormPanel<Styling> {
    public ExampleForm(String id) {
        super(id, Model.of(new Styling()), true, new DefaultFormActions<Styling>() {
            @Override
            public void submit(IModel<Styling> m) {
                WicketSession.get().printStyling(System.out);
            }
        }.setAjax(false));

        Styling proxy = create(Styling.class);

        getFormSettings().setClientsideRequiredValidation(false);

        FormElementSettings fset = new FormElementSettings().setRequired(true);
        IChoiceRenderer<StyleOpts> styleOptsRenderer = new EnumChoiceRenderer<StyleOpts>(this);
        addRadioButtons(name(proxy.getRadio()), StyleOpts.class, fset, new ListModel<StyleOpts>(Arrays.asList(StyleOpts.values())), styleOptsRenderer);
        addDropDown(name(proxy.getCombo()), StyleOpts.class, fset, new ListModel<StyleOpts>(Arrays.asList(StyleOpts.values())), styleOptsRenderer);
        addEmailTextField(name(proxy.getEmail()), fset);
        addTextField(name(proxy.getText()), fset);
        addDatePicker(name(proxy.getDate()), fset);
        addPasswordTextField(name(proxy.getPassword()), new FormElementSettings());
    }

    public static enum StyleOpts {
        OPT1, OPT2, OPT3, OPT4, OPT5;
    }

    public static class Styling implements Serializable {
        private char[] password = null;

        private String text = "test@gmail.com";

        private String email = "testString";

        private StyleOpts radio = StyleOpts.OPT1;

        private StyleOpts combo = StyleOpts.OPT1;

        @SuppressWarnings("deprecation")
        private Date date = new Date(2000, 4, 4);

        public char[] getPassword() {
            return this.password;
        }

        public void setPassword(char[] password) {
            this.password = password;
        }

        public StyleOpts getRadio() {
            return this.radio;
        }

        public StyleOpts getCombo() {
            return this.combo;
        }

        public void setRadio(StyleOpts radio) {
            this.radio = radio;
        }

        public void setCombo(StyleOpts combo) {
            this.combo = combo;
        }

        public String getText() {
            return this.text;
        }

        public String getEmail() {
            return this.email;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Date getDate() {
            return this.date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}

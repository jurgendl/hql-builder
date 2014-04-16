package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.UserData;

public class FormPanel<T> extends Panel {
    private static final long serialVersionUID = -3268906227997947993L;

    protected static final String FORM_REPEATER = "form.repeater";

    protected static final String FORM_PANEL = "form.panel";

    protected static final String FORM = "form";

    protected RepeatingView repeater;

    public FormPanel(String id, IModel<T> model) {
        super(FORM_PANEL, model);

        setOutputMarkupPlaceholderTag(false);
        setRenderBodyOnly(true);
        setOutputMarkupId(false);

        Form<UserData> form = new Form<UserData>(FORM);
        form.setMarkupId(id);
        add(form);

        repeater = new RepeatingView(FORM_REPEATER);
        repeater.setOutputMarkupPlaceholderTag(false);
        repeater.setRenderBodyOnly(true);
        repeater.setOutputMarkupId(false);
        form.add(repeater);
    }

    protected WebMarkupContainer newContainer() {
        WebMarkupContainer container = new WebMarkupContainer(repeater.newChildId());
        container.setOutputMarkupPlaceholderTag(false);
        container.setRenderBodyOnly(true);
        container.setOutputMarkupId(false);
        return container;
    }

    public TextField<String> addTextField(String property) {
        return addTextField(property, String.class);
    }

    public <F> TextField<F> addTextField(String property, Class<F> type) {
        TextFieldPanel<F> textFieldPanel = new TextFieldPanel<F>(getDefaultModel(), property, type);
        repeater.add(newContainer().add(textFieldPanel));
        return textFieldPanel.getTextField();
    }

    public EmailTextField addEmailTextField(String property) {
        EmailTextFieldPanel textFieldPanel = new EmailTextFieldPanel(getDefaultModel(), property);
        repeater.add(newContainer().add(textFieldPanel));
        return textFieldPanel.getEmailTextField();
    }
}

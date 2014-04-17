package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.springframework.beans.BeanUtils;

public abstract class FormPanel<T extends Serializable> extends Panel implements FormConstants {
    private static final long serialVersionUID = -3268906227997947993L;

    protected RepeatingView repeater;

    public FormPanel(String id, Class<T> modelType) {
        this(id, new CompoundPropertyModel<T>(BeanUtils.instantiate(modelType)));
    }

    public FormPanel(String id, IModel<T> model) {
        super(FORM_PANEL, model);

        setOutputMarkupPlaceholderTag(false);
        setRenderBodyOnly(true);
        setOutputMarkupId(false);

        Form<T> form = new Form<T>(FORM, model) {
            private static final long serialVersionUID = -6736595826748998036L;

            @Override
            protected void onSubmit() {
                super.onSubmit();
                // submit((IModel<T>) getDefaultModel());
                System.out.println("\n\n\n\n\n\n\n\n-----------------\n\n\n\n\n\n\n\n");
            }
        };
        // form.setMarkupId(id);
        add(form);

        repeater = new RepeatingView(FORM_REPEATER);
        repeater.setOutputMarkupPlaceholderTag(false);
        repeater.setRenderBodyOnly(true);
        repeater.setOutputMarkupId(false);
        form.add(repeater);

        Button submit = new Button(FORM_SUBMIT, new ResourceModel("submit.label")) {
            @Override
            public void onSubmit() {
                super.onSubmit();
                System.out.println("\n\n\n\n\n\n\n\n*****************\n\n\n\n\n\n\n\n");
            }
        };
        Button reset = new Button(FORM_RESET, new ResourceModel("reset.label"));
        // submit.setMarkupId(submit.getId());
        // reset.setMarkupId(reset.getId());
        form.add(submit);
        form.add(reset);
    }

    protected abstract void submit(IModel<T> model);

    public TextField<String> addTextField(String property) {
        return addTextField(property, String.class);
    }

    public <F> TextField<F> addTextField(String property, Class<F> type) {
        return new TextFieldPanel<F>(getDefaultModel(), property, type).addTo(repeater);
    }

    public EmailTextField addEmailTextField(String property) {
        return new EmailTextFieldPanel(getDefaultModel(), property).addTo(repeater);
    }

    public PasswordTextField addPasswordTextField(String property) {
        return new PasswordTextFieldPanel(getDefaultModel(), property).addTo(repeater);
    }

    public static abstract class FormRowPanel<T, C extends MarkupContainer> extends Panel implements FormConstants {
        private static final long serialVersionUID = -6401309948019996576L;

        protected final Label label;

        protected final String property;

        protected final Class<T> type;

        protected final C component;

        public FormRowPanel(final IModel<?> model, final String property, final Class<T> type) {
            super(FORM_ROW, model);
            this.property = property;
            this.type = type;
            setOutputMarkupPlaceholderTag(false);
            setRenderBodyOnly(true);
            setOutputMarkupId(false);
            label = addLabel();
            component = addComponent();
        }

        protected C addComponent() {
            C comp = createComponent();
            // comp.setMarkupId(property);
            add(comp);
            return comp;
        }

        protected Label addLabel() {
            AbstractReadOnlyModel<String> labelModel = new AbstractReadOnlyModel<String>() {
                private static final long serialVersionUID = -6461211838443556886L;

                @Override
                public String getObject() {
                    return getLabel();
                }
            };
            Label labelComponent = new Label(LABEL, labelModel) {
                private static final long serialVersionUID = -4486835664954887226L;

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    tag.getAttributes().put(FOR, property);
                }
            };
            add(labelComponent);
            return labelComponent;
        }

        protected WebMarkupContainer newContainer(RepeatingView repeater) {
            WebMarkupContainer container = new WebMarkupContainer(repeater.newChildId());
            container.setOutputMarkupPlaceholderTag(false);
            container.setRenderBodyOnly(true);
            container.setOutputMarkupId(false);
            return container;
        }

        protected C addTo(RepeatingView repeater) {
            repeater.add(newContainer(repeater).add(this));
            return component;
        }

        protected void setPlaceholder(ComponentTag tag) {
            tag.getAttributes().put(PLACEHOLDER, getPlaceholder());
        }

        protected String getLabel() {
            return property;
        }

        protected String getPlaceholder() {
            return getString(PLACEHOLDER);
        }

        protected PropertyModel<T> getValueModel() {
            return new PropertyModel<T>(getDefaultModel(), property);
        }

        protected abstract C createComponent();

        public C getComponent() {
            return this.component;
        }
    }
}

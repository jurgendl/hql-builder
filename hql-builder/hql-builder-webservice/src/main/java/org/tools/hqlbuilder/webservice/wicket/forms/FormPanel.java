package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.Date;
import java.util.MissingResourceException;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.tools.hqlbuilder.webservice.wicket.Converter;
import org.tools.hqlbuilder.webservice.wicket.ModelConverter;

import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;

public abstract class FormPanel<T extends Serializable> extends Panel implements FormConstants {
    private static final long serialVersionUID = -3268906227997947993L;

    protected static final Logger logger = LoggerFactory.getLogger(FormPanel.class);

    protected RepeatingView repeater;

    protected final boolean inheritId;

    public FormPanel(String id, Class<T> modelType) {
        this(id, modelType, false, false);
    }

    public FormPanel(String id, Class<T> modelType, boolean inheritId, boolean ajax) {
        this(id, newFormModel(modelType), inheritId, ajax);
    }

    public FormPanel(String id, IModel<T> model, boolean inheritId, boolean ajax) {
        super(FORM_PANEL, model);

        this.inheritId = inheritId;

        setOutputMarkupPlaceholderTag(true);
        setRenderBodyOnly(false);
        setOutputMarkupId(true);

        final Form<T> form = new Form<T>(FORM, model) {
            private static final long serialVersionUID = -6736595826748998036L;

            @SuppressWarnings("unchecked")
            @Override
            protected void onSubmit() {
                super.onSubmit();
                submit((IModel<T>) getDefaultModel());
            }

            @Override
            public String toString() {
                return getClass().getSuperclass().toString();
            }
        };
        if (inheritId) {
            form.setMarkupId(id);
        }
        form.setOutputMarkupPlaceholderTag(true);
        form.setRenderBodyOnly(false);
        form.setOutputMarkupId(true);
        add(form);

        repeater = new RepeatingView(FORM_REPEATER);
        repeater.setOutputMarkupPlaceholderTag(false);
        repeater.setRenderBodyOnly(true);
        repeater.setOutputMarkupId(false);
        form.add(repeater);

        Component submit;
        if (ajax) {
            submit = new AjaxSubmitLink(FORM_SUBMIT, form) {
                private static final long serialVersionUID = -983242396412538529L;

                @Override
                protected void onAfterSubmit(AjaxRequestTarget target, Form<?> f) {
                    target.add(f);
                }
            };
        } else {
            submit = new Button(FORM_SUBMIT, new ResourceModel("submit.label"));
        }

        Button reset = new Button(FORM_RESET, new ResourceModel("reset.label"));
        if (inheritId) {
            submit.setMarkupId(id + "." + FORM_SUBMIT);
            reset.setMarkupId(id + "." + FORM_RESET);
        }

        form.add(submit);
        form.add(reset);
    }

    public static <T> IModel<T> newFormModel(Class<T> modelType) {
        return new CompoundPropertyModel<T>(BeanUtils.instantiate(modelType));
    }

    public FormPanel<T> liveValidation() {
        AjaxFormValidatingBehavior.addToAllFormComponents(Form.class.cast(get(FORM)), "onkeyup", Duration.NONE);
        return this;
    }

    protected abstract void submit(IModel<T> model);

    protected <F extends FormComponent<?>> F addId(String property, F component) {
        if (inheritId) {
            component.setMarkupId(property);
        }
        return component;
    }

    public DatePicker addDatePicker(String property, boolean required) {
        return addId(property, new DatePickerPanel(getDefaultModel(), property, required, getLocale()).addTo(repeater));
    }

    public <X> DatePicker addDatePicker(String property, boolean required, final Converter<X, Date> dateConverter) {
        return addId(property, new DatePickerPanel(getDefaultModel(), property, required, getLocale()) {
            private static final long serialVersionUID = -7680994763787874544L;

            @Override
            protected IModel<Date> getValueModel() {
                IModel<X> backingModel = new PropertyModel<X>(getDefaultModel(), property);
                return new ModelConverter<X, Date>(backingModel, dateConverter);
            };
        }.addTo(repeater));
    }

    public TextField<String> addTextField(String property, boolean required) {
        return addTextField(property, String.class, required);
    }

    public <F> TextField<F> addTextField(String property, Class<F> type, boolean required) {
        return addId(property, new TextFieldPanel<F>(getDefaultModel(), property, type, required).addTo(repeater));
    }

    public EmailTextField addEmailTextField(String property, boolean required) {
        return addId(property, new EmailTextFieldPanel(getDefaultModel(), property, required).addTo(repeater));
    }

    public PasswordTextField addPasswordTextField(String property, boolean required) {
        return addId(property, new PasswordTextFieldPanel(getDefaultModel(), property, required).addTo(repeater));
    }

    public static abstract class FormRowPanel<T, C extends FormComponent<T>, O> extends Panel implements FormConstants {
        public static final String FEEDBACK_ID = "componentFeedback";

        private static final long serialVersionUID = -6401309948019996576L;

        protected final Label label;

        protected final String property;

        protected final Class<T> type;

        protected final C component;

        protected final boolean required;

        protected O options;

        public FormRowPanel(final IModel<?> model, final String property, final Class<T> type, final boolean required) {
            this(model, property, type, required, null);
        }

        public FormRowPanel(final IModel<?> model, final String property, final Class<T> type, final boolean required, O options) {
            super(FORM_ROW, model);
            this.options = options;
            this.required = required;
            this.property = property;
            this.type = type;
            setOutputMarkupPlaceholderTag(false);
            setRenderBodyOnly(true);
            setOutputMarkupId(false);
            label = addLabel();
            component = addComponent();
            add(new FeedbackPanel(FEEDBACK_ID, new ComponentFeedbackMessageFilter(component)).setOutputMarkupId(true));
        }

        protected C addComponent() {
            C comp = createComponent();
            comp.setRequired(required);
            add(comp);
            return comp;
        }

        protected Label addLabel() {
            AbstractReadOnlyModel<String> labelModel = new AbstractReadOnlyModel<String>() {
                private static final long serialVersionUID = -6461211838443556886L;

                @Override
                public String getObject() {
                    try {
                        return getLabel();
                    } catch (MissingResourceException ex) {
                        logger.error("no translation for " + property);
                        return "${" + property + "}";
                    }
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
            try {
                tag.getAttributes().put(PLACEHOLDER, getPlaceholder());
            } catch (MissingResourceException ex) {
                logger.error("no translation for " + PLACEHOLDER);
            }
        }

        protected void setRequired(ComponentTag tag) {
            if (required) {
                tag.getAttributes().put(REQUIRED, required);
            } else {
                tag.getAttributes().remove(REQUIRED);
            }
        }

        protected void onFormComponentTag(ComponentTag tag) {
            setPlaceholder(tag);
            setRequired(tag);
        }

        protected String getLabel() {
            return getString(property);
        }

        protected String getPlaceholder() {
            return getString(PLACEHOLDER);
        }

        protected IModel<T> getValueModel() {
            return new PropertyModel<T>(getDefaultModel(), property);
        }

        protected abstract C createComponent();

        public C getComponent() {
            return this.component;
        }
    }
}

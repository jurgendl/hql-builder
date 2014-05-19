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
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
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
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;
import org.tools.hqlbuilder.webservice.wicket.ext.RequiredBehavior;

public class FormPanel<T extends Serializable> extends Panel implements FormConstants {
    private static final long serialVersionUID = -3268906227997947993L;

    protected static final Logger logger = LoggerFactory.getLogger(FormPanel.class);

    protected RepeatingView repeater;

    protected boolean inheritId = false;

    protected boolean showLabel = true;

    public FormPanel(String id, Class<T> modelType, FormActions<T> actions) {
        this(id, modelType, false, actions);
    }

    public FormPanel(String id, Class<T> modelType, boolean inheritId, FormActions<T> actions) {
        this(id, newFormModel(modelType), inheritId, actions);
    }

    public FormPanel(String id, IModel<T> model, boolean inheritId, final FormActions<T> actions) {
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
                actions.submit((IModel<T>) getDefaultModel());
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
        if (actions.isAjax()) {
            submit = new AjaxSubmitLink(FORM_SUBMIT, form) {
                private static final long serialVersionUID = -983242396412538529L;

                @SuppressWarnings("unchecked")
                @Override
                protected void onAfterSubmit(AjaxRequestTarget target, Form<?> f) {
                    actions.afterSubmit(target, form, (IModel<T>) getDefaultModel());
                }
            };
        } else {
            submit = new Button(FORM_SUBMIT, new ResourceModel("submit.label"));
        }

        Button reset = new Button(FORM_RESET, new ResourceModel("reset.label"));

        Component cancel;
        if (actions.isAjax()) {
            cancel = new AjaxSubmitLink(FORM_CANCEL, form) {
                private static final long serialVersionUID = 4339770380895679763L;

                @SuppressWarnings("unchecked")
                @Override
                protected void onAfterSubmit(AjaxRequestTarget target, Form<?> f) {
                    actions.afterCancel(target, form, (IModel<T>) getDefaultModel());
                }
            };
            ((AjaxSubmitLink) cancel).setDefaultFormProcessing(false);
        } else {
            cancel = new Button(FORM_CANCEL, new ResourceModel("cancel.label"));
            ((Button) cancel).setDefaultFormProcessing(false);
        }
        cancel.setVisible(actions.isCancelable());

        if (inheritId) {
            submit.setMarkupId(id + "." + FORM_SUBMIT);
            reset.setMarkupId(id + "." + FORM_RESET);
            cancel.setMarkupId(id + "." + FORM_CANCEL);
        }

        form.add(submit);
        form.add(reset);
        form.add(cancel);
    }

    public boolean isShowLabel() {
        return this.showLabel;
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    public static <T> IModel<T> newFormModel(Class<T> modelType) {
        return new CompoundPropertyModel<T>(BeanUtils.instantiate(modelType));
    }

    public FormPanel<T> liveValidation() {
        AjaxFormValidatingBehavior.addToAllFormComponents(Form.class.cast(get(FORM)), "onkeyup", Duration.NONE);
        return this;
    }

    protected <F extends FormComponent<?>> F addId(String property, F component) {
        if (inheritId) {
            component.setMarkupId(property);
        }
        return component;
    }

    public DatePickerPanel<Date> addDatePicker(String property, boolean required) {
        return addDatePicker(property, required, (Converter<Date, Date>) null);
    }

    public TextFieldPanel<String> addTextField(String property, boolean required) {
        return addTextField(property, String.class, required);
    }

    public <X> DatePickerPanel<X> addDatePicker(String property, boolean required, final Converter<X, Date> dateConverter) {
        DatePickerPanel<X> row = new DatePickerPanel<X>(getDefaultModel(), property, dateConverter);
        addRow(property, required, row);
        return row;
    }

    public <F> TextFieldPanel<F> addTextField(String property, Class<F> type, boolean required) {
        TextFieldPanel<F> row = new TextFieldPanel<F>(getDefaultModel(), property, type);
        addRow(property, required, row);
        return row;
    }

    public EmailTextFieldPanel addEmailTextField(String property, boolean required) {
        EmailTextFieldPanel row = new EmailTextFieldPanel(getDefaultModel(), property);
        addRow(property, required, row);
        return row;
    }

    public PasswordTextFieldPanel addPasswordTextField(String property, boolean required) {
        PasswordTextFieldPanel row = new PasswordTextFieldPanel(getDefaultModel(), property);
        addRow(property, required, row);
        return row;
    }

    protected <V, C extends FormComponent<V>> FormRowPanel<V, C> addRow(String property, boolean required, FormRowPanel<V, C> row) {
        settings(row, required);
        row.addComponentsTo(repeater);
        addId(property, row.getComponent());
        return row;
    }

    protected <V, C extends FormComponent<V>> FormRowPanel<V, C> settings(FormRowPanel<V, C> row, boolean required) {
        row.setRequired(required);
        row.setInheritId(inheritId);
        row.setShowLabel(showLabel);
        return row;
    }

    public static abstract class FormRowPanel<T, C extends FormComponent<T>> extends Panel implements FormConstants {
        public static final String FEEDBACK_ID = "componentFeedback";

        private static final long serialVersionUID = -6401309948019996576L;

        protected Label label;

        protected String property;

        protected Class<T> type;

        protected FeedbackPanel feedbackPanel;

        protected WebMarkupContainer container;

        protected C component;

        protected boolean required;

        protected boolean inheritId;

        protected boolean showLabel;

        public FormRowPanel(final IModel<?> model, final String property, final Class<T> type) {
            super(FORM_ROW, model);
            this.property = property;
            this.type = type;
            setOutputMarkupPlaceholderTag(false);
            setRenderBodyOnly(true);
            setOutputMarkupId(false);
        }

        protected Label getLabel() {
            if (label == null) {
                IModel<String> labelModel = new AbstractReadOnlyModel<String>() {
                    private static final long serialVersionUID = -6461211838443556886L;

                    @Override
                    public String getObject() {
                        try {
                            return getLabelText();
                        } catch (MissingResourceException ex) {
                            logger.error("no translation for " + property);
                            return "${" + property + "}";
                        }
                    }
                };
                label = new Label(LABEL, labelModel) {
                    private static final long serialVersionUID = -4486835664954887226L;

                    @Override
                    public boolean isVisible() {
                        return super.isVisible() && showLabel;
                    }

                    @Override
                    protected void onComponentTag(ComponentTag tag) {
                        super.onComponentTag(tag);
                        tag.getAttributes().put(FOR, property);
                    }
                };
            }
            return label;
        }

        protected C getComponent() {
            if (component == null) {
                component = createComponent();
                component.setRequired(required);
                component.add(new RequiredBehavior());
            }
            return this.component;
        }

        protected abstract C createComponent();

        protected FeedbackPanel getFeedback() {
            if (feedbackPanel == null) {
                feedbackPanel = new FeedbackPanel(FEEDBACK_ID, new ComponentFeedbackMessageFilter(component));
                feedbackPanel.setOutputMarkupId(true);
            }
            return feedbackPanel;
        }

        protected WebMarkupContainer getContainer(RepeatingView repeater) {
            if (container == null) {
                container = new WebMarkupContainer(repeater.newChildId());
                container.setOutputMarkupPlaceholderTag(false);
                container.setRenderBodyOnly(true);
                container.setOutputMarkupId(false);
            }
            return container;
        }

        protected C addComponentsTo(RepeatingView repeater) {
            C c = getComponent();
            this.add(getLabel());
            this.add(c);
            this.add(getFeedback());
            WebMarkupContainer rowContainer = getContainer(repeater);
            repeater.add(rowContainer);
            rowContainer.add(this);
            return c;
        }

        protected void setupPlaceholder(ComponentTag tag) {
            try {
                tag.getAttributes().put(PLACEHOLDER, getPlaceholderText());
            } catch (MissingResourceException ex) {
                logger.error("no translation for " + PLACEHOLDER);
            }
        }

        protected void setupRequired(ComponentTag tag) {
            if (required) {
                tag.getAttributes().put(REQUIRED, required);
            } else {
                tag.getAttributes().remove(REQUIRED);
            }
        }

        /**
         * call this in overridden method:<br>
         * org.tools.hqlbuilder.webservice.wicket.forms.[Component]Panel.createComponent().new [Component]() {...}.onComponentTag(ComponentTag)
         */
        protected void onFormComponentTag(ComponentTag tag) {
            setupPlaceholder(tag);
            setupRequired(tag);
        }

        protected String getLabelText() {
            return getString(property);
        }

        protected String getPlaceholderText() {
            return getString(PLACEHOLDER);
        }

        protected IModel<T> getValueModel() {
            return new PropertyModel<T>(getDefaultModel(), property);
        }

        public boolean isShowLabel() {
            return this.showLabel;
        }

        public void setShowLabel(boolean showLabel) {
            this.showLabel = showLabel;
        }

        public boolean isInheritId() {
            return this.inheritId;
        }

        public void setInheritId(boolean inheritId) {
            this.inheritId = inheritId;
        }

        public boolean isRequired() {
            return this.required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }
    }

    public static interface FormActions<T> extends Serializable {
        public abstract void afterCancel(AjaxRequestTarget target, Form<T> form, IModel<T> model);

        public abstract void submit(IModel<T> model);

        public abstract boolean isAjax();

        public abstract boolean isCancelable();

        public abstract void afterSubmit(AjaxRequestTarget target, Form<T> form, IModel<T> model);
    }

    public static class DefaultFormActions<T> implements FormActions<T> {
        private static final long serialVersionUID = 555158530492799693L;

        @Override
        public void afterSubmit(AjaxRequestTarget target, Form<T> form, IModel<T> model) {
            target.add(form);
        }

        @Override
        public boolean isAjax() {
            return true;
        }

        @Override
        public boolean isCancelable() {
            return false;
        }

        @Override
        public void afterCancel(AjaxRequestTarget target, Form<T> form, IModel<T> model) {
            afterSubmit(target, form, model);
        }

        @Override
        public void submit(IModel<T> model) {
            //
        }
    }
}

package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.Date;
import java.util.MissingResourceException;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.tools.hqlbuilder.webservice.wicket.HtmlEvent.HtmlFormEvent;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

public class FormPanel<T extends Serializable> extends Panel implements FormConstants {
    private static final long serialVersionUID = -3268906227997947993L;

    protected static final Logger logger = LoggerFactory.getLogger(FormPanel.class);

    protected RepeatingView repeater;

    protected final FormSettings formSettings;

    public static <T> IModel<T> newFormModel(Class<T> modelType) {
        return newFormModel(BeanUtils.instantiate(modelType));
    }

    public static <T> IModel<T> newFormModel(T model) {
        return new CompoundPropertyModel<T>(model);
    }

    public FormPanel(String id, IModel<T> model, boolean inheritId, final FormActions<T> actions) {
        super(FORM_PANEL, model);
        formSettings = new FormSettings(inheritId, actions.isAjax());
        createForm(id, model, actions);
    }

    @SuppressWarnings("unchecked")
    public IModel<T> getFormModel() {
        return (IModel<T>) getDefaultModel();
    }

    protected void createForm(String id, IModel<T> model, final FormActions<T> actions) {
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
        if (formSettings.inheritId) {
            form.setMarkupId(id);
        }

        form.setOutputMarkupPlaceholderTag(true);
        form.setRenderBodyOnly(false);
        form.setOutputMarkupId(true);

        add(form);

        WebMarkupContainer formBody = new WebMarkupContainer(FORM_BODY);
        form.add(formBody);

        repeater = new RepeatingView(FORM_REPEATER);
        repeater.setOutputMarkupPlaceholderTag(true);
        repeater.setRenderBodyOnly(false);
        repeater.setOutputMarkupId(true);
        formBody.add(repeater);

        ResourceModel submitModel = new ResourceModel("submit.label");
        ResourceModel resetModel = new ResourceModel("reset.label");
        ResourceModel cancelModel = new ResourceModel("cancel.label");

        Component submit;

        if (formSettings.ajax) {
            submit = new AjaxSubmitLink(FORM_SUBMIT, form) {
                private static final long serialVersionUID = -983242396412538529L;

                @SuppressWarnings("unchecked")
                @Override
                protected void onAfterSubmit(AjaxRequestTarget target, Form<?> f) {
                    actions.afterSubmit(target, form, (IModel<T>) getDefaultModel());
                }

                @Override
                protected void onAfterRender() {
                    super.onAfterRender();
                    // possible fix ajax validation on password fields (unsafe?)
                    // for now validation on password fields is disabled
                    // pass.setResetPassword(false);
                }

                @Override
                protected void onBeforeRender() {
                    super.onBeforeRender();
                    // possible fix ajax validation on password fields (unsafe?)
                    // for now validation on password fields is disabled
                    // pass.setResetPassword(true);
                }
            };
            submit.setDefaultModel(submitModel);
        } else {
            submit = new Button(FORM_SUBMIT, submitModel);
        }

        Button reset = new Button(FORM_RESET, resetModel);

        // https://cwiki.apache.org/confluence/display/WICKET/Multiple+submit+buttons
        Component cancel;
        if (formSettings.ajax) {
            cancel = new AjaxSubmitLink(FORM_CANCEL, form) {
                private static final long serialVersionUID = 4339770380895679763L;

                @SuppressWarnings("unchecked")
                @Override
                protected void onAfterSubmit(AjaxRequestTarget target, Form<?> f) {
                    actions.afterCancel(target, form, (IModel<T>) getDefaultModel());
                }
            };
            cancel.setDefaultModel(cancelModel);
            ((AjaxSubmitLink) cancel).setDefaultFormProcessing(false);
        } else {
            cancel = new Button(FORM_CANCEL, cancelModel);
            ((Button) cancel).setDefaultFormProcessing(false);
        }
        cancel.setVisible(actions.isCancelable());

        if (formSettings.inheritId) {
            submit.setMarkupId(id + "." + FORM_SUBMIT);
            reset.setMarkupId(id + "." + FORM_RESET);
            cancel.setMarkupId(id + "." + FORM_CANCEL);
        }

        WebMarkupContainer formActions = new WebMarkupContainer(FORM_ACTIONS);
        form.add(formActions);

        formActions.add(submit);
        formActions.add(reset);
        formActions.add(cancel);
    }

    public boolean isShowLabel() {
        return formSettings.showLabel;
    }

    public void setShowLabel(boolean showLabel) {
        formSettings.showLabel = showLabel;
    }

    public String getRequiredClass() {
        return formSettings.requiredClass;
    }

    public void setRequiredClass(String requiredClass) {
        formSettings.requiredClass = requiredClass;
    }

    public void setLiveValidation(boolean liveValidation) {
        formSettings.liveValidation = liveValidation;
    }

    public boolean isLiveValidation() {
        return formSettings.liveValidation;
    }

    protected <V, C extends FormComponent<V>> void setupRequiredBehavior(FormRowPanel<V, C> row) {
        C component = row.getComponent();
        if (formSettings.isAjax() && formSettings.isLiveValidation() && !(component instanceof PasswordTextField)
                && !(component instanceof com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker)) {
            component.add(setupDynamicRequiredBehavior(row));
        } else {
            component.add(setupStaticRequiredBehavior(row));
        }
    }

    protected <V, C extends FormComponent<V>> Behavior setupDynamicRequiredBehavior(final FormRowPanel<V, C> row) {
        return new AjaxFormComponentUpdatingBehavior(HtmlFormEvent.BLUR) {
            private static final long serialVersionUID = -4260087964340628125L;

            @Override
            protected void onError(AjaxRequestTarget ajaxRequestTarget, RuntimeException e) {
                C component = row.getComponent();
                component.add(new CssClassNameRemover(formSettings.validClass));
                component.add(new CssClassNameAppender(formSettings.invalidClass));
                ajaxRequestTarget.add(component, row.getFeedback());
            }

            @Override
            protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                C component = row.getComponent();
                component.add(new CssClassNameRemover(formSettings.invalidClass));
                component.add(new CssClassNameAppender(formSettings.validClass));
                ajaxRequestTarget.add(component, row.getFeedback());
            }
        };
    }

    protected <V, C extends FormComponent<V>> Behavior setupStaticRequiredBehavior(@SuppressWarnings("unused") FormRowPanel<V, C> row) {
        return new Behavior() {
            private static final long serialVersionUID = -8002420572609567089L;

            @SuppressWarnings("rawtypes")
            @Override
            public void afterRender(Component c) {
                Response response = c.getResponse();
                StringBuffer asterisktHtml = new StringBuffer(200);
                if (c instanceof FormComponent && ((FormComponent) c).isRequired()) {
                    asterisktHtml.append("<span class=\"fontawesome-asterisk " + formSettings.requiredMarkerClass + "\"/>");
                }
                response.write(asterisktHtml);
            }
        };
    }

    protected <F extends FormComponent<?>> void setupId(String property, F component) {
        if (formSettings.inheritId) {
            component.setMarkupId(property);
        }
    }

    public DatePickerPanel<Date> addDatePicker(String property, FormElementSettings componentSettings) {
        return addDatePicker(property, componentSettings, (Converter<Date, Date>) null);
    }

    public TextFieldPanel<String> addTextField(String property, FormElementSettings componentSettings) {
        return addTextField(property, String.class, componentSettings);
    }

    public <X> DatePickerPanel<X> addDatePicker(String property, FormElementSettings componentSettings, final Converter<X, Date> dateConverter) {
        DatePickerPanel<X> row = new DatePickerPanel<X>(getDefaultModel(), property, dateConverter, formSettings, componentSettings);
        addRow(property, row);
        return row;
    }

    public <F> TextFieldPanel<F> addTextField(String property, Class<F> type, FormElementSettings componentSettings) {
        TextFieldPanel<F> row = new TextFieldPanel<F>(getDefaultModel(), property, type, formSettings, componentSettings);
        addRow(property, row);
        return row;
    }

    public EmailTextFieldPanel addEmailTextField(String property, FormElementSettings componentSettings) {
        EmailTextFieldPanel row = new EmailTextFieldPanel(getDefaultModel(), property, formSettings, componentSettings);
        addRow(property, row);
        return row;
    }

    public PasswordTextFieldPanel addPasswordTextField(String property, FormElementSettings componentSettings) {
        PasswordTextFieldPanel row = new PasswordTextFieldPanel(getDefaultModel(), property, formSettings, componentSettings);
        addRow(property, row);
        return row;
    }

    protected <V, C extends FormComponent<V>> FormRowPanel<V, C> addRow(String property, FormRowPanel<V, C> row) {
        row.addComponentsTo(repeater);
        setupRequiredBehavior(row);
        setupId(property, row.getComponent());
        return row;
    }

    protected static abstract class FormRowPanel<T, C extends FormComponent<T>> extends Panel implements FormConstants {
        public static final String FEEDBACK_ID = "componentFeedback";

        private static final long serialVersionUID = -6401309948019996576L;

        protected Label label;

        protected String property;

        protected Class<T> type;

        protected FeedbackPanel feedbackPanel;

        protected WebMarkupContainer container;

        protected C component;

        protected final FormSettings formSettings;

        protected final FormElementSettings componentSettings;

        public FormRowPanel(final IModel<?> model, final String property, final Class<T> type, FormSettings formSettings,
                FormElementSettings componentSettings) {
            super(FORM_ROW, model);
            this.formSettings = formSettings;
            this.componentSettings = componentSettings;
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
                            return "[" + property + "]";
                        }
                    }
                };
                label = new Label(LABEL, labelModel) {
                    private static final long serialVersionUID = -4486835664954887226L;

                    @Override
                    public boolean isVisible() {
                        return super.isVisible() && formSettings.isShowLabel();
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
                setupRequired(component);
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
            if (componentSettings.isRequired()) {
                tag.getAttributes().put(REQUIRED, componentSettings.isRequired());
            } else {
                tag.getAttributes().remove(REQUIRED);
            }
        }

        private void setupRequired(C component) {
            component.setRequired(componentSettings.isRequired());
            if (StringUtils.isNotBlank(formSettings.getRequiredClass())) {
                if (componentSettings.isRequired()) {
                    component.add(new CssClassNameAppender(formSettings.getRequiredClass()));
                } else {
                    component.add(new CssClassNameRemover(formSettings.getRequiredClass()));
                }
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
    }
}

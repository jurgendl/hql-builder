package org.tools.hqlbuilder.webservice.wicket.forms;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.name;
import static org.tools.hqlbuilder.webservice.wicket.WebHelper.type;

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
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.tools.hqlbuilder.webservice.wicket.HtmlEvent.HtmlFormEvent;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;
import org.tools.hqlbuilder.webservice.wicket.forms.ColorPickerPanel.ColorPickerSettings;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

@SuppressWarnings("serial")
public class FormPanel<T extends Serializable> extends Panel implements FormConstants {
    protected static final Logger logger = LoggerFactory.getLogger(FormPanel.class);

    protected RepeatingView repeater;

    protected FormSettings formSettings;

    public static <T> IModel<T> newFormModel(Class<T> modelType) {
        return newFormModel(BeanUtils.instantiate(modelType));
    }

    public static <T> IModel<T> newFormModel(T model) {
        return new CompoundPropertyModel<T>(model);
    }

    public FormPanel(String id, IModel<T> model, boolean inheritId, FormActions<T> actions) {
        super(id, model);
        formSettings = new FormSettings(inheritId, actions.isAjax());
        createForm(id, model, actions);
    }

    public FormSettings getFormSettings() {
        return this.formSettings;
    }

    @SuppressWarnings("unchecked")
    public IModel<T> getFormModel() {
        return (IModel<T>) getDefaultModel();
    }

    protected FormActions<T> actions;

    protected Form<T> form;

    protected static WebMarkupContainer createContainer(RepeatingView repeater) {
        WebMarkupContainer container = new WebMarkupContainer(repeater.newChildId());
        container.setOutputMarkupPlaceholderTag(false);
        container.setRenderBodyOnly(true);
        container.setOutputMarkupId(false);
        return container;
    }

    protected void createForm(String id, IModel<T> model, FormActions<T> formactions) {
        setOutputMarkupPlaceholderTag(true);
        setRenderBodyOnly(false);
        setOutputMarkupId(true);

        this.actions = formactions;
        this.form = new Form<T>(FORM, model) {
            @SuppressWarnings("unchecked")
            @Override
            protected void onSubmit() {
                super.onSubmit();
                FormPanel.this.actions.submit((IModel<T>) getDefaultModel());
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

        ResourceModel submitModel = new ResourceModel(SUBMIT_LABEL);
        ResourceModel resetModel = new ResourceModel(RESET_LABEL);
        ResourceModel cancelModel = new ResourceModel(CANCEL_LABEL);

        Component submit;

        if (formSettings.ajax) {
            submit = new AjaxSubmitLink(FORM_SUBMIT, form) {
                @SuppressWarnings("unchecked")
                @Override
                protected void onAfterSubmit(AjaxRequestTarget target, Form<?> f) {
                    FormPanel.this.actions.afterSubmit(target, form, (IModel<T>) getDefaultModel());
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
                @SuppressWarnings("unchecked")
                @Override
                protected void onAfterSubmit(AjaxRequestTarget target, Form<?> f) {
                    FormPanel.this.actions.afterCancel(target, form, (IModel<T>) getDefaultModel());
                }
            };
            cancel.setDefaultModel(cancelModel);
            ((AjaxSubmitLink) cancel).setDefaultFormProcessing(false);
        } else {
            cancel = new Button(FORM_CANCEL, cancelModel);
            ((Button) cancel).setDefaultFormProcessing(false);
        }
        cancel.setVisible(formactions.isCancelable());

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

    protected <PropertyType, ModelType, ComponentType extends FormComponent<ModelType>, Rowpanel extends FormRowPanel<PropertyType, ModelType, ComponentType>> void setupRequiredBehavior(
            Rowpanel row) {
        ComponentType component = row.getComponent();
        if (formSettings.isAjax() && formSettings.isLiveValidation() && !(component instanceof PasswordTextField)
                && !(component instanceof com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker)) {
            component.add(setupDynamicRequiredBehavior(row));
        } else {
            component.add(setupStaticRequiredBehavior(row));
        }
    }

    protected <PropertyType, ModelType, ComponentType extends FormComponent<ModelType>, RowPanel extends FormRowPanel<PropertyType, ModelType, ComponentType>> Behavior setupDynamicRequiredBehavior(
            final RowPanel row) {
        return new AjaxFormComponentUpdatingBehavior(HtmlFormEvent.BLUR) {
            @Override
            protected void onError(AjaxRequestTarget ajaxRequestTarget, RuntimeException e) {
                ComponentType component = row.getComponent();
                component.add(new CssClassNameRemover(formSettings.validClass));
                component.add(new CssClassNameAppender(formSettings.invalidClass));
                ajaxRequestTarget.add(component, row.getFeedback());
            }

            @Override
            protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                ComponentType component = row.getComponent();
                component.add(new CssClassNameRemover(formSettings.invalidClass));
                component.add(new CssClassNameAppender(formSettings.validClass));
                ajaxRequestTarget.add(component, row.getFeedback());
            }
        };
    }

    protected <PropertyType, ModelType, ComponentType extends FormComponent<ModelType>, RowPanel extends FormRowPanel<PropertyType, ModelType, ComponentType>> Behavior setupStaticRequiredBehavior(
            @SuppressWarnings("unused") RowPanel row) {
        return new Behavior() {
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

    public <PropertyType, ComponentType extends FormComponent<PropertyType>, RowPanel extends DefaultFormRowPanel<PropertyType, ComponentType>> RowPanel addDefaultRow(
            RowPanel rowpanel) {
        rowpanel.addComponentsTo(repeater);
        setupRequiredBehavior(rowpanel);
        setupId(rowpanel.getPropertyName(), rowpanel.getComponent());
        return rowpanel;
    }

    public <PropertyType, ModelType, ComponentType extends FormComponent<ModelType>, RowPanel extends FormRowPanel<PropertyType, ModelType, ComponentType>> RowPanel addRow(
            RowPanel rowpanel) {
        rowpanel.addComponentsTo(repeater);
        setupRequiredBehavior(rowpanel);
        setupId(rowpanel.getPropertyName(), rowpanel.getComponent());
        return rowpanel;
    }

    public <F> HiddenFieldPanel<F> addHidden(F propertyPath) {
        return addDefaultRow(new HiddenFieldPanel<F>(getDefaultModel(), propertyPath));
    }

    public DatePickerPanel<Date> addDatePicker(Date propertyPath, FormElementSettings componentSettings) {
        return addDatePicker(propertyPath, componentSettings, (Converter<Date, Date>) null);
    }

    public ColorPickerPanel addColorPicker(String propertyPath, ColorPickerSettings componentSettings) {
        return addDefaultRow(new ColorPickerPanel(getDefaultModel(), propertyPath, formSettings, componentSettings));
    }

    @SuppressWarnings("unchecked")
    public <F> DatePickerPanel<F> addDatePicker(F propertyPath, FormElementSettings componentSettings, Converter<F, Date> dateConverter) {
        return addDefaultRow(new DatePickerPanel<F>(getDefaultModel(), propertyPath, dateConverter, formSettings, componentSettings));
    }

    public <F> RadioButtonsPanel<F> addRadioButtons(F propertyPath, FormElementSettings componentSettings, ListModel<F> choices,
            IChoiceRenderer<F> renderer) {
        return addDefaultRow(new RadioButtonsPanel<F>(getDefaultModel(), propertyPath, formSettings, componentSettings, choices, renderer));
    }

    public <F> DropDownPanel<F> addDropDown(F propertyPath, FormElementSettings componentSettings, ListModel<F> choices, IChoiceRenderer<F> renderer) {
        return addDefaultRow(new DropDownPanel<F>(getDefaultModel(), propertyPath, formSettings, componentSettings, choices, renderer));
    }

    public <F> TextFieldPanel<F> addTextField(F propertyPath, FormElementSettings componentSettings) {
        return addDefaultRow(new TextFieldPanel<F>(getDefaultModel(), propertyPath, formSettings, componentSettings));
    }

    public <F> TextAreaPanel<F> addTextArea(F propertyPath, TextAreaSettings componentSettings) {
        return addDefaultRow(new TextAreaPanel<F>(getDefaultModel(), propertyPath, formSettings, componentSettings));
    }

    public <N extends Number & Comparable<N>> NumberFieldPanel<N> addNumberField(N propertyPath, NumberFieldSettings<N> componentSettings) {
        return addDefaultRow(new NumberFieldPanel<N>(getDefaultModel(), propertyPath, formSettings, componentSettings));
    }

    public <N extends Number & Comparable<N>> RangeFieldPanel<N> addRangeField(N propertyPath, RangeFieldSettings<N> componentSettings) {
        return addDefaultRow(new RangeFieldPanel<N>(getDefaultModel(), propertyPath, formSettings, componentSettings));
    }

    public CheckBoxPanel addCheckBox(Boolean propertyPath, FormElementSettings componentSettings) {
        return addDefaultRow(new CheckBoxPanel(getDefaultModel(), propertyPath, formSettings, componentSettings));
    }

    public EmailTextFieldPanel addEmailTextField(String propertyPath, FormElementSettings componentSettings) {
        return addDefaultRow(new EmailTextFieldPanel(getDefaultModel(), propertyPath, formSettings, componentSettings));
    }

    public PasswordTextFieldPanel addPasswordTextField(String propertyPath, FormElementSettings componentSettings) {
        return addDefaultRow(new PasswordTextFieldPanel(getDefaultModel(), propertyPath, formSettings, componentSettings));
    }

    public <F> FilePickerPanel<F> addFilePicker(F propertyPath, FilePickerSettings componentSettings) {
        return addRow(new FilePickerPanel<F>(propertyPath, formSettings, componentSettings));
    }

    protected static abstract class FormRowPanel<P, T, C extends FormComponent<T>> extends Panel implements FormConstants {
        protected Label label;

        protected IModel<String> labelModel;

        protected IModel<T> valueModel;

        /** lambda path */
        protected transient P propertyPath;

        protected Class<T> propertyType;

        protected String propertyName;

        protected FeedbackPanel feedbackPanel;

        protected WebMarkupContainer container;

        protected C component;

        protected transient FormSettings formSettings;

        protected FormElementSettings componentSettings;

        public FormRowPanel(P propertyPath, IModel<T> valueModel, FormSettings formSettings, FormElementSettings componentSettings) {
            this(valueModel, propertyPath, formSettings, componentSettings);
            this.valueModel = valueModel;
        }

        protected FormRowPanel(IModel<?> model, P propertyPath, FormSettings formSettings, FormElementSettings componentSettings) {
            super(FORM_ROW, model);
            this.formSettings = formSettings;
            this.componentSettings = componentSettings;
            this.propertyPath = propertyPath;
            setOutputMarkupPlaceholderTag(false);
            setRenderBodyOnly(true);
            setOutputMarkupId(false);
        }

        protected Label getLabel() {
            if (label == null) {
                label = new Label(LABEL, getLabelModel()) {
                    @Override
                    public boolean isVisible() {
                        return super.isVisible() && formSettings.isShowLabel();
                    }

                    @Override
                    protected void onComponentTag(ComponentTag tag) {
                        super.onComponentTag(tag);
                        tag.getAttributes().put(FOR, getPropertyName());
                    }
                };
            }
            return label;
        }

        protected C getComponent() {
            if (component == null) {
                component = createComponent(getValueModel(), getPropertyType());
                setupRequired(component);
            }
            return this.component;
        }

        protected abstract C createComponent(IModel<T> model, Class<T> valueType);

        protected FeedbackPanel getFeedback() {
            if (feedbackPanel == null) {
                feedbackPanel = new FeedbackPanel(FEEDBACK_ID, new ComponentFeedbackMessageFilter(component));
                feedbackPanel.setOutputMarkupId(true);
            }
            return feedbackPanel;
        }

        protected WebMarkupContainer getContainer(RepeatingView repeater) {
            if (container == null) {
                container = createContainer(repeater);
            }
            return container;
        }

        protected WebMarkupContainer addComponentsTo(RepeatingView repeater) {
            this.add(getLabel());
            this.add(getComponent());
            this.add(getFeedback());
            WebMarkupContainer rowContainer = getContainer(repeater);
            repeater.add(rowContainer);
            rowContainer.add(this);
            return rowContainer;
        }

        protected void tag(ComponentTag tag, String tagId, Object value) {
            if (value == null || (value instanceof String && StringUtils.isBlank(String.class.cast(value)))) {
                tag.getAttributes().remove(tagId);
            } else {
                tag.getAttributes().put(tagId, value);
            }
        }

        protected void setupPlaceholder(ComponentTag tag) {
            tag(tag, PLACEHOLDER, getPlaceholderText());
        }

        protected void setupRequired(ComponentTag tag) {
            tag(tag, REQUIRED, isRequired());
        }

        public boolean isRequired() {
            return componentSettings.isRequired() && formSettings.isClientsideRequiredValidation();
        }

        protected void setupRequired(C component) {
            component.setRequired(isRequired());
            if (StringUtils.isNotBlank(formSettings.getRequiredClass())) {
                if (isRequired()) {
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
            try {
                return getString(getPropertyName());
            } catch (MissingResourceException ex) {
                logger.error("no translation for " + getPropertyName());
                return "[" + getPropertyName() + "]";
            }
        }

        protected String getPlaceholderText() {
            try {
                return getString(PLACEHOLDER);
            } catch (MissingResourceException ex) {
                logger.error("no translation for " + PLACEHOLDER);
                return null;
            }
        }

        protected FormElementSettings getComponentSettings() {
            return this.componentSettings;
        }

        public IModel<String> getLabelModel() {
            if (labelModel == null) {
                labelModel = new LoadableDetachableModel<String>() {
                    @Override
                    protected String load() {
                        return getLabelText();
                    }
                };
            }
            return labelModel;
        }

        public void setLabelModel(IModel<String> labelModel) {
            this.labelModel = labelModel;
        }

        public String getPropertyName() {
            if (propertyName == null) {
                propertyName = name(propertyPath);
            }
            return this.propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public IModel<T> getValueModel() {
            if (valueModel == null) {
                throw new NullPointerException();
            }
            return this.valueModel;
        }

        public void setValueModel(IModel<T> valueModel) {
            this.valueModel = valueModel;
        }

        public Class<T> getPropertyType() {
            if (propertyType == null) {
                throw new NullPointerException();
            }
            return this.propertyType;
        }

        public void setPropertyType(Class<T> propertyType) {
            this.propertyType = propertyType;
        }
    }

    protected static abstract class DefaultFormRowPanel<T, C extends FormComponent<T>> extends FormRowPanel<T, T, C> {
        public DefaultFormRowPanel(IModel<?> model, T propertyPath, FormSettings formSettings, FormElementSettings componentSettings) {
            super(model, propertyPath, formSettings, componentSettings);
        }

        @Override
        public IModel<T> getValueModel() {
            if (valueModel == null) {
                valueModel = new PropertyModel<T>(getDefaultModel(), getPropertyName());
            }
            return valueModel;
        }

        @Override
        public Class<T> getPropertyType() {
            if (propertyType == null) {
                this.propertyType = type(propertyPath);
            }
            return this.propertyType;
        }
    }
}

package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.wicket.HtmlEvent.HtmlFormEvent;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

@SuppressWarnings({ "serial", "unused" })
public class FormPanel<T extends Serializable> extends Panel implements FormConstants {
    protected static final Logger logger = LoggerFactory.getLogger(FormPanel.class);

    protected RepeatingView repeater;

    protected FormSettings formSettings;

    protected FormActions<T> formActions;

    protected Form<T> form;

    public FormPanel(String id) {
        this(id, new DefaultFormActions<T>(), new FormSettings());
    }

    public FormPanel(String id, FormActions<T> formActions, FormSettings formSettings) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        setRenderBodyOnly(false);
        setOutputMarkupId(true);
        setFormActions(formActions);
        setFormSettings(formSettings);
    }

    protected FormActions<T> getFormActions() {
        if (formActions == null) {
            throw new RuntimeException("FormActions required");
        }
        return this.formActions;
    }

    public void setFormActions(FormActions<T> formActions) {
        if (formActions == null) {
            throw new RuntimeException("FormActions required");
        }
        this.formActions = formActions;
    }

    public FormSettings getFormSettings() {
        if (formSettings == null) {
            throw new RuntimeException("FormSettings required");
        }
        return this.formSettings;
    }

    public void setFormSettings(FormSettings formSettings) {
        if (formSettings == null) {
            throw new RuntimeException("FormSettings required");
        }
        this.formSettings = formSettings;
    }

    public Form<T> getForm() {
        if (form == null) {
            getFormActions();
            getFormSettings();
            IModel<T> formModel = new LoadableDetachableModel<T>() {
                @Override
                protected T load() {
                    return getFormActions().loadObject();
                }
            };
            this.form = new Form<T>(FORM, formModel) {
                @Override
                protected void onSubmit() {
                    onBeforeSubmit();
                    getFormActions().submitModel(getFormModel());
                    onAfterSubmit();
                }
            };

            if (getFormSettings().isInheritId()) {
                form.setMarkupId(form.getId());
            }

            form.setOutputMarkupPlaceholderTag(true);
            form.setRenderBodyOnly(false);
            form.setOutputMarkupId(true);

            add(form);

            WebMarkupContainer formHeader = new WebMarkupContainer(FORM_HEADER);
            form.add(formHeader);

            WebMarkupContainer formBody = new WebMarkupContainer(FORM_BODY);
            form.add(formBody);
            repeater = createRepeater();
            formBody.add(repeater);

            ResourceModel submitModel = new ResourceModel(SUBMIT_LABEL);
            ResourceModel resetModel = new ResourceModel(RESET_LABEL);
            ResourceModel cancelModel = new ResourceModel(CANCEL_LABEL);

            Component submit;

            if (getFormSettings().isAjax()) {
                submit = new AjaxSubmitLink(FORM_SUBMIT, form) {
                    @SuppressWarnings("unchecked")
                    @Override
                    protected void onAfterSubmit(AjaxRequestTarget target, Form<?> f) {
                        getFormActions().afterSubmit(target, (Form<T>) f, getFormModel());
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
            if (getFormSettings().isAjax()) {
                cancel = new AjaxSubmitLink(FORM_CANCEL, form) {
                    @SuppressWarnings("unchecked")
                    @Override
                    protected void onAfterSubmit(AjaxRequestTarget target, Form<?> f) {
                        getFormActions().afterCancel(target, (Form<T>) f, getFormModel());
                    }
                };
                cancel.setDefaultModel(cancelModel);
                ((AjaxSubmitLink) cancel).setDefaultFormProcessing(false);
            } else {
                cancel = new Button(FORM_CANCEL, cancelModel);
                ((Button) cancel).setDefaultFormProcessing(false);
            }
            cancel.setVisible(getFormSettings().isCancelable());

            if (getFormSettings().isInheritId()) {
                submit.setMarkupId(getId() + "." + FORM_SUBMIT);
                reset.setMarkupId(getId() + "." + FORM_RESET);
                cancel.setMarkupId(getId() + "." + FORM_CANCEL);
            }

            WebMarkupContainer formActionsContainer = new WebMarkupContainer(FORM_ACTIONS);
            form.add(formActionsContainer);
            formActionsContainer.add(submit);
            formActionsContainer.add(reset);
            formActionsContainer.add(cancel);

            WebMarkupContainer formFooter = new WebMarkupContainer(FORM_FOOTER);
            form.add(formFooter);
        }
        return form;
    }

    public IModel<T> getFormModel() {
        return getForm().getModel();
    }

    @SuppressWarnings("rawtypes")
    protected void onBeforeSubmit() {
        repeater.visitChildren(FormRowPanel.class, new IVisitor<FormRowPanel, Void>() {
            @Override
            public void component(FormRowPanel object, IVisit<Void> visit) {
                if (object instanceof FormSubmitInterceptor) {
                    FormSubmitInterceptor.class.cast(object).onBeforeSubmit();
                }
            }
        });
    }

    @SuppressWarnings("rawtypes")
    protected void onAfterSubmit() {
        repeater.visitChildren(FormRowPanel.class, new IVisitor<FormRowPanel, Void>() {
            @Override
            public void component(FormRowPanel object, IVisit<Void> visit) {
                if (object instanceof FormSubmitInterceptor) {
                    FormSubmitInterceptor.class.cast(object).onAfterSubmit();
                }
            }
        });
    }

    protected static WebMarkupContainer createContainer(RepeatingView repeater) {
        WebMarkupContainer container = new WebMarkupContainer(repeater.newChildId());
        container.setOutputMarkupPlaceholderTag(false);
        container.setRenderBodyOnly(true);
        container.setOutputMarkupId(false);
        return container;
    }

    protected RepeatingView createRepeater() {
        RepeatingView repeatingView = new RepeatingView(FORM_REPEATER);
        repeatingView.setOutputMarkupPlaceholderTag(true);
        repeatingView.setRenderBodyOnly(false);
        repeatingView.setOutputMarkupId(true);
        return repeatingView;
    }

    protected <PropertyType, ModelType, ComponentType extends FormComponent<ModelType>, Rowpanel extends FormRowPanel<PropertyType, ModelType, ComponentType>> void setupRequiredBehavior(
            Rowpanel row) {
        ComponentType component = row.getComponent();
        if (getFormSettings().isAjax() && getFormSettings().isLiveValidation() && !(component instanceof PasswordTextField)
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
                component.add(new CssClassNameRemover(getFormSettings().validClass));
                component.add(new CssClassNameAppender(getFormSettings().invalidClass));
                ajaxRequestTarget.add(component, row.getFeedback());
            }

            @Override
            protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                ComponentType component = row.getComponent();
                component.add(new CssClassNameRemover(getFormSettings().invalidClass));
                component.add(new CssClassNameAppender(getFormSettings().validClass));
                ajaxRequestTarget.add(component, row.getFeedback());
            }
        };
    }

    protected <PropertyType, ModelType, ComponentType extends FormComponent<ModelType>, RowPanel extends FormRowPanel<PropertyType, ModelType, ComponentType>> Behavior setupStaticRequiredBehavior(
            RowPanel row) {
        return new Behavior() {
            @SuppressWarnings("rawtypes")
            @Override
            public void afterRender(Component c) {
                Response response = c.getResponse();
                StringBuffer asterisktHtml = new StringBuffer(200);
                if (c instanceof FormComponent && ((FormComponent) c).isRequired()) {
                    asterisktHtml.append("<span class=\"fontawesome-asterisk " + getFormSettings().getRequiredMarkerClass() + "\"/>");
                }
                response.write(asterisktHtml);
            }
        };
    }

    protected <F extends FormComponent<?>> void setupId(String property, F component) {
        if (getFormSettings().isInheritId()) {
            component.setMarkupId(property);
        }
    }

    public <PropertyType extends Serializable, ComponentType extends FormComponent<PropertyType>, RowPanel extends DefaultFormRowPanel<PropertyType, ComponentType>> RowPanel addDefaultRow(
            RowPanel rowpanel) {
        return addRow(rowpanel);
    }

    public <PropertyType, ModelType, ComponentType extends FormComponent<ModelType>, RowPanel extends FormRowPanel<PropertyType, ModelType, ComponentType>> RowPanel addCustomRow(
            RowPanel rowpanel) {
        return addRow(rowpanel);
    }

    protected <PropertyType, ModelType, ComponentType extends FormComponent<ModelType>, RowPanel extends FormRowPanel<PropertyType, ModelType, ComponentType>> RowPanel addRow(
            RowPanel rowpanel) {
        rowpanel.addComponents();
        rowpanel.addThisTo(repeater);
        setupRequiredBehavior(rowpanel);
        setupId(rowpanel.getPropertyName(), rowpanel.getComponent());
        return rowpanel;
    }

    public static interface FormSubmitInterceptor {
        public void onBeforeSubmit();

        public void onAfterSubmit();
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

        protected void addComponents() {
            this.add(getLabel());
            this.add(getComponent());
            this.add(getFeedback());
        }

        protected WebMarkupContainer addThisTo(RepeatingView repeater) {
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
            tag(tag, REQUIRED, (componentSettings.isRequired() && formSettings.isClientsideRequiredValidation()) ? REQUIRED : null);
        }

        protected void setupRequired(C component) {
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
            try {
                return getString(getPropertyName());
            } catch (MissingResourceException ex) {
                logger.info("no translation for " + getPropertyName());
                return "[" + getPropertyName() + "]";
            }
        }

        protected String getPlaceholderText() {
            try {
                return getString(PLACEHOLDER);
            } catch (MissingResourceException ex) {
                logger.info("no translation for " + PLACEHOLDER);
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
                try {
                    propertyName = WebHelper.name(propertyPath);
                } catch (ch.lambdaj.function.argument.ArgumentConversionException ex) {
                    propertyName = propertyPath.toString();
                }
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

    protected static abstract class DefaultFormRowPanel<T extends Serializable, C extends FormComponent<T>> extends FormRowPanel<T, T, C> {
        public DefaultFormRowPanel(IModel<?> model, T propertyPath, FormSettings formSettings, FormElementSettings componentSettings) {
            super(model, propertyPath, formSettings, componentSettings);
        }

        public void setValueModel(Model<T> model) {
            valueModel = model;
            getComponent().setModel(model);
        }

        @Override
        public IModel<T> getValueModel() {
            if (valueModel == null) {
                valueModel = new PropertyModel<T>(getDefaultModel(), getPropertyName());
            }
            return valueModel;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Class<T> getPropertyType() {
            if (propertyType == null) {
                try {
                    this.propertyType = WebHelper.type(propertyPath);
                } catch (ch.lambdaj.function.argument.ArgumentConversionException ex) {
                    Type genericSuperclass = getClass().getGenericSuperclass();
                    ParameterizedType parameterizedType = ParameterizedType.class.cast(genericSuperclass);
                    try {
                        this.propertyType = (Class<T>) parameterizedType.getActualTypeArguments()[0];
                    } catch (ClassCastException ex2) {
                        this.propertyType = (Class<T>) Serializable.class;
                    }
                }
            }
            return this.propertyType;
        }
    }

    public <F extends Serializable> HiddenFieldPanel<F> addHidden(F propertyPath) {
        return addDefaultRow(new HiddenFieldPanel<F>(getFormModel(), propertyPath));
    }

    public ColorPickerPanel addColorPicker(String propertyPath, ColorPickerSettings componentSettings) {
        return addDefaultRow(new ColorPickerPanel(getFormModel(), propertyPath, getFormSettings(), componentSettings));
    }

    public <F extends Serializable> RadioButtonsPanel<F> addRadioButtons(F propertyPath, FormElementSettings componentSettings, ListModel<F> choices,
            IChoiceRenderer<F> renderer) {
        return addDefaultRow(new RadioButtonsPanel<F>(getFormModel(), propertyPath, getFormSettings(), componentSettings, choices, renderer));
    }

    public <F extends Serializable> MultiSelectCheckBoxPanel<F> addMultiSelectCheckBox(Collection<F> propertyPath,
            FormElementSettings componentSettings, ListModel<F> choices, IChoiceRenderer<F> renderer) {
        return addCustomRow(new MultiSelectCheckBoxPanel<F>(getFormModel(), propertyPath, getFormSettings(), componentSettings, choices, renderer));
    }

    public <F extends Serializable> DropDownPanel<F> addDropDown(F propertyPath, FormElementSettings componentSettings, ListModel<F> choices,
            IChoiceRenderer<F> renderer) {
        return addDefaultRow(new DropDownPanel<F>(getFormModel(), propertyPath, getFormSettings(), componentSettings, choices, renderer));
    }

    public <F extends Serializable> TextFieldPanel<F> addTextField(F propertyPath, FormElementSettings componentSettings) {
        return addDefaultRow(new TextFieldPanel<F>(getFormModel(), propertyPath, getFormSettings(), componentSettings));
    }

    public <F extends Serializable> TextAreaPanel<F> addTextArea(F propertyPath, TextAreaSettings componentSettings) {
        return addDefaultRow(new TextAreaPanel<F>(getFormModel(), propertyPath, getFormSettings(), componentSettings));
    }

    public <N extends Number & Comparable<N>> NumberFieldPanel<N> addNumberField(N propertyPath, NumberFieldSettings<N> componentSettings) {
        return addDefaultRow(new NumberFieldPanel<N>(getFormModel(), propertyPath, getFormSettings(), componentSettings));
    }

    public <N extends Number & Comparable<N>> RangeFieldPanel<N> addRangeField(N propertyPath, RangeFieldSettings<N> componentSettings) {
        return addDefaultRow(new RangeFieldPanel<N>(getFormModel(), propertyPath, getFormSettings(), componentSettings));
    }

    public CheckBoxPanel addCheckBox(Boolean propertyPath, FormElementSettings componentSettings) {
        return addDefaultRow(new CheckBoxPanel(getFormModel(), propertyPath, getFormSettings(), componentSettings));
    }

    public EmailTextFieldPanel addEmailTextField(String propertyPath, FormElementSettings componentSettings) {
        return addDefaultRow(new EmailTextFieldPanel(getFormModel(), propertyPath, getFormSettings(), componentSettings));
    }

    public PasswordTextFieldPanel addPasswordTextField(String propertyPath, FormElementSettings componentSettings) {
        return addDefaultRow(new PasswordTextFieldPanel(getFormModel(), propertyPath, getFormSettings(), componentSettings));
    }

    /**
     * also "form.setMultiPart(true);" and "form.setMaxSize(Bytes.megabytes(1));"
     */
    public <F> FilePickerPanel<F> addFilePicker(F propertyPath, FilePickerSettings componentSettings, FilePickerHook hook) {
        return addCustomRow(new FilePickerPanel<F>(getFormModel(), propertyPath, getFormSettings(), componentSettings, hook));
    }

    @SuppressWarnings("unchecked")
    public <F extends Serializable> DatePickerPanel<F> addDatePicker(F propertyPath, FormElementSettings componentSettings,
            Converter<F, Date> dateConverter) {
        return addDefaultRow(new DatePickerPanel<F>(getFormModel(), propertyPath, dateConverter, getFormSettings(), componentSettings));
    }

    public DatePickerPanel<Date> addDatePicker(Date propertyPath, FormElementSettings componentSettings) {
        return addDatePicker(propertyPath, componentSettings, (Converter<Date, Date>) null);
    }
}

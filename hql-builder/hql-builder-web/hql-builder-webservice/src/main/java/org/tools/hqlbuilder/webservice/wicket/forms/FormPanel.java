package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;
import org.tools.hqlbuilder.webservice.resources.PocketGrid.PocketGrid;
import org.tools.hqlbuilder.webservice.resources.weloveicons.WeLoveIcons;
import org.tools.hqlbuilder.webservice.wicket.HtmlEvent.HtmlFormEvent;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;
import org.tools.hqlbuilder.webservice.wicket.zuss.ZussResourceReference;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

public class FormPanel<T extends Serializable> extends Panel implements FormConstants {
    private static final long serialVersionUID = -6387604067134639316L;

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

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(new ZussResourceReference(WicketCSSRoot.class, "form.css")));
        response.render(CssHeaderItem.forReference(PocketGrid.POCKET_GRID));
        response.render(CssHeaderItem.forReference(WeLoveIcons.WE_LOVE_ICONS_CSS));
        renderColumnsCss(response);
    }

    protected void renderColumnsCss(IHeaderResponse response) {
        StringBuilder sb = new StringBuilder();
        if (formSettings.getVariation() == FormPanelVariation.label) {
            for (int i = 0; i < formSettings.getColumns(); i++) {
                sb.append(".block-group .block:nth-child(").append((i * 2) + 1).append(") { width: ").append(20 / formSettings.getColumns())
                        .append("%; } ");
                sb.append(".block-group .block:nth-child(").append((i * 2) + 2).append(") { width: ").append((100 - 20) / formSettings.getColumns())
                        .append("%; } ");
            }
        } else {
            for (int i = 0; i < formSettings.getColumns(); i++) {
                sb.append(".block-group .block:nth-child(").append(i + 1).append(") { width: ").append(100 / formSettings.getColumns())
                        .append("%; } ");
            }
        }
        response.render(CssHeaderItem.forCSS(sb.toString(),//
                "css_form_" + getId()));//
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
                private static final long serialVersionUID = -5489467484161698560L;

                @Override
                protected T load() {
                    return getFormActions().loadObject();
                }
            };
            this.form = new Form<T>(FORM, formModel) {
                private static final long serialVersionUID = -5899425422548211723L;

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
                    private static final long serialVersionUID = 1046494045754727027L;

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
                    private static final long serialVersionUID = -8816675271842238444L;

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
            private static final long serialVersionUID = -2678991525434409884L;

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
            @SuppressWarnings("unused") RowPanel row) {
        return new Behavior() {
            private static final long serialVersionUID = -4284643075110091322L;

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

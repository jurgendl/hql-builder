package org.tools.hqlbuilder.webservice.wicket.forms;

import java.util.MissingResourceException;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.wicket.HtmlEvent.HtmlFormEvent;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

public abstract class FormRowPanel<P, T, C extends FormComponent<T>> extends Panel implements FormConstants {
    private static final long serialVersionUID = 5258950770053560483L;

    protected static final Logger logger = LoggerFactory.getLogger(FormRowPanel.class);

    protected Label label;

    protected IModel<String> labelModel;

    protected IModel<T> valueModel;

    /** normally a lambda path */
    protected transient P propertyPath;

    protected Class<T> propertyType;

    protected String propertyName;

    protected FeedbackPanel feedbackPanel;

    protected C component;

    protected final transient FormSettings formSettings;

    protected final FormElementSettings componentSettings;

    public FormRowPanel(String id, P propertyPath, IModel<T> valueModel, FormSettings formSettings, FormElementSettings componentSettings) {
        this(id, valueModel, propertyPath, formSettings, componentSettings);
        this.valueModel = valueModel;
    }

    protected FormRowPanel(String id, IModel<?> model, P propertyPath, FormSettings formSettings, FormElementSettings componentSettings) {
        super(id, model);
        if (formSettings == null) {
            throw new NullPointerException("formSettings");
        }
        if (componentSettings == null) {
            throw new NullPointerException("componentSettings");
        }
        this.formSettings = formSettings;
        this.componentSettings = componentSettings;
        this.propertyPath = propertyPath;
        WebHelper.show(this);
    }

    protected void setupRequiredBehavior() {
        C c = getComponent();
        if (formSettings.isAjax() && formSettings.isLiveValidation() && !(c instanceof PasswordTextField)
                && !(c instanceof com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker)) {
            c.add(setupDynamicRequiredBehavior());
        } else {
            c.add(setupStaticRequiredBehavior());
        }
    }

    protected Behavior setupDynamicRequiredBehavior() {
        return new AjaxFormComponentUpdatingBehavior(HtmlFormEvent.BLUR) {
            private static final long serialVersionUID = -2678991525434409884L;

            @Override
            protected void onError(AjaxRequestTarget ajaxRequestTarget, RuntimeException e) {
                C c = FormRowPanel.this.getComponent();
                c.add(new CssClassNameRemover(formSettings.validClass));
                c.add(new CssClassNameAppender(formSettings.invalidClass));
                ajaxRequestTarget.add(c, getFeedback());
            }

            @Override
            protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                C c = FormRowPanel.this.getComponent();
                c.add(new CssClassNameRemover(formSettings.invalidClass));
                c.add(new CssClassNameAppender(formSettings.validClass));
                ajaxRequestTarget.add(c, getFeedback());
            }
        };
    }

    protected Behavior setupStaticRequiredBehavior() {
        return new Behavior() {
            private static final long serialVersionUID = -4284643075110091322L;

            @SuppressWarnings("rawtypes")
            @Override
            public void afterRender(Component c) {
                Response response = c.getResponse();
                StringBuffer asterisktHtml = new StringBuffer(200);
                if (c instanceof FormComponent && ((FormComponent) c).isRequired()) {
                    asterisktHtml.append("<span class=\"fontawesome-asterisk " + formSettings.getRequiredMarkerClass() + "\"/>");
                }
                response.write(asterisktHtml);
            }
        };
    }

    protected void setupId() {
        if (formSettings.isInheritId()) {
            getComponent().setMarkupId(getPropertyName());
        }
    }

    protected Label getLabel() {
        if (formSettings.getVariation() == FormPanelVariation.label && label == null) {
            label = new Label(LABEL, getLabelModel()) {
                private static final long serialVersionUID = 8512361193054906821L;

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

    protected FormRowPanel<P, T, C> addComponents() {
        this.add(getLabel());
        this.add(getComponent());
        this.add(getFeedback());
        return this;
    }

    protected FormRowPanel<P, T, C> afterAddComponents() {
        setupRequiredBehavior();
        setupId();
        return this;
    }

    @Override
    public MarkupContainer add(Component... childs) {
        if (childs == null || childs.length == 0 || (childs.length == 1 && childs[0] == null)) {
            return this;
        }
        return super.add(childs);
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
                private static final long serialVersionUID = -6695403939068552376L;

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

    @Override
    public String getVariation() {
        return formSettings == null ? null : formSettings.getVariation().toString();
    }
}

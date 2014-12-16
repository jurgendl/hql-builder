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
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.wicket.HtmlEvent.HtmlFormEvent;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

public abstract class FormRowPanel<P, T, C extends FormComponent<T>, ElementSettings extends AbstractFormElementSettings<ElementSettings>> extends
        Panel implements FormConstants {
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

    protected FormSettings formSettings;

    protected ElementSettings componentSettings;

    public FormRowPanel(P propertyPath, IModel<T> valueModel, FormSettings formSettings, ElementSettings componentSettings) {
        this(valueModel, propertyPath, formSettings, componentSettings);
        this.valueModel = valueModel;
    }

    protected FormRowPanel(IModel<?> model, P propertyPath, FormSettings formSettings, ElementSettings componentSettings) {
        super(FORM_ELEMENT, model);
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
            // c.add(setupDynamicRequiredBehavior());
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

    protected void setupId() {
        if (formSettings.isInheritId() || componentSettings.isInheritId()) {
            inheritId();
        }
    }

    public Label getLabel() {
        if (label == null) {
            label = new Label(LABEL, getLabelModel()) {
                private static final long serialVersionUID = 8512361193054906821L;

                @Override
                public boolean isVisible() {
                    return super.isVisible() && (formSettings == null || formSettings.isShowLabel());
                }

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    tag.getAttributes().put(FOR, getComponent().getMarkupId());
                    tag.getAttributes().put(TITLE, getLabelModel().getObject());
                }
            };
        }
        return label;
    }

    public C getFormComponent() {
        return getComponent();
    }

    public C getComponent() {
        if (component == null) {
            component = createComponent(getValueModel(), getPropertyType());
            setupRequired(component);
        }
        return this.component;
    }

    protected abstract C createComponent(IModel<T> model, Class<T> valueType);

    protected FeedbackPanel getFeedback() {
        if (feedbackPanel == null) {
            feedbackPanel = new FeedbackPanel(FEEDBACK_ID, new ComponentFeedbackMessageFilter(component)) {
                private static final long serialVersionUID = 211849904711387432L;

                @Override
                public boolean isVisible() {
                    return super.isVisible() && anyMessage();
                }
            };
            feedbackPanel.setOutputMarkupId(true);
        }
        return feedbackPanel;
    }

    protected FormRowPanel<P, T, C, ElementSettings> addComponents() {
        this.add(getLabel());
        this.add(getComponent());
        this.add(getRequiredMarker());
        this.add(getFeedback());
        return this;
    }

    protected WebMarkupContainer getRequiredMarker() {
        WebMarkupContainer requiredMarker = new WebMarkupContainer("requiredMarker") {
            private static final long serialVersionUID = -6386826366809908431L;

            @Override
            public boolean isVisible() {
                return super.isVisible() && componentSettings.isRequired();
            }
        };
        return requiredMarker;
    }

    @SuppressWarnings("unchecked")
    protected FormRowPanel<P, T, C, ElementSettings> afterAddComponents() {
        getComponent().setLabel((IModel<String>) getLabel().getDefaultModel());
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

    protected void setupPlaceholder(ComponentTag tag) {
        if (componentSettings != null && !componentSettings.isShowPlaceholder()) {
            WebHelper.untag(tag, PLACEHOLDER);
        } else if (componentSettings != null && componentSettings.isShowPlaceholder()) {
            WebHelper.tag(tag, PLACEHOLDER, getPlaceholderText());
        } else if (formSettings != null && formSettings.isShowPlaceholder()) {
            WebHelper.tag(tag, PLACEHOLDER, getPlaceholderText());
        } else {
            WebHelper.untag(tag, PLACEHOLDER);
        }
    }

    protected void setupRequired(ComponentTag tag) {
        if (formSettings != null && formSettings.isClientsideRequiredValidation() && componentSettings != null && componentSettings.isRequired()) {
            WebHelper.tag(tag, REQUIRED, REQUIRED);
        } else {
            WebHelper.untag(tag, REQUIRED);
        }
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
     * org.tools.hqlbuilder.webservice.wicket.forms.[Component]Panel. createComponent().new [Component]() {...}.onComponentTag(ComponentTag)
     */
    protected void onFormComponentTag(ComponentTag tag) {
        setupPlaceholder(tag);
        setupRequired(tag);
    }

    protected String getLabelText() {
        try {
            return getString(getPropertyName());
        } catch (MissingResourceException ex) {
            logger.warn("no translation for " + getPropertyName());
            return "[" + getPropertyName() + "_" + getLocale() + "]";
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

    public ElementSettings getComponentSettings() {
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

    public FormRowPanel<P, T, C, ElementSettings> setLabelModel(IModel<String> labelModel) {
        this.labelModel = labelModel;
        return this;
    }

    public String getPropertyName() {
        if (propertyName == null) {
            try {
                propertyName = WebHelper.name(propertyPath);
            } catch (ch.lambdaj.function.argument.ArgumentConversionException ex) {
                propertyName = propertyPath == null ? null : propertyPath.toString();
            }
        }
        return this.propertyName;
    }

    public FormRowPanel<P, T, C, ElementSettings> setPropertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    public IModel<T> getValueModel() {
        if (valueModel == null) {
            throw new NullPointerException();
        }
        return this.valueModel;
    }

    public FormRowPanel<P, T, C, ElementSettings> setValueModel(IModel<T> valueModel) {
        this.valueModel = valueModel;
        return this;
    }

    public Class<T> getPropertyType() {
        if (propertyType == null) {
            throw new NullPointerException();
        }
        return this.propertyType;
    }

    public FormRowPanel<P, T, C, ElementSettings> setPropertyType(Class<T> propertyType) {
        this.propertyType = propertyType;
        return this;
    }

    public boolean takesUpSpace() {
        return true;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_JS));
    }

    public FormRowPanel<P, T, C, ElementSettings> inheritId() {
        // . is replaced because sql selectors don't work well with dot's
        getComponent().setMarkupId(getPropertyName().toString().replace('.', DOT_REPLACER));
        return this;
    }
}

package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.tools.hqlbuilder.common.CommonUtils;

import com.googlecode.wicket.jquery.core.renderer.ITextRenderer;
import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;

public class AutoCompleteTextFieldPanel<T extends Serializable> extends DefaultFormRowPanel<T, TextField<T>, AutoCompleteTextFieldSettings> {
    private static final long serialVersionUID = -7993592150932306594L;

    protected ListModel<T> choices = null;

    protected ITextRenderer<T> renderer = null;

    public AutoCompleteTextFieldPanel(final IModel<?> model, final T propertyPath, FormSettings formSettings,
            AutoCompleteTextFieldSettings componentSettings, ListModel<T> choices, ITextRenderer<T> renderer) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = renderer;
    }

    @Override
    protected TextField<T> createComponent(IModel<T> model, Class<T> valueType) {
        AutoCompleteTextField<T> textField = new AutoCompleteTextField<T>(VALUE, model, renderer, valueType) {
            private static final long serialVersionUID = -3401872343978963488L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }

            @Override
            protected List<T> getChoices(String input) {
                List<T> show = new ArrayList<T>();
                if (StringUtils.isBlank(input) || input.length() < getComponentSettings().getMinLenght()) {
                    return show;
                }
                String search = getComponentSettings().isNormalize() ? CommonUtils.sortable(input) : input;
                for (T choice : choices.getObject()) {
                    String text = renderer.getText(choice);
                    if (getComponentSettings().isNormalize()) {
                        text = CommonUtils.sortable(text);
                    }
                    if (getComponentSettings().isContains()) {
                        if (text.contains(search)) {
                            show.add(choice);
                        }
                    } else {
                        if (text.startsWith(search)) {
                            show.add(choice);
                        }
                    }
                    if (show.size() >= getComponentSettings().getMaxResults()) {
                        break;
                    }
                }
                return show;
            }
        };

        return textField;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(new JavaScriptContentHeaderItem("$(function() { $( \"#" + getComponent().getMarkupId() + "\" ).puiinputtext(); });", "js_"
                + getComponent().getMarkupId() + "_" + System.currentTimeMillis(), null));
    }
}

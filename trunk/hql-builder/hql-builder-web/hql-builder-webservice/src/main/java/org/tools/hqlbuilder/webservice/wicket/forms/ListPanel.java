package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

/**
 * @see http://www.primefaces.org/primeui/listbox.html
 */
public class ListPanel<T extends Serializable> extends SelectPanel<T, Select<T>, ListSettings> {
    private static final long serialVersionUID = 6519523561212631975L;

    public ListPanel(IModel<?> model, T propertyPath, FormSettings formSettings, ListSettings componentSettings, IOptionRenderer<T> renderer,
            ListModel<T> choices) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices);
    }

    public ListPanel(IModel<?> model, T propertyPath, FormSettings formSettings, ListSettings componentSettings, IOptionRenderer<T> renderer,
            ListModel<T>[] choices, IModel<String>[] groupLabels) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices, groupLabels);
    }

    @Override
    protected Select<T> createComponent(IModel<T> model) {
        if (getComponentSettings().getSize() <= 1) {
            throw new IllegalArgumentException("getComponentSettings().getSize()<=1");
        }
        return new Select<T>(VALUE, model) {
            private static final long serialVersionUID = 6509470567166194399L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
                if (getComponentSettings().isMultiple()) {
                    tag(tag, "multiple", "multiple");
                }
                tag(tag, "size", getComponentSettings().getSize());
                tag(tag, "style", "height: auto");
            }
        };
    }

    @Override
    protected boolean isNullValid() {
        return getComponentSettings().isNullValid();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(OnLoadHeaderItem.forScript("$( \"#" + getComponent().getMarkupId() + "\" ).puilistbox();"));
    }
}

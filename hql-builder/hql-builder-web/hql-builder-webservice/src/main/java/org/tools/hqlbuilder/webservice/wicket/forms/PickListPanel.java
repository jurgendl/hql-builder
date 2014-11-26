package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

/**
 * @see http://www.primefaces.org/primeui/picklist.html
 */
public class PickListPanel<T extends Serializable> extends FormRowPanel<Collection<T>, Collection<T>, ListMultipleChoice<T>, ListSettings> {
    private static final long serialVersionUID = 7874409832671834238L;

    protected IModel<List<T>> choices;

    protected IChoiceRenderer<T> renderer;

    protected PickListPanel(IModel<?> model, Collection<T> propertyPath, FormSettings formSettings, ListSettings componentSettings,
            IModel<List<T>> choices, IChoiceRenderer<T> renderer) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = renderer;
    }

    @Override
    protected ListMultipleChoice<T> createComponent(final IModel<Collection<T>> selection, Class<Collection<T>> valueType) {
        IModel<List<T>> selectionListModel = new IModel<List<T>>() {
            private static final long serialVersionUID = 7030770200558777598L;

            @Override
            public void detach() {
                selection.detach();
            }

            @Override
            public List<T> getObject() {
                return new ArrayList<T>(selection.getObject());
            }

            @Override
            public void setObject(List<T> object) {
                selection.setObject(object);
            }
        };
        ListMultipleChoice<T> source = new ListMultipleChoice<T>("target", selectionListModel, selectionListModel, renderer);
        return source;
    }

    @Override
    protected FormRowPanel<Collection<T>, Collection<T>, ListMultipleChoice<T>, ListSettings> addComponents() {
        this.add(getLabel());
        add(//
                new WebMarkupContainer("puipicklist")//
                .add(new ListMultipleChoice<T>("source", choices, choices, renderer))//
                .add(getComponent()));//
        this.add(getRequiredMarker());
        this.add(getFeedback());
        return this;
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_FACTORY_JS));
    }

    @Override
    public IModel<Collection<T>> getValueModel() {
        if (valueModel == null) {
            String property = getPropertyName();
            valueModel = property == null ? null : new PropertyModel<Collection<T>>(getDefaultModel(), property);
        }
        return valueModel;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Class<Collection<T>> getPropertyType() {
        return (Class) Collection.class;
    }
}

package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.Strings;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://www.primefaces.org/primeui/picklist.html
 */
public class PickListPanel<T extends Serializable> extends FormRowPanel<Collection<T>, Collection<T>, ListMultipleChoice<T>, ListSettings> {
    private static final long serialVersionUID = 7874409832671834238L;

    protected IModel<List<T>> choices;

    protected IChoiceRenderer<T> renderer;

    protected PickListPanel(IModel<?> model, Collection<T> propertyPath, FormSettings formSettings, ListSettings componentSettings,
            IModel<List<T>> choices, final IChoiceRenderer<T> renderer) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = new IChoiceRenderer<T>() {
            private static final long serialVersionUID = 9002126185670506302L;

            @Override
            public Object getDisplayValue(T object) {
                Object tmp = object;
                if (renderer != null) {
                    tmp = renderer.getDisplayValue(object);
                }
                return String.valueOf(tmp);
            }

            @Override
            public String getIdValue(T object, int index) {
                Object displayValue = getDisplayValue(object);
                return String.valueOf(displayValue);
            }
        };
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
        ListMultipleChoice<T> source = new ListMultipleChoice<T>("target", selectionListModel, selectionListModel, renderer) {
            private static final long serialVersionUID = -4637460687004256289L;

            @Override
            protected List<T> convertChoiceIdsToChoices(String[] ids) {
                ArrayList<T> selectedValues = new ArrayList<T>();

                // If one or more ids is selected
                if (ids != null && ids.length > 0 && !Strings.isEmpty(ids[0])) {
                    // Get values that could be selected
                    final Map<String, T> choiceIds2choiceValues = createChoicesIdsMap();

                    // Loop through selected indices
                    for (String id : ids) {
                        if (choiceIds2choiceValues.containsKey(id)) {
                            selectedValues.add(choiceIds2choiceValues.get(id));
                        }
                    }
                }
                return selectedValues;
            }

            private Map<String, T> createChoicesIdsMap() {
                final List<? extends T> _choices = getChoices();
                List<T> otherOptions = choices.getObject();
                final Map<String, T> choiceIds2choiceValues = new HashMap<String, T>(_choices.size() + otherOptions.size(), 1);
                for (int index = 0; index < _choices.size(); index++) {
                    // Get next choice
                    final T choice = _choices.get(index);
                    choiceIds2choiceValues.put(getChoiceRenderer().getIdValue(choice, index), choice);
                }
                for (int index = 0; index < otherOptions.size(); index++) {
                    // Get next choice
                    final T choice = otherOptions.get(index);
                    choiceIds2choiceValues.put(getChoiceRenderer().getIdValue(choice, index), choice);
                }
                return choiceIds2choiceValues;
            }
        };
        return source;
    }

    @Override
    protected FormRowPanel<Collection<T>, Collection<T>, ListMultipleChoice<T>, ListSettings> addComponents() {
        this.add(getLabel());
        add(//
                new WebMarkupContainer("puipicklist")//
                .add(new ListMultipleChoice<T>("source", choices/* new ListModel<T>(new ArrayList<T>()) */, choices, renderer))//
                .add(getComponent())//
                .add(new CssClassNameAppender(PrimeUI.puipicklist)));//
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

package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public abstract class FormRowPanel<T> extends Panel {
    private static final long serialVersionUID = -6401309948019996576L;

    protected static final String VALUE = "value";

    protected static final String PLACEHOLDER = "placeholder";

    protected static final String LABEL = "label";

    protected static final String FOR = "for";

    protected static final String FORM_ROW = "form.row";

    protected final Label label;

    protected final String property;

    protected final Class<T> type;

    public FormRowPanel(final IModel<?> model, final String property, final Class<T> type) {
        super(FORM_ROW, model);
        this.property = property;
        this.type = type;
        setOutputMarkupPlaceholderTag(false);
        setRenderBodyOnly(true);
        setOutputMarkupId(false);
        label = addLabel();
        addComponent();
    }

    protected abstract void addComponent();

    protected Label addLabel() {
        AbstractReadOnlyModel<String> labelModel = new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = -6461211838443556886L;

            @Override
            public String getObject() {
                return getLabel();
            }
        };
        Label labelComponent = new Label(LABEL, labelModel) {
            private static final long serialVersionUID = -4486835664954887226L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.getAttributes().put(FOR, property);
            }
        };
        add(labelComponent);
        return labelComponent;
    }

    protected void setPlaceholder(ComponentTag tag) {
        tag.getAttributes().put(PLACEHOLDER, getPlaceholder());
    }

    protected String getLabel() {
        return property;
    }

    protected String getPlaceholder() {
        return getString(PLACEHOLDER);
    }

    protected PropertyModel<T> getValueModel() {
        return new PropertyModel<T>(getDefaultModel(), property);
    }
}

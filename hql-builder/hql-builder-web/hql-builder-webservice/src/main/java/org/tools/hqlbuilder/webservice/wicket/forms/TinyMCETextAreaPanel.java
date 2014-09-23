package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;

import wicket.contrib.tinymce.settings.TinyMCESettings;

/**
 * @see http://wicket.apache.org/guide/guide/wicketstuff.html#wicketstuff_2
 */
public class TinyMCETextAreaPanel<T extends Serializable> extends DefaultFormRowPanel<T, TextArea<T>, TinyMCETextAreaSettings> {
    private static final long serialVersionUID = -2084541282171331186L;

    public TinyMCETextAreaPanel(final IModel<?> model, final T propertyPath, FormSettings formSettings,
            TinyMCETextAreaSettings TinyMCETextAreaSettings) {
        super(model, propertyPath, formSettings, TinyMCETextAreaSettings);
    }

    @Override
    protected TextArea<T> createComponent(IModel<T> model, Class<T> valueType) {
        TextArea<T> textArea = new TextArea<T>(VALUE, model) {
            private static final long serialVersionUID = 7135822567688476733L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
        TinyMCESettings tinyMCESettings = componentSettings.getTinyMCESettings();
        textArea.add(new wicket.contrib.tinymce.TinyMceBehavior(tinyMCESettings));
        return textArea;
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }
}

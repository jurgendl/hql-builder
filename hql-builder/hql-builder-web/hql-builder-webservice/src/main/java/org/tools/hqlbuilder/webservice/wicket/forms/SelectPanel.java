package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.extensions.markup.html.form.select.SelectOptions;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;

public abstract class SelectPanel<T extends Serializable, C extends Select<T>, S extends FormElementSettings> extends DefaultFormRowPanel<T, C, S> {
    public static final String OPTION = "option";

    public static final String OPTGROUP = "optgroup";

    private static final long serialVersionUID = -6781073146798103698L;

    protected ListModel<T>[] choices;

    protected IModel<String>[] groupLabels;

    protected IOptionRenderer<T> renderer;

    @SuppressWarnings("unchecked")
    public SelectPanel(IModel<?> model, T propertyPath, FormSettings formSettings, S componentSettings, IOptionRenderer<T> renderer,
            ListModel<T> choices) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = new ListModel[] { choices };
        this.renderer = renderer;
    }

    public SelectPanel(IModel<?> model, T propertyPath, FormSettings formSettings, S componentSettings, IOptionRenderer<T> renderer,
            ListModel<T>[] choices, IModel<String>[] groupLabels) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = renderer;
        this.groupLabels = groupLabels;
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }

    @SuppressWarnings("unchecked")
    @Override
    protected C createComponent(IModel<T> model, Class<T> valueType) {
        Select<T> choice = createComponent(model);

        choice.add(new NullOption<T>().setVisible(isNullValid()));

        RepeatingView optgroupRepeater = new RepeatingView(OPTGROUP);
        WebHelper.show(optgroupRepeater);
        choice.add(optgroupRepeater);

        for (int i = 0; i < choices.length; i++) {
            final int I = i;

            WebMarkupContainer optgroupWebcontainer = new WebMarkupContainer(optgroupRepeater.newChildId()) {
                private static final long serialVersionUID = -3644549985638665292L;

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    if (groupLabels != null) {
                        tag.put(LABEL, groupLabels[I].getObject());
                    }
                }
            };
            if (groupLabels != null) {
                WebHelper.show(optgroupWebcontainer);
            } else {
                optgroupWebcontainer.setRenderBodyOnly(true);
            }
            optgroupRepeater.add(optgroupWebcontainer);

            SelectOptions<T> options = new SelectOptions<T>("optionsContainer", this.choices[I], renderer) {
                private static final long serialVersionUID = -7724272123559477783L;

                @Override
                protected SelectOption<T> newOption(final String text, final IModel<? extends T> optModel) {
                    SelectOption<T> selectOption = new SelectOption<T>(OPTION, optModel) {
                        private static final long serialVersionUID = 6450521870585988265L;

                        @Override
                        public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
                            replaceComponentTagBody(markupStream, openTag, text);
                        }

                        @Override
                        protected void onComponentTag(ComponentTag tag) {
                            super.onComponentTag(tag);
                            tag.setType(TagType.OPEN);
                            tag.put(TITLE, text);
                        }
                    };
                    return selectOption;
                }
            };
            optgroupWebcontainer.add(options);
        }

        return (C) choice;
    }

    protected abstract boolean isNullValid();

    protected abstract Select<T> createComponent(IModel<T> model);
}

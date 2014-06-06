package org.tools.hqlbuilder.webservice.wicket.tables;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.name;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.wicket.tables.Table.ActionsPanel;
import org.tools.hqlbuilder.webservice.wicket.tables.Table.EmailColumn;
import org.tools.hqlbuilder.webservice.wicket.tables.Table.URLColumn;

@SuppressWarnings("serial")
public class EnhancedTable<T extends Serializable> extends Panel {
    private static final Logger logger = LoggerFactory.getLogger(Table.class);

    public static final String FORM_ID = "tableform";

    public static final String TABLE_ID = "table";

    public static final String ACTIONS_ID = "table.actions";

    protected final Table<T> table;

    public EnhancedTable(String id, List<IColumn<T, String>> columns, final DataProvider<T> dataProvider) {
        super(id);
        setOutputMarkupId(true);
        Form<?> form = new Form<Object>(FORM_ID);
        add(form);
        table = new Table<T>(form, TABLE_ID, columns, dataProvider);
        form.add(table);
    }

    public Table<T> getTable() {
        return this.table;
    }

    public static <D> IColumn<D, String> newColumn(Component parent, Object argument) {
        return new PropertyColumn<D, String>(labelModel(parent, argument), name(argument), name(argument));
    }

    public static <D> IColumn<D, String> newEmailColumn(Component parent, Object argument) {
        return new EmailColumn<D>(labelModel(parent, argument), name(argument));
    }

    /** {@link URL}, {@link URI} and url as {@link String} supported */
    public static <D> IColumn<D, String> newURLColumn(Component parent, Object argument) {
        return new URLColumn<D>(labelModel(parent, argument), name(argument));
    }

    public static <D> IColumn<D, String> newTimeColumn(Component parent, Object argument) {
        return newDateOrTimeColumn(parent, argument, new DateConverter(true) {
            @Override
            protected DateTimeFormatter getFormat(Locale locale) {
                return DateTimeFormat.longTime().withLocale(locale);
            }

            @Override
            public String getDatePattern(Locale locale) {
                return getFormat(locale).toString();
            }
        });
    }

    public static <D> IColumn<D, String> newDateColumn(Component parent, Object argument) {
        return newDateOrTimeColumn(parent, argument, new DateConverter(true) {
            @Override
            protected DateTimeFormatter getFormat(Locale locale) {
                return DateTimeFormat.longDate().withLocale(locale);
            }

            @Override
            public String getDatePattern(Locale locale) {
                return getFormat(locale).toString();
            }
        });
    }

    public static <D> IColumn<D, String> newDateTimeColumn(Component parent, Object argument) {
        return newDateOrTimeColumn(parent, argument, new DateConverter(true) {
            @Override
            protected DateTimeFormatter getFormat(Locale locale) {
                return DateTimeFormat.longDateTime().withLocale(locale);
            }

            @Override
            public String getDatePattern(Locale locale) {
                return getFormat(locale).toString();
            }
        });
    }

    public static <D> IColumn<D, String> newDateOrTimeColumn(Component parent, Object argument, final DateConverter dateConverter) {
        return new PropertyColumn<D, String>(labelModel(parent, argument), name(argument), name(argument)) {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            public void populateItem(Item<ICellPopulator<D>> item, String componentId, IModel<D> rowModel) {
                item.add(new DateLabel(componentId, (IModel) getDataModel(rowModel), dateConverter));
            }
        };
    }

    public static Model<String> labelModel(Component parent, Object argument) {
        String property = name(argument);
        return labelModel(parent, property);
    }

    public static Model<String> labelModel(Component parent, String property) {
        String label;
        try {
            label = parent.getString(property);
        } catch (MissingResourceException ex) {
            logger.error(parent.getClass().getName() + ": no translation for " + property);
            label = "[" + property + "]";
        }
        return new Model<String>(label);
    }

    public static <T extends Serializable> IColumn<T, String> getActionsColumn(Component parent, final DataProvider<T> provider) {
        return new AbstractColumn<T, String>(labelModel(parent, ACTIONS_ID)) {
            @Override
            @SuppressWarnings({ "rawtypes", "unchecked" })
            public void populateItem(Item cellItem, String componentId, IModel rowModel) {
                T object = ((T) rowModel.getObject());
                cellItem.add(new ActionsPanel<T>(componentId, object) {
                    @Override
                    public void onDelete(AjaxRequestTarget target, T o) {
                        provider.delete(target, o);
                    }

                    @Override
                    public void onEdit(AjaxRequestTarget target, T o) {
                        provider.edit(target, o);
                    }
                });
            }
        };
    }
}

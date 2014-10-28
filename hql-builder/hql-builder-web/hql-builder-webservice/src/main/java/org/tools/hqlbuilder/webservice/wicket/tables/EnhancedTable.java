package org.tools.hqlbuilder.webservice.wicket.tables;

import static org.tools.hqlbuilder.common.CommonUtils.name;

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
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.jquery.ui.tablesorter.TableSorter;
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

    public EnhancedTable(String id, List<TableColumn<T>> columns, final DataProvider<T> dataProvider) {
        super(id);
        setOutputMarkupId(true);
        Form<?> form;
        if (dataProvider.isStateless()) {
            form = new StatelessForm<T>(FORM_ID);
        } else {
            form = new Form<T>(FORM_ID);
        }
        add(form);
        table = new Table<T>(form, TABLE_ID, columns, dataProvider);
        form.add(table);
    }

    public Table<T> getTable() {
        return this.table;
    }

    public static <D> TableColumn<D> newColumn(Component parent, Object argument) {
        return new TableColumn<D>(labelModel(parent, argument), name(argument));
    }

    public static <D> TableColumn<D> newEmailColumn(Component parent, Object argument) {
        return new EmailColumn<D>(labelModel(parent, argument), name(argument));
    }

    /** {@link URL}, {@link URI} and url as {@link String} supported */
    public static <D> TableColumn<D> newURLColumn(Component parent, Object argument) {
        return new URLColumn<D>(labelModel(parent, argument), name(argument));
    }

    public static <D> TableColumn<D> newTimeColumn(Component parent, Object argument) {
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

    public static <D> TableColumn<D> newDateColumn(Component parent, Object argument) {
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

    public static <D> TableColumn<D> newDateTimeColumn(Component parent, Object argument) {
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

    public static <D> TableColumn<D> newDateOrTimeColumn(Component parent, Object argument, final DateConverter dateConverter) {
        return new TableColumn<D>(labelModel(parent, argument), name(argument)) {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            public void populateItem(Item<ICellPopulator<D>> item, String componentId, IModel<D> rowModel) {
                item.add(new DateLabel(componentId, (IModel) getDataModel(rowModel), dateConverter));
            }
        };
    }

    public static IModel<String> labelModel(Component parent, Object argument) {
        String property = name(argument);
        return labelModel(parent, property);
    }

    public static IModel<String> labelModel(final Component parent, final String property) {
        IModel<String> label;
        try {
            label = new IModel<String>() {
                @Override
                public void detach() {
                    //
                }

                @Override
                public String getObject() {
                    return parent.getString(property);
                }

                @Override
                public void setObject(String object) {
                    //
                }
            };
        } catch (MissingResourceException ex) {
            logger.error(parent.getClass().getName() + ": no translation for " + property);
            label = Model.of("[" + property + "_" + parent.getLocale() + "]");
        }
        return label;
    }

    public static <T extends Serializable> TableColumn<T> getActionsColumn(Component parent, final DataProvider<T> provider) {
        return new ActionsColumn<T>(labelModel(parent, ACTIONS_ID), provider);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        // client column sorting
        {
            boolean anyClientSortable = false;
            boolean addComma = false;
            String config = "textExtraction:function(node){return $(node).text();},sortMultiSortKey:'ctrlKey',cssAsc:'wicket_orderDown',cssDesc:'wicket_orderUp',headers:{";
            int i = 0;
            for (IColumn<T, String> column : getTable().getColumns()) {
                boolean clientSortable = ((TableColumn<T>) column).getSorting() == Side.client;
                if (!clientSortable) {
                    config += (addComma ? "," : "") + i + ":{sorter:false}";
                    addComma = true;
                }
                anyClientSortable |= clientSortable;
                i++;
            }
            config += "}";
            if (anyClientSortable) {
                response.render(JavaScriptHeaderItem.forReference(TableSorter.TABLE_SORTER_JS));
                response.render(OnDomReadyHeaderItem.forScript("$('#" + getTable().getMarkupId() + "').tablesorter({" + config + "});"));
            }
        }
    }

    public static class ActionsColumn<T extends Serializable> extends TableColumn<T> {
        protected final DataProvider<T> provider;

        public ActionsColumn(IModel<String> displayModel, final DataProvider<T> provider) {
            setDisplayModel(displayModel);
            this.provider = provider;
        }

        @Override
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public void populateItem(Item cellItem, String componentId, IModel rowModel) {
            T object = ((T) rowModel.getObject());
            cellItem.add(new ActionsPanel<T>(componentId, object, provider) {
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
    }
}

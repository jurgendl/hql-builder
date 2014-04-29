package org.tools.hqlbuilder.webservice.wicket.tables;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.name;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

import org.apache.wicket.Component;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @see https://www.packtpub.com/article/apache-wicket-displaying-data-using-datatable
 */
public class Table<T extends Serializable> extends DefaultDataTable<T, String> {
    private static final long serialVersionUID = -5074672726454953465L;

    public Table(List<IColumn<T, String>> columns, final IDataProvider<T> dataProvider, int max) {
        super("table", columns, new SortableDataProvider<T, String>() {
            private static final long serialVersionUID = -8317851838497601307L;

            @Override
            public Iterator<? extends T> iterator(long first, long count) {
                return dataProvider.iterator(first, count);
            }

            @Override
            public long size() {
                return dataProvider.size();
            }

            @Override
            public IModel<T> model(T object) {
                return Model.of(object);
            }
        }, max);
    }

    public static <D> IColumn<D, String> newColumn(Component parent, Object argument) {
        return new PropertyColumn<D, String>(labelModel(parent, argument), name(argument), name(argument));
    }

    public static <D> IColumn<D, String> newEmailColumn(Component parent, Object argument) {
        return new EmailColumn<D>(labelModel(parent, argument), name(argument));
    }

    public static <D> IColumn<D, String> newTimeColumn(Component parent, Object argument) {
        return newDateOrTimeColumn(parent, argument, new DateConverter(true) {
            private static final long serialVersionUID = 2343896464273737921L;

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
            private static final long serialVersionUID = 7740123878863398108L;

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
            private static final long serialVersionUID = 3387299335035913335L;

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
            private static final long serialVersionUID = -4327523236766929509L;

            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            public void populateItem(Item<ICellPopulator<D>> item, String componentId, IModel<D> rowModel) {
                item.add(new DateLabel(componentId, (IModel) getDataModel(rowModel), dateConverter));
            }
        };
    }

    public static Model<String> labelModel(Component parent, Object argument) {
        String property = name(argument);
        String label;
        try {
            label = parent.getString(property);
        } catch (MissingResourceException ex) {
            // TODO logger.error("no translation for " + property);
            label = "${" + property + "}";
        }
        return new Model<String>(label);
    }

    public static class EmailColumn<D> extends PropertyColumn<D, String> {
        private static final long serialVersionUID = 4348693361586305371L;

        public EmailColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
            super(displayModel, sortProperty, propertyExpression);
        }

        public EmailColumn(IModel<String> displayModel, String propertyExpression) {
            super(displayModel, propertyExpression, propertyExpression);
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public void populateItem(Item<ICellPopulator<D>> item, String componentId, IModel<D> rowModel) {
            item.add(new LinkPanel(componentId, (IModel) getDataModel(rowModel)));
        }

        private class LinkPanel extends Panel {
            private static final long serialVersionUID = -9215479986488566139L;

            public LinkPanel(String id, final IModel<String> model) {
                super(id);
                add(new ExternalLink("link", new AbstractReadOnlyModel<String>() {
                    private static final long serialVersionUID = 11942951671862663L;

                    @Override
                    public String getObject() {
                        return "mailto:" + model.getObject();
                    }
                }, model));
            }
        }
    }
}

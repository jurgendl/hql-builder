package org.tools.hqlbuilder.webservice.wicket.tables;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.name;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigation;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationIncrementLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.string.Strings;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.wicket.ext.ExternalLink;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

/**
 * @see https://www.packtpub.com/article/apache-wicket-displaying-data-using-datatable
 * @see http://wicketinaction.com/2008/10/building-a-listeditor-form-component/
 */
@SuppressWarnings("serial")
public class Table<T extends Serializable> extends AjaxFallbackDefaultDataTable<T, String> {
    private static final Logger logger = LoggerFactory.getLogger(Table.class);

    public static final String ID = "table";

    public static final String ACTIONS_ID = "table.actions";

    public static final String ACTIONS_DELETE_ID = "delete";

    public static final String ACTIONS_EDIT_ID = "edit";

    public static final String ACTIONS_FORM_ID = "rowform";

    public static final String ACTIONS_ADD_ID = "add";

    public String CSS_DISABLED_STYLE = "disabled";

    public Table(List<IColumn<T, String>> columns, final DataProvider<T> dataProvider) {
        super(ID, columns, dataProvider, dataProvider.getRowsPerPage());
        setOutputMarkupId(true);
    }

    public DataProvider<T> getDataprovider() {
        return (DataProvider<T>) getDataProvider();
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

    public static class EmailColumn<D> extends PropertyColumn<D, String> {
        public EmailColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
            super(displayModel, sortProperty, propertyExpression);
        }

        public EmailColumn(IModel<String> displayModel, String propertyExpression) {
            super(displayModel, propertyExpression, propertyExpression);
        }

        @Override
        public void populateItem(Item<ICellPopulator<D>> item, String componentId, IModel<D> rowModel) {
            IModel<Object> dataModel = getDataModel(rowModel);
            @SuppressWarnings("rawtypes")
            IModel dataModelUncast = dataModel;
            @SuppressWarnings("unchecked")
            IModel<String> dataModelCast = dataModelUncast;
            item.add(new LinkPanel(componentId, dataModelCast));
        }

        private class LinkPanel extends Panel {
            public LinkPanel(String id, final IModel<String> model) {
                super(id);
                add(new ExternalLink("link", new AbstractReadOnlyModel<String>() {
                    @Override
                    public String getObject() {
                        return "mailto:" + model.getObject();
                    }
                }, model));
            }
        }
    }

    public static class URLColumn<D> extends PropertyColumn<D, String> {
        public URLColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
            super(displayModel, sortProperty, propertyExpression);
        }

        public URLColumn(IModel<String> displayModel, String propertyExpression) {
            super(displayModel, propertyExpression, propertyExpression);
        }

        @Override
        public void populateItem(Item<ICellPopulator<D>> item, String componentId, IModel<D> rowModel) {
            item.add(new LinkPanel(componentId, getDataModel(rowModel)));
        }

        private class LinkPanel extends Panel {
            public LinkPanel(String id, final IModel<Object> model) {
                super(id);
                AbstractReadOnlyModel<String> linkModel = new AbstractReadOnlyModel<String>() {
                    @Override
                    public String getObject() {
                        Object object = model.getObject();
                        if (object == null) {
                            return null;
                        }
                        if (object instanceof String) {
                            return String.class.cast(object);
                        }
                        if (object instanceof URL) {
                            return URL.class.cast(object).toExternalForm();
                        }
                        if (object instanceof URI) {
                            return URI.class.cast(object).toASCIIString();
                        }
                        throw new UnsupportedOperationException("type for url not supported: " + object.getClass().getName());
                    }
                };
                add(new ExternalLink("link", linkModel, true));
            }
        }
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

    public static abstract class ActionsPanel<T extends Serializable> extends Panel {
        public ActionsPanel(String id, final T object) {
            super(id);
            setOutputMarkupId(true);
            @SuppressWarnings("unchecked")
            final Form<T> form = (Form<T>) getParent();
            AjaxFallbackButton editLink = new AjaxFallbackButton(ACTIONS_EDIT_ID, form) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> f) {
                    onEdit(target, object);
                }
            };
            add(editLink);
            AjaxFallbackButton deleteLink = new AjaxFallbackButton(ACTIONS_DELETE_ID, form) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> f) {
                    onDelete(target, object);
                }
            };
            add(deleteLink);
        }

        /**
         * add table to AjaxRequestTarget and make service call to delete object
         */
        public abstract void onDelete(AjaxRequestTarget target, T object);

        /**
         * add table to AjaxRequestTarget
         */
        public abstract void onEdit(AjaxRequestTarget target, T object);
    }

    public static interface DataProvider<T extends Serializable> extends ISortableDataProvider<T, String> {
        /** service call to delete object & target.add( feedback ); target.add( table ); */
        public void delete(AjaxRequestTarget target, T object);

        public void edit(AjaxRequestTarget target, T object);

        public void add(AjaxRequestTarget target);

        public int getRowsPerPage();
    }

    public static abstract class DefaultDataProvider<T extends Serializable> implements DataProvider<T>, ISortState<String> {
        protected Map<String, SortOrder> sort = new LinkedHashMap<String, SortOrder>();

        protected int rowsPerPage = 10;

        @Override
        public IModel<T> model(T object) {
            return Model.of(object);
        }

        @Override
        public void detach() {
            //
        }

        @Override
        public Iterator<? extends T> iterator(long first, long count) {
            return select(first, count, sort);
        }

        protected abstract Iterator<T> select(long first, long count, Map<String, SortOrder> sorting);

        @Override
        public ISortState<String> getSortState() {
            return this;
        }

        @Override
        public int getRowsPerPage() {
            return rowsPerPage;
        }

        public void setRowsPerPage(int rowsPerPage) {
            this.rowsPerPage = rowsPerPage;
        }

        @Override
        public void setPropertySortOrder(String property, SortOrder sortOrder) {
            sort.put(property, sortOrder);
        }

        @Override
        public SortOrder getPropertySortOrder(String property) {
            SortOrder sortOrder = sort.get(property);
            if (sortOrder == null) {
                sortOrder = SortOrder.NONE;
            }
            return sortOrder;
        }
    }

    @Override
    public void addBottomToolbar(AbstractToolbar toolbar) {
        super.addBottomToolbar(new BottomToolbar(this, getDataprovider()));
    }

    @Override
    public void addTopToolbar(AbstractToolbar toolbar) {
        if (toolbar instanceof AjaxNavigationToolbar) {
            super.addTopToolbar(new TopToolbar(this, getDataprovider()));
        } else {
            super.addTopToolbar(toolbar); // headers toolbar
        }
    }

    @Override
    protected DataGridView<T> newDataGridView(String id, List<? extends IColumn<T, String>> columns, IDataProvider<T> dataProvider) {
        return new DataGrid(id, columns, dataProvider);
    }

    @Override
    protected void onAfterRenderChildren() {
        debug("", this);
    }

    private void debug(String prefix, MarkupContainer component) {
        Iterator<Component> iterator = component.iterator();
        System.out.println(prefix + component);
        while (iterator.hasNext()) {
            Component child = iterator.next();
            if (child instanceof MarkupContainer) {
                debug(prefix + "\t", (MarkupContainer) child);
            } else {
                System.out.println(prefix + "\t" + child);
            }
        }
    }

    public class DataGrid extends org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView<T> {
        protected static final String CELL_REPEATER_ID = "cells";

        protected static final String CELL_ITEM_ID = "cell";

        public DataGrid(String id, List<? extends IColumn<T, String>> columns, IDataProvider<T> dataProvider) {
            super(id, columns, dataProvider);
        }

        protected void populateItemNotFinal(final WebMarkupContainer container, final Item<T> item) {
            RepeatingView cells = new RepeatingView(CELL_REPEATER_ID);
            container.add(cells);

            int populatorsNumber = internalGetPopulators().size();
            for (int i = 0; i < populatorsNumber; i++) {
                ICellPopulator<T> populator = internalGetPopulators().get(i);
                IModel<ICellPopulator<T>> populatorModel = new Model<ICellPopulator<T>>(populator);
                @SuppressWarnings("unchecked")
                Item<ICellPopulator<T>> cellItem = newCellItem(cells.newChildId(), i, populatorModel);
                cells.add(cellItem);

                populator.populateItem(cellItem, CELL_ITEM_ID, item.getModel());

                if (cellItem.get("cell") == null) {
                    throw new WicketRuntimeException(
                            populator.getClass().getName()
                                    + ".populateItem() failed to add a component with id ["
                                    + CELL_ITEM_ID
                                    + "] to the provided [cellItem] object. Make sure you call add() on cellItem and make sure you gave the added component passed in 'componentId' id. ( *cellItem*.add(new MyComponent(*componentId*, rowModel) )");
                }
            }
        }

        @Override
        protected IItemFactory<T> newItemFactory() {
            return new IItemFactory<T>() {
                @Override
                public Item<T> newItem(int index, IModel<T> model) {
                    String id = DataGrid.this.newChildId();
                    Item<T> item = DataGrid.this.newItem(id, index, model);
                    Form<T> form = new Form<T>(ACTIONS_FORM_ID, model);
                    form.add(new Label("test", "test"));
                    item.add(form);
                    DataGrid.this.populateItemNotFinal(form, item); // why oh why is the original final?
                    return item;
                }
            };
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        protected Item newCellItem(final String id, final int index, final IModel model) {
            Item item = Table.this.newCellItem(id, index, model);
            final IColumn<T, String> column = getColumns().get(index);
            if (column instanceof IStyledColumn) {
                item.add(new CssAttributeBehavior() {
                    @Override
                    protected String getCssClass() {
                        return ((IStyledColumn<T, String>) column).getCssClass();
                    }
                });
            }
            return item;
        }

        @Override
        protected Item<T> newRowItem(final String id, final int index, final IModel<T> model) {
            return Table.this.newRowItem(id, index, model);
        }

        public abstract class CssAttributeBehavior extends Behavior {
            protected abstract String getCssClass();

            @Override
            public void onComponentTag(final Component component, final ComponentTag tag) {
                String className = getCssClass();
                if (!Strings.isEmpty(className)) {
                    tag.append("class", className, " ");
                }
            }
        }
    }

    public class TopToolbar extends AjaxNavigationToolbar {
        public TopToolbar(final DataTable<T, String> table, @SuppressWarnings("unused") final DataProvider<T> dataProvider) {
            super(table);
        }

        @Override
        protected PagingNavigator newPagingNavigator(final String navigatorId, final DataTable<?, ?> table) {
            return new PagingNavigator(navigatorId, table, null);
        }
    }

    public class PagingNavigator extends AjaxPagingNavigator {
        public PagingNavigator(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
            super(id, pageable, labelProvider);
        }

        @Override
        protected AbstractLink newPagingNavigationIncrementLink(String id, IPageable pageable, int increment) {
            AbstractLink link = new AjaxPagingNavigationIncrementLink(id, pageable, increment);
            modifyButtonLinkDisableBehavior(link);
            return link;
        }

        @Override
        protected AbstractLink newPagingNavigationLink(String id, IPageable pageable, int pageNumber) {
            final AbstractLink link = new AjaxPagingNavigationLink(id, pageable, pageNumber);
            modifyButtonLinkDisableBehavior(link);
            return link;
        }

        @Override
        protected PagingNavigation newNavigation(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
            return new AjaxPagingNavigation(id, pageable, labelProvider) {
                @SuppressWarnings("hiding")
                @Override
                protected Link<?> newPagingNavigationLink(String id, IPageable pageable, long pageIndex) {
                    final Link<?> link = new AjaxPagingNavigationLink(id, pageable, pageIndex);
                    modifyButtonLinkDisableBehavior(link);
                    return link;
                }
            };
        }

        protected void modifyButtonLinkDisableBehavior(final AbstractLink link) {
            link.add(new CssClassNameAppender(CSS_DISABLED_STYLE) {
                @Override
                public boolean isEnabled(Component component) {
                    return super.isEnabled(component) && !link.isEnabled();
                }
            });
            link.add(new CssClassNameRemover(CSS_DISABLED_STYLE) {
                @Override
                public boolean isEnabled(Component component) {
                    return super.isEnabled(component) && link.isEnabled();
                }
            });
        }
    }

    public class BottomToolbar extends AbstractToolbar {
        public BottomToolbar(final DataTable<T, String> table, final DataProvider<T> dataProvider) {
            super(table);

            WebMarkupContainer td = new WebMarkupContainer("td");
            add(td);

            td.add(AttributeModifier.replace("colspan", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return String.valueOf(table.getColumns().size());
                }
            }));
            Label norecordsfoundLabel = new Label("norecordsfound", new ResourceModel("norecordsfound"));
            norecordsfoundLabel.setVisible(getTable().getRowCount() == 0);
            td.add(norecordsfoundLabel);

            AjaxFallbackLink<String> addLink = new AjaxFallbackLink<String>(ACTIONS_ADD_ID) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    dataProvider.add(target);
                }
            };
            td.add(addLink);
        }
    }
}

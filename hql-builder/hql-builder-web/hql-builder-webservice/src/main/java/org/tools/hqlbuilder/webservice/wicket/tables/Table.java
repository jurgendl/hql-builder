package org.tools.hqlbuilder.webservice.wicket.tables;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigation;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationIncrementLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.tools.hqlbuilder.webservice.wicket.ext.ExternalLink;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

/**
 * @see https://www.packtpub.com/article/apache-wicket-displaying-data-using-datatable
 * @see http://wicketinaction.com/2008/10/building-a-listeditor-form-component/
 */
@SuppressWarnings("serial")
class Table<T extends Serializable> extends AjaxFallbackDefaultDataTable<T, String> {
    public static final String ACTIONS_DELETE_ID = "delete";

    public static final String ACTIONS_EDIT_ID = "edit";

    public static final String ACTIONS_ADD_ID = "add";

    public String CSS_DISABLED_STYLE = "disabled";

    public Table(Form<?> form, String id, List<IColumn<T, String>> columns, final DataProvider<T> dataProvider) {
        super(id, columns, new DelegateDataProvider<T>(form, dataProvider), dataProvider.getRowsPerPage());
        setOutputMarkupId(true);
    }

    public DataProvider<T> getDataprovider() {
        return (DataProvider<T>) getDataProvider();
    }

    protected static class EmailColumn<D> extends PropertyColumn<D, String> {
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

    protected static class URLColumn<D> extends PropertyColumn<D, String> {
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

    protected static abstract class ActionsPanel<T extends Serializable> extends Panel {
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
        return new DataGridView<T>(id, columns, dataProvider);
    }

    protected class TopToolbar extends AjaxNavigationToolbar {
        public TopToolbar(final DataTable<T, String> table, @SuppressWarnings("unused") final DataProvider<T> dataProvider) {
            super(table);
        }

        @Override
        protected PagingNavigator newPagingNavigator(final String navigatorId, final DataTable<?, ?> table) {
            return new PagingNavigator(navigatorId, table, null);
        }
    }

    protected class PagingNavigator extends AjaxPagingNavigator {
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

    protected class BottomToolbar extends AbstractToolbar {
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

            // ugly hack, parent='tableform' is set during form.add(table) which happens later and we need the form here
            // possible fix 1: put this in a form (form within form)
            // possible fix 2: adjust AjaxFallbackButton to fetch form at the moment of execution later so it is not needed during contruction
            Form<?> form = ((DelegateDataProvider<T>) getDataprovider()).getForm();
            AjaxFallbackButton addLink = new AjaxFallbackButton(ACTIONS_ADD_ID, form) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> f) {
                    dataProvider.add(target);
                }
            };
            add(addLink);
        }
    }

    /** ugly hack to get form parent later during contructor execution */
    private static class DelegateDataProvider<T extends Serializable> implements DataProvider<T> {
        private final Form<?> form;

        private final DataProvider<T> delegate;

        public DelegateDataProvider(Form<?> form, DataProvider<T> delegate) {
            this.delegate = delegate;
            this.form = form;
        }

        @Override
        public void delete(AjaxRequestTarget target, T object) {
            this.delegate.delete(target, object);
        }

        @Override
        public void edit(AjaxRequestTarget target, T object) {
            this.delegate.edit(target, object);
        }

        @Override
        public void add(AjaxRequestTarget target) {
            this.delegate.add(target);
        }

        @Override
        public int getRowsPerPage() {
            return this.delegate.getRowsPerPage();
        }

        @Override
        public ISortState<String> getSortState() {
            return this.delegate.getSortState();
        }

        @Override
        public void detach() {
            this.delegate.detach();
        }

        @Override
        public Iterator<? extends T> iterator(long first, long count) {
            return this.delegate.iterator(first, count);
        }

        @Override
        public long size() {
            return this.delegate.size();
        }

        @Override
        public IModel<T> model(T object) {
            return this.delegate.model(object);
        }

        public Form<?> getForm() {
            return this.form;
        }
    }
}

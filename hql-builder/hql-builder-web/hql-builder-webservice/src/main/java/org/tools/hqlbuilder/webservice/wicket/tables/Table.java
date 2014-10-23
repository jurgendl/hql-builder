package org.tools.hqlbuilder.webservice.wicket.tables;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigation;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationIncrementLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.sort.AjaxFallbackOrderByLink;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.string.Strings;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.WeLoveIcons;
import org.tools.hqlbuilder.webservice.wicket.components.LinkPanel;
import org.tools.hqlbuilder.webservice.wicket.components.LinkPanel.LinkType;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

/**
 * @see https 
 *      ://www.packtpub.com/article/apache-wicket-displaying-data-using-datatable
 * @see http://wicketinaction.com/2008/10/building-a-listeditor-form-component/
 */
public class Table<T extends Serializable> extends
		AjaxFallbackDefaultDataTable<T, String> {
	private static final long serialVersionUID = -997730195881970840L;

	public static final String ACTIONS_DELETE_ID = "delete";

	public static final String ACTIONS_EDIT_ID = "edit";

	public static final String ACTIONS_ADD_ID = "add";

	protected String CSS_DISABLED_STYLE = "ui-state-active";

	protected String CSS_ACTIVE_STYLE = "ui-state-active";

	public static final String CSS_EVEN = "ui-widget-content pui-datatable-even ui-datatable-even";

	public static final String CSS_ODD = "ui-widget-content pui-datatable-odd ui-datatable-odd";

	protected String cssEven = CSS_EVEN;

	protected String cssOdd = CSS_ODD;

	protected AjaxFallbackButton addLink;

	protected BottomToolbar bottomToolbar;

	public Table(Form<?> form, String id, List<TableColumn<T>> columns,
			final DataProvider<T> dataProvider, TableSettings settings) {
		super(id, columns, new DelegateDataProvider<T>(form, dataProvider),
				dataProvider.getRowsPerPage());
		addLink.setVisible(settings.isAdd());
		setOutputMarkupId(true);
	}

	public DataProvider<T> getDataprovider() {
		return (DataProvider<T>) getDataProvider();
	}

	protected static class EmailColumn<D> extends TableColumn<D> {
		private static final long serialVersionUID = 4634739390630581195L;

		public EmailColumn(IModel<String> displayModel,
				String propertyExpression) {
			setDisplayModel(displayModel);
			setPropertyExpression(propertyExpression);
		}

		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void populateItem(Item<ICellPopulator<D>> item,
				String componentId, IModel<D> rowModel) {
			IModel<Object> dataModel = getDataModel(rowModel);
			IModel dataModelUncast = dataModel;
			IModel<String> dataModelCast = dataModelUncast;
			item.add(new LinkPanel(componentId, dataModelUncast, dataModelCast,
					LinkType.email));
		}
	}

	public static class URLColumn<D> extends TableColumn<D> {
		private static final long serialVersionUID = -2998876473654238089L;

		public URLColumn(IModel<String> displayModel, String propertyExpression) {
			setDisplayModel(displayModel);
			setPropertyExpression(propertyExpression);
		}

		@Override
		public void populateItem(Item<ICellPopulator<D>> item,
				String componentId, IModel<D> rowModel) {
			item.add(new LinkPanel(componentId, getDataModel(rowModel),
					getDisplayModel(), LinkType.url));
		}
	}

	protected static abstract class ActionsPanel<T extends Serializable>
			extends Panel {
		private static final long serialVersionUID = -5249593513368522879L;

		public ActionsPanel(String id, final T object, TableSettings settings) {
			super(id);
			setOutputMarkupId(true);
			@SuppressWarnings("unchecked")
			final Form<T> form = (Form<T>) getParent();
			if (settings.isEdit()) {
				AjaxFallbackButton editLink = new AjaxFallbackButton(
						ACTIONS_EDIT_ID, form) {
					private static final long serialVersionUID = 2401036651703118413L;

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> f) {
						onEdit(target, object);
					}
				};
				add(editLink);
			} else {
				add(new WebMarkupContainer(ACTIONS_EDIT_ID).setVisible(false));
			}
			if (settings.isDelete()) {
				AjaxFallbackButton deleteLink = new AjaxFallbackButton(
						ACTIONS_DELETE_ID, form) {
					private static final long serialVersionUID = 8838151595047275051L;

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> f) {
						onDelete(target, object);
					}
				};
				add(deleteLink);
			} else {
				add(new WebMarkupContainer(ACTIONS_DELETE_ID).setVisible(false));
			}
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
		bottomToolbar = new BottomToolbar(this, getDataprovider());
		super.addBottomToolbar(bottomToolbar);
	}

	@Override
	public void addTopToolbar(AbstractToolbar toolbar) {
		if (toolbar instanceof AjaxNavigationToolbar) {
			// moved navigation to bottom
			super.addBottomToolbar(new TopToolbar(this, getDataprovider()));
		} else if (toolbar instanceof AjaxFallbackHeadersToolbar) {
			super.addTopToolbar(new HeadersToolbar(this, getDataprovider()));
		}
	}

	@Override
	protected Item<T> newRowItem(final String id, final int index,
			final IModel<T> model) {
		return new Item<T>(id, index, model) {
			private static final long serialVersionUID = 4130240092094444434L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("class", (getIndex() % 2 == 0) ? cssEven : cssOdd);
			}
		};
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		if (!isEnabledInHierarchy()) {
			return;
		}
		response.render(CssHeaderItem
				.forReference(WeLoveIcons.WE_LOVE_ICONS_CSS));
	}

	@Override
	protected DataGridView<T> newDataGridView(String id,
			List<? extends IColumn<T, String>> columns,
			IDataProvider<T> dataProvider) {
		return new DefaultDataGridView(id, columns, dataProvider);
	}

	protected class DefaultDataGridView extends DataGridView<T> {
		private static final long serialVersionUID = -5612396859737033644L;

		public DefaultDataGridView(String id,
				List<? extends IColumn<T, String>> columns,
				IDataProvider<T> dataProvider) {
			super(id, columns, dataProvider);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		protected Item newCellItem(final String id, final int index,
				final IModel model) {
			Item item = Table.this.newCellItem(id, index, model);
			final TableColumn<T> column = (TableColumn<T>) Table.this
					.getColumns().get(index);
			if (column instanceof IStyledColumn) {
				item.add(new CssAttributeBehavior() {
					private static final long serialVersionUID = -8376202471270737937L;

					@Override
					protected String getCssClass() {
						return ((IStyledColumn<T, String>) column)
								.getCssClass();
					}
				});
			}
			return item;
		}

		@Override
		protected Item<T> newRowItem(final String id, final int index,
				final IModel<T> model) {
			return Table.this.newRowItem(id, index, model);
		}
	}

	protected static abstract class CssAttributeBehavior extends Behavior {
		private static final long serialVersionUID = 1159801773515376493L;

		protected abstract String getCssClass();

		/**
		 * @see Behavior#onComponentTag(Component, ComponentTag)
		 */
		@Override
		public void onComponentTag(final Component component,
				final ComponentTag tag) {
			String className = getCssClass();
			if (!Strings.isEmpty(className)) {
				tag.append("class", className, " ");
			}
		}
	}

	public class HeadersToolbar extends AjaxFallbackHeadersToolbar<String> {
		private static final long serialVersionUID = -8737070685949753385L;

		public HeadersToolbar(DataTable<?, String> table,
				ISortStateLocator<String> stateLocator) {
			super(table, stateLocator);
		}

		@Override
		protected WebMarkupContainer newSortableHeader(String borderId,
				String property, ISortStateLocator<String> locator) {
			return new AjaxFallbackOrderBy(borderId, property, locator,
					getAjaxCallListener()) {
				private static final long serialVersionUID = -7436736813608388408L;

				@Override
				protected void onSortChanged() {
					getTable().setCurrentPage(0);
				}

				@Override
				protected void onAjaxClick(final AjaxRequestTarget target) {
					target.add(getTable());
				}
			};
		}
	}

	public class TopToolbar extends AjaxNavigationToolbar {
		private static final long serialVersionUID = 7871654433608259728L;

		public TopToolbar(final DataTable<T, String> table,
				final DataProvider<T> dataProvider) {
			super(table);
		}

		@Override
		protected PagingNavigator newPagingNavigator(final String navigatorId,
				final DataTable<?, ?> table) {
			return new PagingNavigator(navigatorId, table, null);
		}
	}

	public class PagingNavigator extends AjaxPagingNavigator {
		private static final long serialVersionUID = 1844950934466502565L;

		public PagingNavigator(String id, IPageable pageable,
				IPagingLabelProvider labelProvider) {
			super(id, pageable, labelProvider);
		}

		@Override
		protected AbstractLink newPagingNavigationIncrementLink(String id,
				IPageable pageable, int increment) {
			AjaxPagingNavigationIncrementLink link = new AjaxPagingNavigationIncrementLink(
					id, pageable, increment);
			modifyButtonLinkDisableBehavior(link);
			return link;
		}

		@Override
		protected AbstractLink newPagingNavigationLink(String id,
				IPageable pageable, int pageNumber) {
			AjaxPagingNavigationLink link = new AjaxPagingNavigationLink(id,
					pageable, pageNumber);
			modifyButtonLinkDisableBehavior(link);
			return link;
		}

		@Override
		protected PagingNavigation newNavigation(String id, IPageable pageable,
				IPagingLabelProvider labelProvider) {
			return new AjaxPagingNavigation(id, pageable, labelProvider) {
				private static final long serialVersionUID = -6446431226749147484L;

				@Override
				protected Link<?> newPagingNavigationLink(String id0,
						IPageable pageable0, long pageIndex) {
					AjaxPagingNavigationLink link = new AjaxPagingNavigationLink(
							id0, pageable0, pageIndex);
					if (pageable0.getCurrentPage() == pageIndex) {
						modifyButtonLinkActiveBehavior(link);
					} else {
						modifyButtonLinkDisableBehavior(link);
					}
					return link;
				}
			};
		}

		protected void modifyButtonLinkDisableBehavior(final AbstractLink link) {
			link.add(new CssClassNameAppender(CSS_DISABLED_STYLE) {
				private static final long serialVersionUID = -8703931679380496079L;

				@Override
				public boolean isEnabled(Component component) {
					return super.isEnabled(component) && !link.isEnabled();
				}
			});
			link.add(new CssClassNameRemover(CSS_DISABLED_STYLE) {
				private static final long serialVersionUID = -7077515156924411650L;

				@Override
				public boolean isEnabled(Component component) {
					return super.isEnabled(component) && link.isEnabled();
				}
			});
		}

		protected void modifyButtonLinkActiveBehavior(final AbstractLink link) {
			link.add(new CssClassNameAppender(CSS_ACTIVE_STYLE) {
				private static final long serialVersionUID = -4390935870504634276L;

				@Override
				public boolean isEnabled(Component component) {
					return super.isEnabled(component) && !link.isEnabled();
				}
			});
			link.add(new CssClassNameRemover(CSS_ACTIVE_STYLE) {
				private static final long serialVersionUID = -6666736063912700264L;

				@Override
				public boolean isEnabled(Component component) {
					return super.isEnabled(component) && link.isEnabled();
				}
			});
		}
	}

	public class BottomToolbar extends AbstractToolbar {
		private static final long serialVersionUID = -8277730819874510969L;

		public BottomToolbar(final DataTable<T, String> table,
				final DataProvider<T> dataProvider) {
			super(table);

			WebMarkupContainer td = new WebMarkupContainer("td");
			add(td);

			td.add(AttributeModifier.replace("colspan",
					new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 5599883778610261348L;

						@Override
						public String getObject() {
							return String.valueOf(table.getColumns().size());
						}
					}));
			Label norecordsfoundLabel = new Label("norecordsfound",
					new ResourceModel("norecordsfound"));
			norecordsfoundLabel.setVisible(getTable().getRowCount() == 0);
			td.add(norecordsfoundLabel);

			// ugly hack, parent='tableform' is set during form.add(table) which
			// happens later and we need the form here
			// possible fix 1: put this in a form (form within form)
			// possible fix 2: adjust AjaxFallbackButton to fetch form at the
			// moment of execution later so it is not needed during contruction
			Form<?> form = ((DelegateDataProvider<T>) getDataprovider())
					.getForm();
			addLink = new AjaxFallbackButton(ACTIONS_ADD_ID, form) {
				private static final long serialVersionUID = -8033338314334624474L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> f) {
					dataProvider.add(target);
				}
			};
			td.add(addLink);

			RepeatingView extraActions = new RepeatingView("extraActions");
			td.add(extraActions);
		}
	}

	/* ugly hack */
	private static class DelegateDataProvider<T extends Serializable>
			implements DataProvider<T> {
		private static final long serialVersionUID = -3517388036834640180L;

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

	/** see AjaxFallbackOrderByBorder, to change sorting */
	protected abstract class AjaxFallbackOrderBy extends Border {
		private static final long serialVersionUID = 212283337538504257L;

		public AjaxFallbackOrderBy(final String id, final String sortProperty,
				final ISortStateLocator<String> stateLocator,
				final IAjaxCallListener ajaxCallListener) {
			this(id, sortProperty, stateLocator,
					new AjaxFallbackOrderByLink.DefaultCssProvider<String>(),
					ajaxCallListener);
		}

		public AjaxFallbackOrderBy(final String id, final String sortProperty,
				final ISortStateLocator<String> stateLocator,
				final AjaxFallbackOrderByLink.ICssProvider<String> cssProvider,
				final IAjaxCallListener ajaxCallListener) {
			super(id);
			AjaxFallbackOrderByLink<String> link = new AjaxFallbackOrderByLink<String>(
					"orderByLink", sortProperty, stateLocator, cssProvider,
					ajaxCallListener) {
				private static final long serialVersionUID = -6094915237038098719L;

				@Override
				protected void onSortChanged() {
					AjaxFallbackOrderBy.this.onSortChanged();
				}

				@Override
				public void onClick(final AjaxRequestTarget target) {
					AjaxFallbackOrderBy.this.onAjaxClick(target);
				}

				@Override
				protected SortOrder nextSortOrder(final SortOrder order) {
					switch (order) {
					default: // <null>
					case NONE:
						return SortOrder.ASCENDING;
					case ASCENDING:
						return SortOrder.DESCENDING;
					case DESCENDING:
						return SortOrder.NONE;
					}
				}
			};
			addToBorder(link);
			add(new AjaxFallbackOrderByLink.CssModifier<String>(link,
					cssProvider));
			link.add(getBodyContainer());
		}

		/**
		 * This method is a hook for subclasses to perform an action after sort
		 * has changed
		 */
		protected abstract void onSortChanged();

		protected abstract void onAjaxClick(final AjaxRequestTarget target);
	}

	public BottomToolbar getBottomToolbar() {
		return this.bottomToolbar;
	}

	public String getCssEven() {
		return this.cssEven;
	}

	public void setCssEven(String cssEven) {
		this.cssEven = cssEven;
	}

	public String getCssOdd() {
		return this.cssOdd;
	}

	public void setCssOdd(String cssOdd) {
		this.cssOdd = cssOdd;
	}
}

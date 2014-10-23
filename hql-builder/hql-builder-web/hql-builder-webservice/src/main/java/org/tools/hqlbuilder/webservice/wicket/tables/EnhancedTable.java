package org.tools.hqlbuilder.webservice.wicket.tables;

import static org.tools.hqlbuilder.common.CommonUtils.name;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
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

	protected LinkedHashMap<IColumn<T, String>, TableColumnSettings> columns;

	public EnhancedTable(String id,
			LinkedHashMap<IColumn<T, String>, TableColumnSettings> columns,
			final DataProvider<T> dataProvider, TableSettings settings) {
		super(id);
		this.columns = columns;
		setOutputMarkupId(true);
		Form<?> form;
		if (settings.isStateless()) {
			form = new StatelessForm<T>(FORM_ID);
		} else {
			form = new Form<T>(FORM_ID);
		}
		add(form);
		table = new Table<T>(form, TABLE_ID, new ArrayList<IColumn<T, String>>(
				columns.keySet()), dataProvider, settings);
		form.add(table);
	}

	public Table<T> getTable() {
		return this.table;
	}

	public static String getServerColumnSorting(Object argument,
			TableColumnSettings settings) {
		return settings.getSorting() == Side.server ? name(argument) : null;
	}

	public static <D> IColumn<D, String> newColumn(Component parent,
			Object argument, TableColumnSettings settings) {
		return new PropertyColumn<D, String>(labelModel(parent, argument),
				getServerColumnSorting(argument, settings), name(argument));
	}

	public static <D> IColumn<D, String> newEmailColumn(Component parent,
			Object argument, TableColumnSettings settings) {
		return new EmailColumn<D>(labelModel(parent, argument),
				getServerColumnSorting(argument, settings), name(argument));
	}

	/** {@link URL}, {@link URI} and url as {@link String} supported */
	public static <D> IColumn<D, String> newURLColumn(Component parent,
			Object argument, TableColumnSettings settings) {
		return new URLColumn<D>(labelModel(parent, argument),
				getServerColumnSorting(argument, settings), name(argument));
	}

	public static <D> IColumn<D, String> newTimeColumn(Component parent,
			Object argument, TableColumnSettings settings) {
		return newDateOrTimeColumn(parent, argument, new DateConverter(true) {
			@Override
			protected DateTimeFormatter getFormat(Locale locale) {
				return DateTimeFormat.longTime().withLocale(locale);
			}

			@Override
			public String getDatePattern(Locale locale) {
				return getFormat(locale).toString();
			}
		}, settings);
	}

	public static <D> IColumn<D, String> newDateColumn(Component parent,
			Object argument, TableColumnSettings settings) {
		return newDateOrTimeColumn(parent, argument, new DateConverter(true) {
			@Override
			protected DateTimeFormatter getFormat(Locale locale) {
				return DateTimeFormat.longDate().withLocale(locale);
			}

			@Override
			public String getDatePattern(Locale locale) {
				return getFormat(locale).toString();
			}
		}, settings);
	}

	public static <D> IColumn<D, String> newDateTimeColumn(Component parent,
			Object argument, TableColumnSettings settings) {
		return newDateOrTimeColumn(parent, argument, new DateConverter(true) {
			@Override
			protected DateTimeFormatter getFormat(Locale locale) {
				return DateTimeFormat.longDateTime().withLocale(locale);
			}

			@Override
			public String getDatePattern(Locale locale) {
				return getFormat(locale).toString();
			}
		}, settings);
	}

	public static <D> IColumn<D, String> newDateOrTimeColumn(Component parent,
			Object argument, final DateConverter dateConverter,
			TableColumnSettings settings) {
		return new PropertyColumn<D, String>(labelModel(parent, argument),
				getServerColumnSorting(argument, settings), name(argument)) {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void populateItem(Item<ICellPopulator<D>> item,
					String componentId, IModel<D> rowModel) {
				item.add(new DateLabel(componentId,
						(IModel) getDataModel(rowModel), dateConverter));
			}
		};
	}

	public static IModel<String> labelModel(Component parent, Object argument) {
		String property = name(argument);
		return labelModel(parent, property);
	}

	public static IModel<String> labelModel(final Component parent,
			final String property) {
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
			logger.error(parent.getClass().getName() + ": no translation for "
					+ property);
			label = Model.of("[" + property + "_" + parent.getLocale() + "]");
		}
		return label;
	}

	public static <T extends Serializable> IColumn<T, String> getActionsColumn(
			Component parent, final DataProvider<T> provider,
			final TableSettings settings) {
		return new ActionsColumn<T>(labelModel(parent, ACTIONS_ID), provider,
				settings);
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
			String config = "textExtraction:function(node){return $(node).text();},sortMultiSortKey:'ctrlKey',cssHeader:'wicket_orderNone',cssAsc:'wicket_orderUp',cssDesc:'wicket_orderDown',headers:{";
			int i = 0;
			for (Map.Entry<IColumn<T, String>, TableColumnSettings> columnSettings : columns
					.entrySet()) {
				if (i > 0) {
					config += ",";
				}
				boolean clientSortable = !(columnSettings.getKey() instanceof ActionsColumn)
						&& columnSettings.getValue() != null
						&& columnSettings.getValue().getSorting() == Side.client;
				anyClientSortable |= clientSortable;
				config += i + ":{sorter:" + clientSortable + "}";
				i++;
			}
			config += "}";
			if (anyClientSortable) {
				response.render(JavaScriptHeaderItem
						.forReference(TableSorter.TABLE_SORTER_JS));
				response.render(OnDomReadyHeaderItem.forScript("$('#"
						+ getTable().getMarkupId() + "').tablesorter({"
						+ config + "});;"));
			}
		}
	}

	public static class ActionsColumn<T extends Serializable> extends
			AbstractColumn<T, String> {
		protected final DataProvider<T> provider;
		protected final TableSettings settings;

		public ActionsColumn(IModel<String> displayModel,
				final DataProvider<T> provider, final TableSettings settings) {
			super(displayModel);
			this.provider = provider;
			this.settings = settings;
		}

		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void populateItem(Item cellItem, String componentId,
				IModel rowModel) {
			T object = ((T) rowModel.getObject());
			cellItem.add(new ActionsPanel<T>(componentId, object, settings) {
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

package org.tools.hqlbuilder.webservice.wicket.pages;

import static org.tools.hqlbuilder.common.CommonUtils.proxy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDateTime;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.demo.Registration;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;
import org.tools.hqlbuilder.webservice.jquery.ui.tablesorter.TableSorter;
import org.tools.hqlbuilder.webservice.wicket.forms.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.tables.DefaultDataProvider;
import org.tools.hqlbuilder.webservice.wicket.tables.EnhancedTable;
import org.tools.hqlbuilder.webservice.wicket.tables.Side;
import org.tools.hqlbuilder.webservice.wicket.tables.TableColumnSettings;
import org.tools.hqlbuilder.webservice.wicket.tables.TableSettings;
import org.wicketstuff.annotation.mount.MountPath;

@SuppressWarnings("serial")
@MountPath("/form/registrations")
public class RegistrationsPage extends BasePage {
	@SpringBean
	private HqlWebServiceClient hqlWebClient;

	private FormPanel<Registration> formPanel;

	private EnhancedTable<Registration> table;

	public RegistrationsPage(PageParameters parameters) {
		super(parameters);
		Registration proxy = proxy(Registration.class);
		DefaultDataProvider<Registration> dataProvider = initDataProvider();
		DefaultFormActions<Registration> formActions = initTable(proxy,
				dataProvider);
		add(table);
		initForm(proxy, formActions);
		add(formPanel);
	}

	protected DefaultDataProvider<Registration> initDataProvider() {
		final int rows = 9999;
		DefaultDataProvider<Registration> dataProvider = new DefaultDataProvider<Registration>() {
			@Override
			public int getRowsPerPage() {
				return rows;
			}

			@Override
			@SuppressWarnings("unchecked")
			public Iterator<Registration> select(long first, long count,
					Map<String, SortOrder> sorting) {
				String hql = "select obj from "
						+ Registration.class.getSimpleName() + " obj";
				String orderByHql = " order by ";
				for (Map.Entry<String, SortOrder> sortEntry : sorting
						.entrySet()) {
					if (sortEntry.getValue() != null
							&& sortEntry.getValue() != SortOrder.NONE) {
						orderByHql += "obj."
								+ sortEntry.getKey()
								+ " "
								+ (sortEntry.getValue() == SortOrder.ASCENDING ? "asc"
										: "desc") + ", ";
					}
				}
				if (!" order by ".equals(orderByHql)) {
					hql += orderByHql.substring(0, orderByHql.length() - 2);
				}
				QueryParameters query = new QueryParameters().setHql(hql)
						.setFirst((int) first).setMax((int) count);
				Iterator<Serializable> iterator = hqlWebClient.execute(query)
						.getResults().getValue().iterator();
				return Iterator.class.cast(iterator);
			}

			@Override
			public long size() {
				String hql = "select count(obj.id) from "
						+ Registration.class.getSimpleName() + " obj";
				List<Serializable> value = hqlWebClient
						.execute(new QueryParameters().setHql(hql))
						.getResults().getValue();
				return Long.parseLong(String.valueOf(value.get(0)));
			}

			@Override
			public void delete(AjaxRequestTarget target, Registration object) {
				hqlWebClient.delete(object);
				if (target != null) {
					target.add(table);
				}
			}

			@Override
			public void edit(AjaxRequestTarget target, Registration object) {
				formPanel.setVisible(true);
				formPanel.getFormModel().setObject(object);
				if (target != null) {
					target.add(formPanel);
				}
			}

			@Override
			public void add(AjaxRequestTarget target) {
				formPanel.setVisible(true);
				formPanel.getFormModel().setObject(new Registration());
				if (target != null) {
					target.add(formPanel);
					@SuppressWarnings({ "unused", "unchecked" })
					Form<Registration> form = (Form<Registration>) formPanel
							.get(FormPanel.FORM);
				}
			}
		};
		dataProvider.setRowsPerPage(rows);
		return dataProvider;
	}

	protected DefaultFormActions<Registration> initTable(Registration proxy,
			DefaultDataProvider<Registration> dataProvider) {
		List<IColumn<Registration, String>> columns = new ArrayList<IColumn<Registration, String>>();
		TableColumnSettings tcSet = new TableColumnSettings().setFiltering(
				Side.none).setSorting(Side.client);
		columns.add(EnhancedTable.<Registration> newColumn(this,
				proxy.getFirstName(), tcSet));
		columns.add(EnhancedTable.<Registration> newColumn(this,
				proxy.getLastName(), tcSet));
		columns.add(EnhancedTable.<Registration> newColumn(this,
				proxy.getUsername(), tcSet));
		columns.add(EnhancedTable.<Registration> newEmailColumn(this,
				proxy.getEmail(), tcSet));
		columns.add(EnhancedTable.<Registration> newDateTimeColumn(this,
				proxy.getDateOfBirth(), tcSet));

		TableSettings settings = new TableSettings();
		columns.add(EnhancedTable.<Registration> getActionsColumn(this,
				dataProvider, settings));
		table = new EnhancedTable<Registration>("registrationstable", columns,
				dataProvider, settings);

		DefaultFormActions<Registration> formActions = new DefaultFormActions<Registration>() {
			@Override
			public void submitModel(IModel<Registration> model) {
				Registration object = model.getObject();
				object.setVerification(new LocalDateTime());
				Serializable id = hqlWebClient.save(object);
				object = hqlWebClient.get(object.getClass(), id);
				model.setObject(object);
			}

			@Override
			public void afterSubmit(AjaxRequestTarget target,
					Form<Registration> form, IModel<Registration> model) {
				formPanel.getFormModel().setObject(new Registration());
				formPanel.setVisible(false);
				table.setVisible(true);
				if (target != null) {
					target.add(formPanel);
					target.add(table);
				}
			}
		};

		return formActions;
	}

	protected void initForm(Registration proxy,
			DefaultFormActions<Registration> formActions) {
		formPanel = new FormPanel<Registration>("registrationform",
				formActions, new FormSettings().setLiveValidation(true)
						.setAjax(true).setInheritId(true));
		formPanel.addTextField(proxy.getUsername(), new FormElementSettings(
				true));
		formPanel.addTextField(proxy.getFirstName(), new FormElementSettings(
				true));
		formPanel.addTextField(proxy.getLastName(), new FormElementSettings(
				true));
		formPanel.addEmailTextField(proxy.getEmail(), new FormElementSettings(
				true));
		formPanel.addDatePicker(proxy.getDateOfBirth(),
				new FormElementSettings(true));
		formPanel.addPasswordTextField(proxy.getPassword(),
				new FormElementSettings(true));
		formPanel.setVisible(false);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem
				.forReference(TableSorter.TABLE_SORTER_JS));
		String config = "textExtraction:function(node){return $(node).text();},sortMultiSortKey:'ctrlKey',cssHeader:'wicket_orderNone',cssAsc:'wicket_orderUp',cssDesc:'wicket_orderDown',headers:{";
		for (int i = 0; i < this.table.getTable().getColumns().size(); i++) {
			if (i > 0) {
				config += ",";
			}
			boolean sortable = false;
			if (i < this.table.getTable().getColumns().size() - 1) {
				sortable = true;
			}
			config += i + ":{sorter:" + sortable + "}";
		}
		config += "}";
		response.render(OnDomReadyHeaderItem.forScript("$('#"
				+ this.table.getTable().getMarkupId() + "').tablesorter({"
				+ config + "});;"));

	}
}

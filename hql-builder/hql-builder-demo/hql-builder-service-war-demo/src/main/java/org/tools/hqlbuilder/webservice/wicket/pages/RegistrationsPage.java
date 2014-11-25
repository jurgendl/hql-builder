package org.tools.hqlbuilder.webservice.wicket.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDateTime;
import org.tools.hqlbuilder.common.CommonUtils;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.demo.Registration;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;
import org.tools.hqlbuilder.webservice.wicket.forms.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.FormConstants;
import org.tools.hqlbuilder.webservice.wicket.forms.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.tables.DefaultDataProvider;
import org.tools.hqlbuilder.webservice.wicket.tables.EnhancedTable;
import org.tools.hqlbuilder.webservice.wicket.tables.Side;
import org.tools.hqlbuilder.webservice.wicket.tables.TableColumn;
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
        Registration proxy = CommonUtils.proxy(Registration.class);
        DefaultDataProvider<Registration> dataProvider = this.initDataProvider();
        DefaultFormActions<Registration> formActions = this.initTable(proxy, dataProvider);
        this.add(this.table);
        this.initForm(proxy, formActions);
        this.add(this.formPanel);
    }

    protected DefaultDataProvider<Registration> initDataProvider() {
        DefaultDataProvider<Registration> dataProvider = new DefaultDataProvider<Registration>() {
            @Override
            public void add(AjaxRequestTarget target) {
                RegistrationsPage.this.formPanel.setVisible(true);
                RegistrationsPage.this.formPanel.getFormModel().setObject(new Registration());
                if (target != null) {
                    target.add(RegistrationsPage.this.formPanel);
                    @SuppressWarnings({ "unused", "unchecked" })
                    Form<Registration> form = (Form<Registration>) RegistrationsPage.this.formPanel.get(FormConstants.FORM);
                }
            }

            @Override
            public void delete(AjaxRequestTarget target, Registration object) {
                RegistrationsPage.this.hqlWebClient.delete(object);
                if (target != null) {
                    target.add(RegistrationsPage.this.table);
                }
            }

            @Override
            public void edit(AjaxRequestTarget target, Registration object) {
                RegistrationsPage.this.formPanel.setVisible(true);
                RegistrationsPage.this.formPanel.getFormModel().setObject(object);
                if (target != null) {
                    target.add(RegistrationsPage.this.formPanel);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            public Iterator<Registration> select(long first, long count, Map<String, SortOrder> sorting) {
                String hql = "select obj from " + Registration.class.getSimpleName() + " obj";
                String orderByHql = " order by ";
                for (Map.Entry<String, SortOrder> sortEntry : sorting.entrySet()) {
                    if ((sortEntry.getValue() != null) && (sortEntry.getValue() != SortOrder.NONE)) {
                        orderByHql += "obj." + sortEntry.getKey() + " " + (sortEntry.getValue() == SortOrder.ASCENDING ? "asc" : "desc") + ", ";
                    }
                }
                if (!" order by ".equals(orderByHql)) {
                    hql += orderByHql.substring(0, orderByHql.length() - 2);
                }
                QueryParameters query = new QueryParameters().setHql(hql).setFirst((int) first).setMax((int) count);
                Iterator<Serializable> iterator = RegistrationsPage.this.hqlWebClient.execute(query).getResults().getValue().iterator();
                return Iterator.class.cast(iterator);
            }

            @Override
            public long size() {
                String hql = "select count(obj.id) from " + Registration.class.getSimpleName() + " obj";
                List<Serializable> value = RegistrationsPage.this.hqlWebClient.execute(new QueryParameters().setHql(hql)).getResults().getValue();
                return Long.parseLong(String.valueOf(value.get(0)));
            }
        };
        return dataProvider;
    }

    protected void initForm(Registration proxy, DefaultFormActions<Registration> formActions) {
        this.formPanel = new FormPanel<Registration>("registrationform", formActions, new FormSettings().setLiveValidation(true).setAjax(true)
                .setInheritId(true));
        this.formPanel.addTextField(proxy.getUsername(), new FormElementSettings(true));
        this.formPanel.addTextField(proxy.getFirstName(), new FormElementSettings(true));
        this.formPanel.addTextField(proxy.getLastName(), new FormElementSettings(true));
        this.formPanel.addEmailTextField(proxy.getEmail(), new FormElementSettings(true));
        this.formPanel.addDatePicker(proxy.getDateOfBirth(), new FormElementSettings(true));
        this.formPanel.addPasswordTextField(proxy.getPassword(), new FormElementSettings(true));
        this.formPanel.setVisible(false);
    }

    protected DefaultFormActions<Registration> initTable(Registration proxy, DefaultDataProvider<Registration> dataProvider) {
        List<TableColumn<Registration, ?>> columns = new ArrayList<TableColumn<Registration, ?>>();
        Side sort = Side.server;
        columns.add(EnhancedTable.<Registration, String> newColumn(this, proxy.getFirstName()).setSorting(sort));
        columns.add(EnhancedTable.<Registration, String> newColumn(this, proxy.getLastName()).setSorting(sort));
        columns.add(EnhancedTable.<Registration, String> newColumn(this, proxy.getUsername()).setSorting(sort));
        columns.add(EnhancedTable.<Registration> newEmailColumn(this, proxy.getEmail()).setSorting(sort));
        columns.add(EnhancedTable.<Registration> newDateTimeColumn(this, proxy.getDateOfBirth()).setSorting(sort));
        columns.add(EnhancedTable.<Registration> getActionsColumn(this, dataProvider));
        this.table = new EnhancedTable<Registration>("registrationstable", columns, dataProvider);

        DefaultFormActions<Registration> formActions = new DefaultFormActions<Registration>() {
            @Override
            public void afterSubmit(AjaxRequestTarget target, Form<Registration> form, IModel<Registration> model) {
                RegistrationsPage.this.formPanel.getFormModel().setObject(new Registration());
                RegistrationsPage.this.formPanel.setVisible(false);
                RegistrationsPage.this.table.setVisible(true);
                if (target != null) {
                    target.add(RegistrationsPage.this.formPanel);
                    target.add(RegistrationsPage.this.table);
                }
            }

            @Override
            public void submitModel(IModel<Registration> model) {
                Registration object = model.getObject();
                object.setVerification(new LocalDateTime());
                Serializable id = RegistrationsPage.this.hqlWebClient.save(object);
                object = RegistrationsPage.this.hqlWebClient.get(object.getClass(), id);
                model.setObject(object);
            }
        };

        return formActions;
    }
}

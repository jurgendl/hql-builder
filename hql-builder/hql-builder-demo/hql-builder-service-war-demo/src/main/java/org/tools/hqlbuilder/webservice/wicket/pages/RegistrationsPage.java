package org.tools.hqlbuilder.webservice.wicket.pages;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.create;
import static org.tools.hqlbuilder.webservice.wicket.WebHelper.name;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDateTime;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.test.Registration;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.forms.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.tables.DefaultDataProvider;
import org.tools.hqlbuilder.webservice.wicket.tables.EnhancedTable;

@SuppressWarnings("serial")
@MountedPage("/form/registrations")
public class RegistrationsPage extends BasePage {
    private static final String FORM_ID = "userdata.form";

    @SpringBean
    private HqlWebServiceClient hqlWebClient;

    private FormPanel<Registration> formPanel;

    private EnhancedTable<Registration> table;

    public RegistrationsPage(PageParameters parameters) {
        super(parameters);

        Registration proxy = create(Registration.class);

        DefaultDataProvider<Registration> dataProvider = new DefaultDataProvider<Registration>() {
            @Override
            @SuppressWarnings("unchecked")
            public Iterator<Registration> select(long first, long count, Map<String, SortOrder> sorting) {
                String hql = "select obj from " + Registration.class.getSimpleName() + " obj";
                String orderByHql = " order by ";
                for (Map.Entry<String, SortOrder> sortEntry : sorting.entrySet()) {
                    if (sortEntry.getValue() != null && sortEntry.getValue() != SortOrder.NONE) {
                        orderByHql += "obj." + sortEntry.getKey() + " " + (sortEntry.getValue() == SortOrder.ASCENDING ? "asc" : "desc") + ", ";
                    }
                }
                if (!" order by ".equals(orderByHql)) {
                    hql += orderByHql.substring(0, orderByHql.length() - 2);
                }
                QueryParameters query = new QueryParameters().setHql(hql).setFirst((int) first).setMax((int) count);
                Iterator<Serializable> iterator = hqlWebClient.execute(query).getResults().getValue().iterator();
                return Iterator.class.cast(iterator);
            }

            @Override
            public long size() {
                String hql = "select count(obj.id) from " + Registration.class.getSimpleName() + " obj";
                List<Serializable> value = hqlWebClient.execute(new QueryParameters().setHql(hql)).getResults().getValue();
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
                formPanel.setDefaultModelObject(object);
                if (target != null) {
                    target.add(formPanel);
                }
            }

            @Override
            public void add(AjaxRequestTarget target) {
                formPanel.setVisible(true);
                formPanel.setDefaultModelObject(new Registration());
                if (target != null) {
                    target.add(formPanel);
                    @SuppressWarnings({ "unused", "unchecked" })
                    Form<Registration> form = (Form<Registration>) formPanel.get(FormPanel.FORM);
                }
            }
        };
        dataProvider.setRowsPerPage(5);

        List<IColumn<Registration, String>> columns = new ArrayList<IColumn<Registration, String>>();
        columns.add(EnhancedTable.<Registration> newColumn(this, proxy.getFirstName()));
        columns.add(EnhancedTable.<Registration> newColumn(this, proxy.getLastName()));
        columns.add(EnhancedTable.<Registration> newColumn(this, proxy.getUsername()));
        columns.add(EnhancedTable.<Registration> newEmailColumn(this, proxy.getEmail()));
        columns.add(EnhancedTable.<Registration> newDateTimeColumn(this, proxy.getDateOfBirth()));
        columns.add(EnhancedTable.<Registration> getActionsColumn(this, dataProvider));

        table = new EnhancedTable<Registration>("registrations", columns, dataProvider);

        DefaultFormActions<Registration> formActions = new DefaultFormActions<Registration>() {
            @Override
            public void submit(IModel<Registration> model) {
                Registration object = model.getObject();
                object.setVerification(new LocalDateTime());
                Serializable id = hqlWebClient.save(object);
                object = hqlWebClient.get(object.getClass(), id);
                model.setObject(object);
            }

            @Override
            public void afterSubmit(AjaxRequestTarget target, Form<Registration> form, IModel<Registration> model) {
                formPanel.setDefaultModelObject(new Registration());
                formPanel.setVisible(false);
                table.setVisible(true);
                if (target != null) {
                    target.add(formPanel);
                    target.add(table);
                }
            }

            @Override
            public boolean isCancelable() {
                return true;
            }
        };
        formPanel = new FormPanel<Registration>(FORM_ID, Model.of(new Registration()), true, formActions);
        formPanel.setLiveValidation(true);
        formPanel.addTextField(name(proxy.getUsername()), new FormElementSettings(true));
        formPanel.addTextField(name(proxy.getFirstName()), new FormElementSettings(true));
        formPanel.addTextField(name(proxy.getLastName()), new FormElementSettings(true));
        formPanel.addEmailTextField(name(proxy.getEmail()), new FormElementSettings(true));
        formPanel.addDatePicker(name(proxy.getDateOfBirth()), new FormElementSettings(true));
        formPanel.addPasswordTextField(name(proxy.getPassword()), new FormElementSettings(true));

        formPanel.setVisible(false);

        add(table);
        add(formPanel);
    }
}

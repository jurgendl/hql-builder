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
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.tables.Table;
import org.tools.hqlbuilder.webservice.wicket.tables.Table.DataProvider;
import org.tools.hqlbuilder.webservice.wicket.tables.Table.DefaultDataProvider;

@MountedPage("/form/registrations")
public class RegistrationsPage extends DefaultWebPage {
    private static final long serialVersionUID = 1247275992404894937L;

    private static final String FORM_ID = "userdata.form";

    @SpringBean
    protected HqlWebServiceClient hqlWebClient;

    private FormPanel<Registration> formPanel;

    private Table<Registration> table;

    public RegistrationsPage(PageParameters parameters) {
        super(parameters);

        Registration proxy = create(Registration.class);

        DataProvider<Registration> dataProvider = new DefaultDataProvider<Registration>() {
            private static final long serialVersionUID = 6812428385117168023L;

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
                target.add(table);
            }

            @Override
            public void edit(AjaxRequestTarget target, Registration object) {
                formPanel.setVisible(true);
                formPanel.setDefaultModelObject(Model.of(object));
                target.add(formPanel);
            }

            @Override
            public void add(AjaxRequestTarget target) {
                formPanel.setVisible(true);
                formPanel.setDefaultModelObject(FormPanel.newFormModel(Registration.class));
                target.add(formPanel);
            }
        };

        List<IColumn<Registration, String>> columns = new ArrayList<IColumn<Registration, String>>();
        columns.add(Table.<Registration> newColumn(this, proxy.getFirstName()));
        columns.add(Table.<Registration> newColumn(this, proxy.getLastName()));
        columns.add(Table.<Registration> newColumn(this, proxy.getUsername()));
        columns.add(Table.<Registration> newEmailColumn(this, proxy.getEmail()));
        columns.add(Table.<Registration> newDateTimeColumn(this, proxy.getDateOfBirth()));
        columns.add(Table.<Registration> getActionsColumn(this, dataProvider));

        table = new Table<Registration>(columns, dataProvider);

        DefaultFormActions<Registration> formActions = new DefaultFormActions<Registration>() {
            private static final long serialVersionUID = 3530578296967349699L;

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
                formPanel.setDefaultModelObject(FormPanel.newFormModel(Registration.class));
                formPanel.setVisible(false);
                table.setVisible(true);
                target.add(formPanel);
                target.add(table);
            }

            @Override
            public boolean isCancelable() {
                return true;
            }
        };
        formPanel = new FormPanel<Registration>(FORM_ID, Registration.class, true, formActions);
        formPanel.addTextField(name(proxy.getUsername()), true);
        formPanel.addTextField(name(proxy.getFirstName()), true);
        formPanel.addTextField(name(proxy.getLastName()), true);
        formPanel.addEmailTextField(name(proxy.getEmail()), true);
        formPanel.addDatePicker(name(proxy.getDateOfBirth()), true);
        formPanel.addPasswordTextField(name(proxy.getPassword()), true);
        formPanel.liveValidation();

        formPanel.setVisible(false);

        add(table);
        add(formPanel);
    }
}

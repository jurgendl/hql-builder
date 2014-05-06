package org.tools.hqlbuilder.webservice.wicket.pages;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.create;
import static org.tools.hqlbuilder.webservice.wicket.WebHelper.name;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.test.Registration;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.tables.Table;
import org.tools.hqlbuilder.webservice.wicket.tables.Table.DataProvider;
import org.tools.hqlbuilder.webservice.wicket.tables.Table.DefaultDataProvider;

@MountedPage("/form/registrations")
public class RegistrationsPage extends DefaultWebPage {
    private static final long serialVersionUID = 1247275992404894937L;

    @SpringBean
    protected HqlWebServiceClient hqlWebClient;

    private FormPanel<Registration> formPanel;

    public RegistrationsPage(PageParameters parameters) {
        super(parameters);

        Registration proxy = create(Registration.class);
        final String FORM_ID = "userdata.form";

        @SuppressWarnings("unchecked")
        DataProvider<Registration> dataProvider = new DefaultDataProvider<Registration>() {
            private static final long serialVersionUID = 6812428385117168023L;

            @Override
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
                Iterator<Object> iterator = hqlWebClient.execute(query).getResults().getValue().iterator();
                return Iterator.class.cast(iterator);
            }

            @Override
            public long size() {
                String hql = "select count(obj.id) from " + Registration.class.getSimpleName() + " obj";
                List<Object> value = hqlWebClient.execute(new QueryParameters().setHql(hql)).getResults().getValue();
                return Long.parseLong(String.valueOf(value.get(0)));
            }

            @Override
            public void delete(AjaxRequestTarget target, Registration object) {
                hqlWebClient.delete(object);
                Table<Registration> table = (Table<Registration>) get(Table.ID);
                target.add(table);
            }

            @Override
            public void edit(AjaxRequestTarget target, Registration object) {
                formPanel.setVisible(true);
                formPanel.setDefaultModelObject(Model.of(object));
                target.add(formPanel);
            }
        };

        List<IColumn<Registration, String>> columns = new ArrayList<IColumn<Registration, String>>();
        columns.add(Table.<Registration> newColumn(this, proxy.getFirstName()));
        columns.add(Table.<Registration> newColumn(this, proxy.getLastName()));
        columns.add(Table.<Registration> newColumn(this, proxy.getUsername()));
        columns.add(Table.<Registration> newEmailColumn(this, proxy.getEmail()));
        columns.add(Table.<Registration> newDateTimeColumn(this, proxy.getVerification()));
        columns.add(Table.<Registration> getActionsColumn(this, dataProvider));

        Table<Registration> table = new Table<Registration>(columns, dataProvider);

        formPanel = new FormPanel<Registration>(FORM_ID, Registration.class, true) {
            private static final long serialVersionUID = -2653547660762438431L;

            @Override
            protected void submit(IModel<Registration> model) {
                Registration object = model.getObject();
                object.setVerification(new Date());
                Serializable id = hqlWebClient.save(object);
                object = hqlWebClient.get(object.getClass(), id);
                model.setObject(object);
            }
        };
        formPanel.addTextField(name(proxy.getUsername()), true);
        formPanel.addTextField(name(proxy.getFirstName()), true);
        formPanel.addTextField(name(proxy.getLastName()), true);
        formPanel.addEmailTextField(name(proxy.getEmail()), true);
        formPanel.addPasswordTextField(name(proxy.getPassword()), true);

        formPanel.setVisible(false);

        add(table);
        add(formPanel);
    }
}

package org.tools.hqlbuilder.webservice.wicket.pages;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.create;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.test.Registration;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.tables.Table;
import org.tools.hqlbuilder.webservice.wicket.tables.Table.DeleteCall;

@MountedPage("/form/registrations")
public class RegistrationsPage extends DefaultWebPage {
    private static final long serialVersionUID = 1247275992404894937L;

    @SpringBean
    protected HqlWebServiceClient hqlWebClient;

    public RegistrationsPage(PageParameters parameters) {
        super(parameters);

        Registration registration = create(Registration.class);

        List<IColumn<Registration, String>> columns = new ArrayList<IColumn<Registration, String>>();
        columns.add(Table.<Registration> newColumn(this, registration.getFirstName()));
        columns.add(Table.<Registration> newColumn(this, registration.getLastName()));
        columns.add(Table.<Registration> newColumn(this, registration.getUsername()));
        columns.add(Table.<Registration> newEmailColumn(this, registration.getEmail()));
        columns.add(Table.<Registration> newDateTimeColumn(this, registration.getVerification()));
        columns.add(Table.<Registration> getActionsColumn(this, new DeleteCall<Registration>() {
            private static final long serialVersionUID = 26719642712228627L;

            @Override
            public void delete(Registration object) {
                hqlWebClient.delete(object);
            }

            @Override
            public void updateUI(AjaxRequestTarget target) {
                @SuppressWarnings("unchecked")
                Table<Registration> table = (Table<Registration>) get(Table.ID);
                target.add(table);
            }
        }));

        IDataProvider<Registration> dataProvider = new IDataProvider<Registration>() {
            private static final long serialVersionUID = 6812428385117168023L;

            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            public Iterator<Registration> iterator(long first, long count) {
                return (Iterator) hqlWebClient
                        .execute(
                                new QueryParameters().setFirst((int) first).setMax((int) count)
                                        .setHql("select obj from " + Registration.class.getSimpleName() + " obj")).getResults().getValue().iterator();
            }

            @Override
            public long size() {
                return Long.parseLong(String.valueOf(hqlWebClient
                        .execute(new QueryParameters().setHql("select count(obj.id) from " + Registration.class.getSimpleName() + " obj"))
                        .getResults().getValue().get(0)));
            }

            @Override
            public IModel<Registration> model(Registration object) {
                return Model.of(object);
            }

            @Override
            public void detach() {
                //
            }
        };

        Table<Registration> table = new Table<Registration>(columns, dataProvider, 10);
        add(table);
    }
}

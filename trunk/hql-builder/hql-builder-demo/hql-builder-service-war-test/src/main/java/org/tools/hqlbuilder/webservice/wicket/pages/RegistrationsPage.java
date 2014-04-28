package org.tools.hqlbuilder.webservice.wicket.pages;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.create;
import static org.tools.hqlbuilder.webservice.wicket.WebHelper.name;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.test.Registration;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;

@MountedPage("/form/registrations")
public class RegistrationsPage extends DefaultWebPage {
    private static final long serialVersionUID = 1247275992404894937L;

    @SpringBean
    protected HqlWebServiceClient hqlWebClient;

    public RegistrationsPage(PageParameters parameters) {
        super(parameters);

        Registration registration = create(Registration.class);

        final int MAX = 5;
        List<IColumn<Registration, String>> columns = new ArrayList<IColumn<Registration, String>>();
        columns.add(new PropertyColumn<Registration, String>(labelModel(registration.getFirstName()), name(registration.getFirstName())));
        columns.add(new PropertyColumn<Registration, String>(labelModel(registration.getLastName()), name(registration.getLastName())));
        columns.add(new PropertyColumn<Registration, String>(labelModel(registration.getUsername()), name(registration.getUsername())));
        columns.add(new PropertyColumn<Registration, String>(labelModel(registration.getEmail()), name(registration.getEmail())));
        columns.add(new PropertyColumn<Registration, String>(labelModel(registration.getVerification()), name(registration.getVerification())));
        SortableDataProvider<Registration, String> dataProvider = new SortableDataProvider<Registration, String>() {
            private static final long serialVersionUID = 6812428385117168023L;

            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            public Iterator<Registration> iterator(long first, long count) {
                QueryParameters queryParameters = new QueryParameters();
                queryParameters.setFirst((int) first);
                queryParameters.setMax((int) count);
                queryParameters.setHql("select obj from " + Registration.class.getSimpleName() + " obj");
                return (Iterator) hqlWebClient.execute(queryParameters).getResults().getValue().iterator();
            }

            @Override
            public long size() {
                QueryParameters queryParameters = new QueryParameters();
                queryParameters.setHql("select count(obj.id) from " + Registration.class.getSimpleName() + " obj");
                return Long.parseLong(String.valueOf(hqlWebClient.execute(queryParameters).getResults().getValue().get(0)));
            }

            @Override
            public IModel<Registration> model(Registration object) {
                return Model.of(object);
            }
        };
        DefaultDataTable<Registration, String> table = new DefaultDataTable<Registration, String>("table", columns, dataProvider, MAX);
        add(table);
    }

    protected Model<String> labelModel(Object argument) {
        String property = name(argument);
        String label;
        try {
            label = getString(property);
        } catch (MissingResourceException ex) {
            logger.error("no translation for " + property);
            label = "${" + property + "}";
        }
        return new Model<String>(label);
    }
}

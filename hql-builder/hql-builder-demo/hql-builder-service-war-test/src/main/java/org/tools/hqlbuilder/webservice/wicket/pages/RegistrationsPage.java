package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
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

        LoadableDetachableModel<List<Registration>> model = new LoadableDetachableModel<List<Registration>>() {
            private static final long serialVersionUID = -4920708447371430985L;

            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            protected List<Registration> load() {
                QueryParameters queryParameters = new QueryParameters();
                queryParameters.setHql("from " + Registration.class.getSimpleName());
                return (List) hqlWebClient.execute(queryParameters).getResults().getValue();
            }
        };

        add(new ListView<Registration>("registrationsListview", model) {
            private static final long serialVersionUID = -7343927385998219167L;

            @Override
            protected void populateItem(ListItem<Registration> item) {
                item.add(new Label("registration", item.getModelObject().getFirstName() + " " + item.getModelObject().getLastName()));
            }
        });
    }
}

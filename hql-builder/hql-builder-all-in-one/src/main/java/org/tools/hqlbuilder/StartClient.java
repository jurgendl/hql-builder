package org.tools.hqlbuilder;

import java.util.ArrayList;
import java.util.List;

import org.tools.hqlbuilder.common.QueryParameter;

public class StartClient {
    public static void main(String[] args) {
        HqlServiceClient hqlServiceClient = (HqlServiceClient) new org.springframework.context.support.ClassPathXmlApplicationContext(
                "org/tools/hqlbuilder/applicationContext.xml").getBean("hqlServiceClient");
        System.out.println(hqlServiceClient.getHqlService().getConnectionInfo());
        System.out.println(hqlServiceClient.getHqlService().getSqlForHql("from Persoon p where p.voornaam=?"));
        List<QueryParameter> queryParameters = new ArrayList<QueryParameter>();
        queryParameters.add(new QueryParameter(null, null, "Jurgen", String.class.getName()));
        System.out.println(hqlServiceClient.getHqlService().execute("from Persoon p where p.voornaam=?", queryParameters));
        System.out.println(hqlServiceClient.getHqlService().getHibernateWebResolver());
    }
}

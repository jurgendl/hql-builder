package org.tools.hqlbuilder.test;

import java.net.URI;
import java.util.List;

import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;

public class HqlWebServiceClientTest {
    public static void main(String[] args) {
        try {

            HqlWebServiceClient hc = new HqlWebServiceClient();
            hc.setUri(URI.create("http://localhost:8686/hqlbuilder/rest"));
            hc.afterPropertiesSet();
            // System.out.println(hc.ping());
            // System.out.println(hc.createScript());
            // System.out.println(hc.getConnectionInfo());
            // System.out.println(hc.getHibernateHelpURL());
            // System.out.println(hc.getHqlHelpURL());
            // System.out.println(hc.getLuceneHelpURL());
            // System.out.println(hc.getProject());
            // System.out.println(hc.getSqlForHql("from Pojo"));
            // System.out.println(hc.getClasses());
            // System.out.println(hc.getHibernateInfo());
            // System.out.println(hc.getNamedQueries());
            // System.out.println(hc
            // .getProperties("org.tools.hqlbuilder.test.Pojo"));
            // System.out.println(hc.getReservedKeywords());
            // System.out.println(hc.getUri());
            String hql = "select version from Pojo where id=?";
            List<QueryParameter> parameters = hc.findParameters(hql);
            System.out.println(parameters);
            parameters.get(0).setValueText(null);
            parameters.get(0).setValue(1l);
            System.out.println(hc.execute(new QueryParameters(hql, parameters)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

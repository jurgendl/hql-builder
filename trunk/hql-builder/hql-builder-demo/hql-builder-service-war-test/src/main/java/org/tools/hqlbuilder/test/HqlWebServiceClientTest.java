package org.tools.hqlbuilder.test;

import java.util.List;

import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;

/**
 * http://howtodoinjava.com/2013/08/03/jax-rs-2-0-resteasy-3-0-2-final-client-api-example/
 */
public class HqlWebServiceClientTest {
    public static void main(String[] args) {
        try {
            HqlWebServiceClient hc = new HqlWebServiceClient(Pojo.class.getPackage().getName());
            hc.setServiceUrl("http://localhost:8686/hqlbuilder/rest");
            hc.afterPropertiesSet();
            System.out.println(hc.createScript());
            System.out.println(hc.getConnectionInfo());
            System.out.println(hc.getHibernateHelpURL());
            System.out.println(hc.getHqlHelpURL());
            System.out.println(hc.getLuceneHelpURL());
            System.out.println(hc.getProject());
            System.out.println(hc.getSqlForHql("from Pojo"));
            System.out.println(hc.getClasses());
            System.out.println(hc.getHibernateInfo());
            System.out.println(hc.getNamedQueries());
            System.out.println(hc.getProperties("org.tools.hqlbuilder.test.Pojo"));
            System.out.println(hc.getReservedKeywords());
            String hql = "select version from Pojo where id=?";
            List<QueryParameter> parameters = hc.findParameters(hql);
            System.out.println(parameters);
            parameters.get(0).setValueText("1l");
            System.out.println(hc.execute(new QueryParameters(hql, parameters)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

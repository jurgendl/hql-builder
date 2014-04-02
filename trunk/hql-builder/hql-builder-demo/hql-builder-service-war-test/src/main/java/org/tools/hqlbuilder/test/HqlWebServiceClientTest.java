package org.tools.hqlbuilder.test;

import java.net.URI;

import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;

public class HqlWebServiceClientTest {
    public static void main(String[] args) {
        try {
            HqlWebServiceClient hc = new HqlWebServiceClient();
            hc.setUri(URI.create("http://localhost:80/hqlbuilder/rest"));
            hc.afterPropertiesSet();
            System.out.println(hc.ping());
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
            System.out.println(hc.getUri());
            System.out.println(hc.findParameters("select version from Pojo where id=:id"));
            System.out.println(hc.execute(new QueryParameters("from Pojo")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

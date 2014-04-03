package org.tools.hqlbuilder.test;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;
import org.tools.hqlbuilder.webcommon.resteasy.PojoResource;

/**
 * @see https://jaxb.java.net/guide/index.html
 */
public class HqlWebServiceClientTest {
    private static final String serviceUrl = "http://localhost:8686/hqlbuilder/rest";

    public static void main(String[] args) {
        try {
            test0();
            // test1();
            // test2();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void test0() throws Exception {
        HqlWebServiceClient hc = new HqlWebServiceClient(Pojo.class.getPackage().getName());
        hc.setServiceUrl(serviceUrl);
        hc.afterPropertiesSet();
        System.out.println(hc.getHibernateWebResolver());
    }

    public static void test1() throws Exception {
        HqlWebServiceClient hc = new HqlWebServiceClient(Pojo.class.getPackage().getName());
        hc.setServiceUrl(serviceUrl);
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
        String hql = "from Pojo where id=:id";
        List<QueryParameter> parameters = hc.findParameters(hql);
        System.out.println(parameters);
        parameters.get(0).setValueText("1l");
        System.out.println(hc.execute(new QueryParameters(hql, parameters)));
    }

    public static void test2() throws Exception {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target(serviceUrl);
        ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
        PojoResource hc = rtarget.proxy(PojoResource.class);

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

        // FIXME
        String hql = "from Pojo where id=:id";
        List<QueryParameter> parameters = hc.findParameters(hql).getValue();
        System.out.println(parameters);
        parameters.get(0).setValueText("1l");
        System.out.println(hc.execute(new QueryParameters(hql, parameters)));
    }
}

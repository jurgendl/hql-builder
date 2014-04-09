package org.tools.hqlbuilder.webservice;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.test.User;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;
import org.tools.hqlbuilder.webcommon.resteasy.PojoResource;

/**
 * @see https://jaxb.java.net/guide/index.html
 */
public class HqlWebServiceClientTest {
    private static final String serviceUrl = "http://localhost:80/hqlbuilder/xml";

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
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope("localhost", 80), new UsernamePasswordCredentials("hqladmin", "hqladmin"));
        CloseableHttpClient httpclient = HttpClients.custom()//
                .setDefaultCredentialsProvider(credsProvider)//
                .setRedirectStrategy(new DefaultRedirectStrategy() {
                    @Override
                    public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                        boolean redirected = super.isRedirected(request, response, context);
                        return redirected;
                    }

                    @Override
                    public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                        HttpUriRequest redirect = super.getRedirect(request, response, context);
                        return redirect;
                    }
                })//
                .build();//

        try {
            HttpGet httpget = new HttpGet("http://localhost:80/hqlbuilder/pages");

            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                InputStream in = response.getEntity().getContent();
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                out.close();
                System.out.println(new String(out.toByteArray()));
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    public static void test1() throws Exception {
        HqlWebServiceClient hc = getClient();
        System.out.println(hc.ping());
        System.out.println(hc.createScript());
        System.out.println(hc.getConnectionInfo());
        System.out.println(hc.getHibernateHelpURL());
        System.out.println(hc.getHqlHelpURL());
        System.out.println(hc.getLuceneHelpURL());
        System.out.println(hc.getProject());
        System.out.println(hc.getSqlForHql("from " + User.class.getSimpleName()));
        System.out.println(hc.getClasses());
        System.out.println(hc.getHibernateInfo());
        System.out.println(hc.getNamedQueries());
        System.out.println(hc.getProperties(User.class.getName()));
        System.out.println(hc.getReservedKeywords());
        String hql = "from " + User.class.getSimpleName() + " where id=:id";
        List<QueryParameter> parameters = hc.findParameters(hql);
        System.out.println(parameters);
        parameters.get(0).setValueText("1l");
        System.out.println(hc.execute(new QueryParameters(hql, parameters)));
    }

    public static void test2() throws Exception {
        PojoResource hc = getRestClient();
        System.out.println(hc.ping());
        System.out.println(hc.createScript());
        System.out.println(hc.getConnectionInfo());
        System.out.println(hc.getHibernateHelpURL());
        System.out.println(hc.getHqlHelpURL());
        System.out.println(hc.getLuceneHelpURL());
        System.out.println(hc.getProject());
        System.out.println(hc.getSqlForHql("from " + User.class.getSimpleName()));
        System.out.println(hc.getClasses());
        System.out.println(hc.getHibernateInfo());
        System.out.println(hc.getNamedQueries());
        System.out.println(hc.getProperties(User.class.getName()));
        System.out.println(hc.getReservedKeywords());

        // FIXME
        String hql = "from " + User.class.getSimpleName() + " where id=:id";
        List<QueryParameter> parameters = hc.findParameters(hql).getValue();
        System.out.println(parameters);
        parameters.get(0).setValueText("1l");
        System.out.println(hc.execute(new QueryParameters(hql, parameters)));
    }

    public static HqlWebServiceClient getClient() throws Exception {
        HqlWebServiceClient hc = new HqlWebServiceClient(User.class.getPackage().getName());
        hc.setServiceUrl(serviceUrl);
        hc.afterPropertiesSet();
        return hc;
    }

    public static PojoResource getRestClient() {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target(serviceUrl);
        ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
        PojoResource hc = rtarget.proxy(PojoResource.class);
        return hc;
    }
}

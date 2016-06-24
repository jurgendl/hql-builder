package org.tools.hqlbuilder.webservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.demo.User;
import org.tools.hqlbuilder.webclient.HqlWebServiceClient;
import org.tools.hqlbuilder.webclient.ServiceUrlBuilder;
import org.tools.hqlbuilder.webcommon.ResteasyProvidersBean;
import org.tools.hqlbuilder.webcommon.resteasy.JAXBContextResolver;
import org.tools.hqlbuilder.webcommon.resteasy.PojoResource;

/**
 * @see https://jaxb.java.net/guide/index.html
 */
public class HqlWebServiceClientTest {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = null;
        try {
            context = new ClassPathXmlApplicationContext("org/tools/hqlbuilder/webservice/test-context.xml");
            Properties cfg = Properties.class.cast(context.getBean("webProperties"));
            ServiceUrlBuilder sub = new ServiceUrlBuilder(cfg);
            // test0(cfg, sub.getServiceUrl());
            test1(sub.getServiceUrl());
            // test2(sub.getServiceUrl());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        } finally {
            if (context != null) {
                try {
                    context.close();
                } catch (Exception ex2) {
                    //
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void test0(Properties cfg, String url) throws Exception {
        String base = url;
        url = url + "/classes";
        try {
            org.apache.http.impl.client.DefaultHttpClient httpclient = new org.apache.http.impl.client.DefaultHttpClient();
            try {
                // try to get the home page
                HttpGet httpget = new HttpGet(url);
                HttpResponse httpClientResponse = httpclient.execute(httpget);
                HttpEntity entity = httpClientResponse.getEntity();

                // check status and close entity stream
                // System.out.println("Login form get: " + httpClientResponse.getStatusLine());
                EntityUtils.consume(entity);

                // check cookies
                // System.out.println("Initial set of cookies:");
                // List<org.apache.http.cookie.Cookie> cookies = httpclient.getCookieStore().getCookies();
                // System.out.println(cookies);

                HttpPost httppost = new HttpPost(base + cfg.getProperty("login"));

                // Prepare post parameters
                List<NameValuePair> nvps = new ArrayList<>();
                nvps.add(new BasicNameValuePair(cfg.getProperty("username.propertyname"), "hqladmin"));
                nvps.add(new BasicNameValuePair(cfg.getProperty("password.propertyname"), "hqladmin"));
                // httppost.addHeader("Referer", "http://localhost/hqlbuilder/xml/classes");
                httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

                httpClientResponse = httpclient.execute(httppost);

                // copy response headers and determine redirect location
                Header[] allHeaders = httpClientResponse.getAllHeaders();
                // System.out.println("Headers: ");
                String location = "";
                for (Header header : allHeaders) {
                    // System.out.println(header);
                    if ("location".equalsIgnoreCase(header.getName())) {
                        location = header.getValue();
                    }
                    httpClientResponse.addHeader(header.getName(), header.getValue());
                }

                // check response body
                entity = httpClientResponse.getEntity();
                // System.out.println("Response content: " + httpClientResponse.getStatusLine());
                // System.out.println(EntityUtils.toString(entity)); // always empty
                EntityUtils.consume(entity);

                // check cookies
                // System.out.println("Post logon cookies:");
                // cookies = httpclient.getCookieStore().getCookies();
                // System.out.println(cookies);

                // populate redirect information in response
                // System.out.println(">> " + httpClientResponse.getHeaders("Location")[0].getValue());
                httpClientResponse.setStatusCode(httpClientResponse.getStatusLine().getStatusCode()); // 302

                // test if server-side get works for home page at this point (it does)
                httpget = new HttpGet(location);
                httpClientResponse = httpclient.execute(httpget);
                entity = httpClientResponse.getEntity();

                // print response body (all home content is loaded)
                // System.out.println("home get: " + httpClientResponse.getStatusLine());
                // System.out.println("Response content: " + httpClientResponse.getStatusLine());
                System.out.println(EntityUtils.toString(entity));
                EntityUtils.consume(entity);
            } finally {
                httpclient.getConnectionManager().shutdown();
                httpclient.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void test1(String url) throws Exception {
        HqlWebServiceClient hc = getClient(url);
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
        // System.out.println(hc.execute(new QueryParameters(hql, parameters)));
    }

    public static void test2(String url) throws Exception {
        PojoResource hc = getRestClient(url);
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

        // FIXME test
        // String hql = "from " + User.class.getSimpleName() + " where id=:id";
        // List<QueryParameter> parameters = hc.findParameters(hql).getValue();
        // System.out.println(parameters);
        // parameters.get(0).setValueText("1l");
        // System.out.println(hc.execute(new QueryParameters(hql, parameters)));
    }

    public static HqlWebServiceClient getClient(String url) throws Exception {
        String pack = User.class.getPackage().getName();
        JAXBContextResolver jaxbContextResolver = new JAXBContextResolver(pack);
        ResteasyProvidersBean restEasyProvidersBean = new ResteasyProvidersBean(jaxbContextResolver);
        HqlWebServiceClient hc = new HqlWebServiceClient();
        hc.setResteasyProvider(restEasyProvidersBean.getInstance());
        hc.setServiceUrl(url);
        hc.afterPropertiesSet();
        return hc;
    }

    public static PojoResource getRestClient(String url) {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target(url);
        ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
        PojoResource hc = rtarget.proxy(PojoResource.class);
        return hc;
    }
}

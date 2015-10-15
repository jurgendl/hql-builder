package org.tools.hqlbuilder.webservice.js;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import org.tools.hqlbuilder.webservice.wicket.CachingUrlResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

// https://console.developers.google.com/project/
// https://developers.google.com/identity/sign-in/web/sign-in
// https://developers.google.com/identity/sign-in/web/backend-auth
// https://developers.google.com/identity/sign-in/web/devconsole-project
// <div class="g-signin2" data-onsuccess="onSignIn" data-theme="dark"></div>
public class GoogleLogin {
    public static CachingUrlResourceReference JS_PLATFORM = new CachingUrlResourceReference(URI.create("http://apis.google.com/js/platform.js"),
            "platform");

    public static JavaScriptResourceReference JS_LOGIN = new JavaScriptResourceReference(GoogleLogin.class, "googlelogin.js");

    static {
        try {
            JS_LOGIN.addJavaScriptResourceReferenceDependency(JS_PLATFORM);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void init(WicketApplication app) {
        Properties p = new Properties();
        try (FileInputStream inStream = new FileInputStream(System.getProperty("user.home") + "/google.app.client_id.properties")) {
            p.load(inStream);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        app.setGoogleSigninClientId(p.getProperty("google.app.client_id"));
    }
}

package org.tools.hqlbuilder.webservice.wicket;

import java.util.Locale;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebResponse;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;

public class WicketSession extends WebSession {
    private static final long serialVersionUID = 5401902370873451702L;

    public static final String LOCALE = "locale";

    public static final String THEME = "theme";

    /** default "overcast" */
    protected String jqueryUITheme = "overcast";

    protected boolean pushToCookies = true;

    public WicketSession(Request request) {
        super(request);
        Injector.get().inject(this);
        {
            pushToCookies = false;
            try {
                ServletWebRequest webRequest = (ServletWebRequest) request;
                Cookie cookieTheme = webRequest.getCookie(THEME);
                if (cookieTheme != null && StringUtils.isNotBlank(cookieTheme.getValue())) {
                    jqueryUITheme = cookieTheme.getValue();
                }
                Cookie cookieLocale = webRequest.getCookie(LOCALE);
                if (cookieLocale != null && StringUtils.isNotBlank(cookieLocale.getValue())) {
                    String[] tmp = cookieLocale.getValue().split("_");
                    if (tmp.length == 3) {
                        setLocale(new Locale(tmp[0], tmp[1], tmp[2]));
                    } else if (tmp.length == 2) {
                        setLocale(new Locale(tmp[0], tmp[1]));
                    } else {
                        setLocale(new Locale(tmp[0]));
                    }
                }
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
            pushToCookies = true;
        }
    }

    public static WicketSession get() {
        return WicketSession.class.cast(Session.get());
    }

    public String getJQueryUITheme() {
        return this.jqueryUITheme;
    }

    public void setJQueryUITheme(String theme) {
        this.jqueryUITheme = theme;
        if (pushToCookies) {
            getWebResponse().addCookie(new Cookie(THEME, theme));
        }
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        if (pushToCookies) {
            getWebResponse().addCookie(new Cookie(LOCALE, locale == null ? "en" : locale.toString()));
        }
    }

    protected ServletWebRequest getWebRequest() {
        return (ServletWebRequest) RequestCycle.get().getRequest();
    }

    protected ServletWebResponse getWebResponse() {
        return (ServletWebResponse) RequestCycle.get().getResponse();
    }
}

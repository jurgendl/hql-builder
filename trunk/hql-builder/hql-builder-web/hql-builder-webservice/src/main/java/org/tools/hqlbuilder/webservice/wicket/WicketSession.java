package org.tools.hqlbuilder.webservice.wicket;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
        init();
    }

    protected void init() {
        try {
            pushToCookies = false;
            jqueryUITheme = loadCookie(THEME, "overcast");
            String localeCookieValue = loadCookie(LOCALE, null);
            if (StringUtils.isNotBlank(localeCookieValue)) {
                String[] tmp = localeCookieValue.split("_");
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
        } finally {
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
            saveCookie(THEME, theme, 365);
        }
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        if (pushToCookies) {
            saveCookie(LOCALE, locale == null ? "en" : locale.toString(), 365);
        }
    }

    public String loadCookie(String cookieName, String defaultValue) {
        List<Cookie> cookies = ((WebRequest) RequestCycle.get().getRequest()).getCookies();
        if (cookies == null) {
            return defaultValue;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return defaultValue;
    }

    public void saveCookie(String cookieName, String cookieValue, int expiryTimeInDays) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(expiryTimeInDays));
        ((WebResponse) RequestCycle.get().getResponse()).addCookie(cookie);
    }

    public void removeCookieIfPresent(String cookieName) {
        List<Cookie> cookies = ((WebRequest) RequestCycle.get().getRequest()).getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                ((WebResponse) RequestCycle.get().getResponse()).clearCookie(cookie);
                return;
            }
        }
    }

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }
}

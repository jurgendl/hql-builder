package org.tools.hqlbuilder.webservice.wicket;

import java.io.IOException;
import java.util.Properties;

import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

public class WicketSession extends WebSession {
    private static final long serialVersionUID = 5401902370873451702L;

    public static final String STYLE_PATH = "org/tools/hqlbuilder/webservice/css/";

    private String jqueryUITheme = "redmond";

    private Properties styling = null;

    public WicketSession(Request request) {
        super(request);
        Injector.get().inject(this);
    }

    public static WicketSession get() {
        return WicketSession.class.cast(Session.get());
    }

    public String getJQueryUITheme() {
        return this.jqueryUITheme;
    }

    public void setJQueryUITheme(String theme) {
        this.jqueryUITheme = theme;
    }

    public Properties getStyling() {
        if (styling == null) {
            styling = new Properties();
            try {
                styling.load(DefaultWebPage.class.getClassLoader().getResourceAsStream(STYLE_PATH + "settings.zuss"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return this.styling;
    }
}

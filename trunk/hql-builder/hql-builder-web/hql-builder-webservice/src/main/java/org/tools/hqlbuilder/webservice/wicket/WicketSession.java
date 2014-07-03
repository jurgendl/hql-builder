package org.tools.hqlbuilder.webservice.wicket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

public class WicketSession extends WebSession {
    private static final long serialVersionUID = 5401902370873451702L;

    public static final String STYLE_PATH = "org/tools/hqlbuilder/webservice/css/";

    private String jqueryUITheme = "redmond";

    private LinkedHashMap<String, String> styling = null;

    private long lastModified = -1l;

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

    public LinkedHashMap<String, String> getStyling() {
        URL resource = DefaultWebPage.class.getClassLoader().getResource(STYLE_PATH + "settings.zuss");
        long remoteLastModified;
        try {
            URLConnection connection = resource.openConnection();
            remoteLastModified = connection.getLastModified();
        } catch (IOException ex1) {
            remoteLastModified = Long.MAX_VALUE;
        }
        if (styling == null || this.lastModified < remoteLastModified) {
            lastModified = remoteLastModified;
            styling = new LinkedHashMap<String, String>();
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(resource.openStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        styling.put(parts[0].trim(), parts[1].trim());
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception ex) {
                        //
                    }
                }
            }
        }
        return this.styling;
    }

    public void printStyling(PrintStream out) {
        if (styling != null) {
            for (Map.Entry<String, String> entry : styling.entrySet()) {
                out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }
}

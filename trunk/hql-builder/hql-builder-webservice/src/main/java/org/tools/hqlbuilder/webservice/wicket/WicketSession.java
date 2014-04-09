package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

public class WicketSession extends WebSession {
    private static final long serialVersionUID = 5401902370873451702L;

    public WicketSession(Request request) {
        super(request);
        Injector.get().inject(this);
    }
}
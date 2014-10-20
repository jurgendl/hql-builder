package org.tools.hqlbuilder.webservice.wicket.components;

import java.net.URI;
import java.net.URL;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class LinkPanel extends Panel {
    public static final String LINK_ID = "link";

    public static enum LinkType {
        url, email;
    }

    private static final long serialVersionUID = -7352081661850450279L;

    private LinkType linkType;

    public LinkPanel(String id, final IModel<Object> model, final IModel<String> labelModel, LinkType linkType) {
        super(id);
        this.linkType = linkType;
        add(getLink(model, labelModel));
    }

    protected AbstractLink getLink(final IModel<Object> model, final IModel<String> labelModel) {
        switch (linkType) {
            case email:
                return getEmailLink(model, labelModel);
            default:
            case url:
                return getURLLink(model, labelModel);
        }
    }

    protected AbstractLink getURLLink(final IModel<Object> model, final IModel<String> labelModel) {
        return new ExternalLink(LINK_ID, new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = 6336814546294579370L;

            @Override
            public String getObject() {
                Object object = model.getObject();
                if (object == null) {
                    return null;
                }
                if (object instanceof String) {
                    return String.class.cast(object);
                }
                if (object instanceof URL) {
                    return URL.class.cast(object).toExternalForm();
                }
                if (object instanceof URI) {
                    return URI.class.cast(object).toASCIIString();
                }
                throw new UnsupportedOperationException("type for url not supported: " + object.getClass().getName());
            }
        }, labelModel);
    }

    protected AbstractLink getEmailLink(final IModel<Object> model, final IModel<String> labelModel) {
        return new ExternalLink(LINK_ID, new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = -2903758951408761993L;

            @Override
            public String getObject() {
                return "mailto:" + model.getObject();
            }
        }, labelModel);
    }
}
package org.tools.hqlbuilder.webservice.wicket.components;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

/**
 * <span wicket:id="growl"></span>
 *
 * @see http://www.primefaces.org/primeui/growl.html
 */
public class Growl extends Panel {
    private static final long serialVersionUID = 8477795062120562100L;

    public static final String ID = "growl";

    public static final String GROWL_MESSAGE = "growlmessage";

    public Growl() {
        super(ID);
        setRenderBodyOnly(true);
        setOutputMarkupPlaceholderTag(false);
        setOutputMarkupId(false);
        add(new Label(GROWL_MESSAGE, Model.of("")));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_JS));
        response.render(CssHeaderItem.forReference(PrimeUI.PRIME_UI_CSS));
        String js = "$(function() { " + "\n";
        js += "$('#" + Growl.this.get(GROWL_MESSAGE).getMarkupId() + "').puigrowl();" + "\n";
        js += "});" + "\n";
        response.render(JavaScriptHeaderItem.forScript(js, "puigrowl"));
    }

    public void message(AjaxRequestTarget target, GrowlMessage... message) {
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < message.length; i++) {
            msg.append(message[i].toString()).append(",");
        }
        target.appendJavaScript(";$('#" + Growl.this.get(GROWL_MESSAGE).getMarkupId() + "').puigrowl('show',["
                + msg.delete(msg.length(), msg.length()).toString() + "]);");
    }

    public static class GrowlMessage implements Serializable {
        private static final long serialVersionUID = -3655733150961814228L;

        private String message;

        private String title;

        private GrowlSeverity severity;

        public GrowlMessage() {
            super();
        }

        public GrowlMessage(GrowlSeverity severity, String title, String message) {
            this.severity = severity;
            this.title = title;
            this.message = message;
        }

        @Override
        public String toString() {
            return "{severity:'" + severity + "',summary:'" + title + "',detail:'" + message + "'}";
        }

        public String getMessage() {
            return this.message;
        }

        public GrowlSeverity getSeverity() {
            return this.severity;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setSeverity(GrowlSeverity severity) {
            this.severity = severity;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static enum GrowlSeverity {
        info, warn, error;
    }
}

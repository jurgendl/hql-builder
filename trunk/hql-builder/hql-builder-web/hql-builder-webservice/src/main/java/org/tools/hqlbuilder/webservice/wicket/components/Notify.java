package org.tools.hqlbuilder.webservice.wicket.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.tools.hqlbuilder.webservice.resources.prime.PrimeUI;

/**
 * <span wicket:id="notify"></span>
 */
public class Notify extends Panel {
    private static final long serialVersionUID = 8477795062120562100L;

    public static final String ID = "notify";

    public static final String NOTIFY_BOTTOM = "notifybottom";

    public static final String NOTIFY_TOP = "notifytop";

    public Notify() {
        super(ID);
        setRenderBodyOnly(true);
        setOutputMarkupPlaceholderTag(false);
        setOutputMarkupId(false);
        add(new Label(NOTIFY_TOP, Model.of("")));
        add(new Label(NOTIFY_BOTTOM, Model.of("")));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_JS));
        response.render(CssHeaderItem.forReference(PrimeUI.PRIME_UI_CSS));
        String js = "$(function() { " + "\n";
        js += "$('#" + Notify.this.get(NOTIFY_TOP).getMarkupId() + "').puinotify({easing: 'easeInOutCirc', position: 'top'});" + "\n";
        js += "$('#" + Notify.this.get(NOTIFY_BOTTOM).getMarkupId() + "').puinotify({easing: 'easeInOutCirc', position: 'bottom'});" + "\n";
        js += "});" + "\n";
        response.render(JavaScriptHeaderItem.forScript(js, getClass().getName()));
    }

    public void bottomMessage(AjaxRequestTarget target, String message) {
        String js = "$(function() { " + "\n";
        js += "$('#" + Notify.this.get(NOTIFY_BOTTOM).getMarkupId() + "').puinotify('show','" + message + "');" + "\n";
        js += "});" + "\n";
        target.appendJavaScript(";" + js);
    }

    public void topMessage(AjaxRequestTarget target, String message) {
        String js = "$(function() { " + "\n";
        js += "$('#" + Notify.this.get(NOTIFY_TOP).getMarkupId() + "').puinotify('show','" + message + "');" + "\n";
        js += "});" + "\n";
        target.appendJavaScript(";" + js);
    }
}

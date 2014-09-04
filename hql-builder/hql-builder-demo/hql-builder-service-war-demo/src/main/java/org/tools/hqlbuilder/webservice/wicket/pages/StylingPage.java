package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.components.Growl;
import org.tools.hqlbuilder.webservice.wicket.components.Growl.GrowlMessage;
import org.tools.hqlbuilder.webservice.wicket.components.Growl.GrowlSeverity;
import org.tools.hqlbuilder.webservice.wicket.components.Notify;
import org.tools.hqlbuilder.webservice.wicket.components.ThemesPanel;

@SuppressWarnings("serial")
@MountedPage("/styling")
public class StylingPage extends BasePage {
    public StylingPage(PageParameters parameters) {
        super(parameters);
        setStatelessHint(false);

        add(new ThemesPanel("themespanel"));

        {
            final Notify notify = new Notify();
            add(notify);
            add(new AjaxFallbackLink<String>("testnotify", Model.of("testnotify")) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    notify.topMessage(target, "topmessage");
                    notify.bottomMessage(target, "bottommessage");
                }
            });
        }
        {
            final Growl growl = new Growl();
            add(growl);
            add(new AjaxFallbackLink<String>("testgrowl", Model.of("testgrowl")) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    growl.message(target, new GrowlMessage(GrowlSeverity.error, "error", "error"), new GrowlMessage(GrowlSeverity.info, "info",
                            "info"), new GrowlMessage(GrowlSeverity.warn, "warn", "warn"));
                }
            });
        }

        ExampleForm stylingform = new ExampleForm("stylingform");
        add(stylingform);
    }
}

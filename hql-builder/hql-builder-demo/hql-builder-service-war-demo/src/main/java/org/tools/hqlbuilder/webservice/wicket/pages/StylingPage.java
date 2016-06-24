package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.components.Growl;
import org.tools.hqlbuilder.webservice.wicket.components.Growl.GrowlMessage;
import org.tools.hqlbuilder.webservice.wicket.components.Growl.GrowlSeverity;
import org.tools.hqlbuilder.webservice.wicket.components.Notify;
import org.wicketstuff.annotation.mount.MountPath;

@SuppressWarnings("serial")
@MountPath("/styling")
public class StylingPage extends BasePage {
    private static final long serialVersionUID = 1L;

	public StylingPage(PageParameters parameters) {
        super(parameters);
        setStatelessHint(false);

        {
            final Notify notify = new Notify();
            add(notify);
            add(new AjaxFallbackLink<String>("testnotify", Model.of("testnotify")) {
                private static final long serialVersionUID = 1L;

				@Override
                public void onClick(AjaxRequestTarget target) {
                    notify.topMessage(target, "topmessage");
                    notify.bottomMessage(target, "bottommessage");
                }
            }.setMarkupId("testnotify"));
        }
        {
            final Growl growl = new Growl();
            add(growl);
            add(new AjaxFallbackLink<String>("testgrowl", Model.of("testgrowl")) {
                private static final long serialVersionUID = 1L;

				@Override
                public void onClick(AjaxRequestTarget target) {
                    growl.message(target, //
                            new GrowlMessage(GrowlSeverity.error, "error", "error"), //
                            new GrowlMessage(GrowlSeverity.info, "info", "info"),//
                            new GrowlMessage(GrowlSeverity.warn, "warn", "warn")//
                    );
                }
            }.setMarkupId("testgrowl"));
        }

        add(new ExampleForm("stylingform"));
    }
}

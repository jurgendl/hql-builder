package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.Arrays;
import java.util.Locale;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;

@SuppressWarnings("serial")
@MountedPage("/settings")
public class SettingsPage extends BasePage {
    public SettingsPage(PageParameters parameters) {
        super(parameters);

        add(new LocalesPanel("localespanel", Arrays.asList(Locale.ENGLISH, new Locale("nl"))));
    }
}

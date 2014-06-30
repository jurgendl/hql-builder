package org.tools.hqlbuilder.webservice.wicket.pages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.tools.hqlbuilder.webservice.wicket.WicketSession;

@SuppressWarnings("serial")
public class ThemesPanel extends Panel {
    protected List<String> themes = null;

    protected StatelessForm<Object> themeForm;

    protected DropDownChoice<String> changeTheme;

    public ThemesPanel(String id) {
        super(id);

        getThemes(); // inititialise themes

        Model<String> themeModel = new Model<String>();
        String sessionTheme = WicketSession.get().getJQueryUITheme();
        themeModel.setObject(sessionTheme);

        this.changeTheme = new DropDownChoice<String>("themeOptions", themeModel, themes, new IChoiceRenderer<String>() {
            @Override
            public Object getDisplayValue(String object) {
                return object;
            }

            @Override
            public String getIdValue(String object, int index) {
                return object;
            }
        }) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }

            @Override
            protected void onSelectionChanged(String newSelection) {
                WicketSession.get().setJQueryUITheme(newSelection);
            }
        };
        this.changeTheme.setNullValid(false);
        themeForm = new StatelessForm<Object>("themeForm") {
            @Override
            protected void onSubmit() {
                WicketSession.get().setJQueryUITheme(changeTheme.getModelObject());
            }
        };
        themeForm.setMarkupId(themeForm.getId());
        add(themeForm.add(changeTheme));
    }

    public List<String> getThemes() {
        if (themes == null) {
            themes = new ArrayList<String>();
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(ThemesPanel.class.getClassLoader().getResourceAsStream(
                        "org/tools/hqlbuilder/webservice/jquery/ui/jquery-ui-themes-1.10.4/themes/themes.list")));
                String theme;
                while ((theme = in.readLine()) != null) {
                    themes.add(theme);
                }
                in.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return this.themes;
    }

    public void setThemes(List<String> themes) {
        this.themes = themes;
    }
}

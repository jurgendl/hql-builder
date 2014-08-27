package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;
import org.tools.hqlbuilder.webservice.wicket.components.SocialPanel;
import org.tools.hqlbuilder.webservice.wicket.components.SocialPanel.Social;
import org.tools.hqlbuilder.webservice.wicket.forms.ColorPickerPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.ColorPickerSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.ColorPickerSettings.ColorFormat;

@SuppressWarnings("serial")
@MountedPage("/styling")
public class StylingPage extends BasePage {
    public StylingPage(PageParameters parameters) {
        super(parameters);
        setStatelessHint(false);

        ExampleForm stylingform = new ExampleForm("stylingform");
        stylingform.nextRow();

        boolean dont = false;
        if (dont) {
            stylingform.getFormSettings().setColumns(3).setLabelWidth("22em");

            ColorPickerSettings colorPickerSettings = new ColorPickerSettings();
            colorPickerSettings.setClickoutFiresChange(true);
            colorPickerSettings.setPreferredFormat(ColorFormat.hsl);
            colorPickerSettings.setShowButtons(true);
            colorPickerSettings.setShowPalette(true);
            colorPickerSettings.setShowInitial(true);
            colorPickerSettings.setShowInput(true);
            for (String _key_ : WicketApplication.get().getZussStyle().keys()) {
                final String property = _key_.toString().substring(1);
                ColorPickerPanel cpp = new ColorPickerPanel(stylingform.getDefaultModel(), property, stylingform.getFormSettings(),
                        ColorPickerSettings.class.cast(colorPickerSettings.clone())) {
                    @Override
                    public IModel<String> getLabelModel() {
                        return new LoadableDetachableModel<String>() {
                            @Override
                            protected String load() {
                                return property.replace('_', ' ').replace('[', ' ').replace(']', ' ').trim();
                            }
                        };
                    }

                    @Override
                    public String getPropertyName() {
                        return property;
                    }

                    @Override
                    public IModel<String> getValueModel() {
                        return new IModel<String>() {
                            @Override
                            public void detach() {
                                //
                            }

                            @Override
                            public String getObject() {
                                String value = WicketApplication.get().getZussStyle().getStyle("@" + property);
                                value = StringUtils.isBlank(value) ? null : value.substring(0, value.length() - 1);
                                return value;
                            }

                            @Override
                            public void setObject(String value) {
                                WicketApplication.get().getZussStyle().setStyle("@" + property, value + ";");
                            }
                        };
                    }
                };
                stylingform.addDefaultRow(cpp);
            }
        }

        if (true) {
            ArrayList<Social> options = new ArrayList<Social>(new TreeSet<Social>(Arrays.asList(Social.values())));
            add(new ListView<Social>("socialbuttons", options) {
                private static final long serialVersionUID = -7495456081110874114L;

                @Override
                protected void populateItem(ListItem<Social> item) {
                    item.add(new SocialPanel("socialbutton", item.getModel()));
                }
            });
            add(new ListView<Social>("socialbars", options) {
                private static final long serialVersionUID = -2422255718832136362L;

                @Override
                protected void populateItem(ListItem<Social> item) {
                    item.add(new SocialPanel("socialbar", item.getModel(), true));
                }
            });
        }

        add(stylingform);
    }
}

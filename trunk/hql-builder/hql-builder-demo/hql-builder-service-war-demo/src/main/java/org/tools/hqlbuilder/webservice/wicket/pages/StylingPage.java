package org.tools.hqlbuilder.webservice.wicket.pages;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;
import org.tools.hqlbuilder.webservice.wicket.forms.ColorPickerPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.ColorPickerSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.ColorPickerSettings.ColorFormat;
import org.tools.hqlbuilder.webservice.wicket.zuss.ZussResourceReference;

@SuppressWarnings("serial")
@MountedPage("/styling")
public class StylingPage extends BasePage {
    public StylingPage(PageParameters parameters) {
        super(parameters);
        setStatelessHint(false);

        ExampleForm stylingform = new ExampleForm("stylingform");

        boolean dont = false;
        if (dont) {
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

        add(stylingform);
    }

    /**
     * @see org.tools.hqlbuilder.webservice.wicket.DefaultWebPage#addDynamicResources(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    protected void addDynamicResources(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(new ZussResourceReference(WicketCSSRoot.class, "horizontalmenu.css")));
        response.render(CssHeaderItem.forReference(new ZussResourceReference(WicketCSSRoot.class, "form.css")));
        response.render(CssHeaderItem.forReference(new ZussResourceReference(WicketCSSRoot.class, "table.css")));
    }
}

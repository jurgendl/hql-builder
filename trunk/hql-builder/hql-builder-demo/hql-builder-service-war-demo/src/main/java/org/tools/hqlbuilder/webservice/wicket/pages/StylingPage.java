package org.tools.hqlbuilder.webservice.wicket.pages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.WicketSession;
import org.tools.hqlbuilder.webservice.wicket.forms.ColorPickerPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.ColorPickerPanel.ColorFormat;
import org.tools.hqlbuilder.webservice.wicket.forms.ColorPickerPanel.ColorPickerSettings;
import org.zkoss.zuss.Resolver;
import org.zkoss.zuss.Zuss;
import org.zkoss.zuss.impl.out.BuiltinResolver;
import org.zkoss.zuss.metainfo.ZussDefinition;

@SuppressWarnings("serial")
@MountedPage("/styling")
public class StylingPage extends BasePage {
    public StylingPage(PageParameters parameters) {
        super(parameters);
        setStatelessHint(false);

        ExampleForm stylingform = new ExampleForm("stylingform");

        ColorPickerSettings colorPickerSettings = new ColorPickerSettings();
        colorPickerSettings.setClickoutFiresChange(true);
        colorPickerSettings.setPreferredFormat(ColorFormat.hsl);
        colorPickerSettings.setShowButtons(true);
        colorPickerSettings.setShowPalette(true);
        colorPickerSettings.setShowInitial(true);
        colorPickerSettings.setShowInput(true);
        for (Object _key_ : WicketSession.get().getStyling().keySet()) {
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
                            String value = WicketSession.get().getStyling().get("@" + property);
                            value = StringUtils.isBlank(value) ? null : value.substring(0, value.length() - 1);
                            return value;
                        }

                        @Override
                        public void setObject(String value) {
                            WicketSession.get().getStyling().put("@" + property, value + ";");
                        }
                    };
                }
            };
            stylingform.addRow(cpp);
        }

        add(stylingform);
    }

    /**
     * @see org.tools.hqlbuilder.webservice.wicket.DefaultWebPage#addDynamicResources(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    protected void addDynamicResources(IHeaderResponse response) {
        try {
            zuss(response, "horizontalmenu");
            zuss(response, "form");
            zuss(response, "table");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void zuss(IHeaderResponse response, String cssId) throws IOException, UnsupportedEncodingException {
        String charset = "utf-8";
        ZussDefinition parsed = Zuss.parse(DefaultWebPage.class.getClassLoader().getResourceAsStream(WicketSession.STYLE_PATH + cssId + ".zuss"),
                charset, null, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Resolver resolver = new BuiltinResolver() {
            @Override
            public Object getVariable(String name) {
                String value = WicketSession.get().getStyling().get("@" + name);
                if (value == null) {
                    return super.getVariable(name);
                }
                value = value.trim();
                return value.substring(0, value.length() - 1);
            }
        };
        Zuss.translate(parsed, out, charset, resolver);
        String css = new String(out.toByteArray(), charset);
        CssContentHeaderItem cssHeader = CssHeaderItem.forCSS(css, cssId + ".css");
        response.render(cssHeader);
    }
}

package org.tools.hqlbuilder.webservice.wicket.pages;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.create;
import static org.tools.hqlbuilder.webservice.wicket.WebHelper.name;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.wicket.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.MountedPage;
import org.tools.hqlbuilder.webservice.wicket.WicketSession;
import org.tools.hqlbuilder.webservice.wicket.forms.ColorPickerPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
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

        Model<Styling> model = Model.of(new Styling());
        Styling proxy = create(Styling.class);
        FormPanel<Styling> stylingform = new FormPanel<Styling>("stylingform", model, true, new DefaultFormActions<Styling>().setAjax(false));

        FormElementSettings fset = new FormElementSettings().setRequired(true);
        stylingform.addEmailTextField(name(proxy.getTestEmail()), fset);
        stylingform.addTextField(name(proxy.getTestString()), fset);
        stylingform.addDatePicker(name(proxy.getTestDate()), fset);
        stylingform.addPasswordTextField(name(proxy.getPassword()), new FormElementSettings());

        for (Object keyO : WicketSession.get().getStyling().keySet()) {
            String property = keyO.toString().substring(1);
            ColorPickerPanel cpp = new ColorPickerPanel(stylingform.getDefaultModel(), property, stylingform.getFormSettings(),
                    new FormElementSettings()) {
                @Override
                protected IModel<String> getValueModel() {
                    return new IModel<String>() {
                        @Override
                        public void detach() {
                            //
                        }

                        @Override
                        public String getObject() {
                            String value = WicketSession.get().getStyling().getProperty("@" + property);
                            value = StringUtils.isBlank(value) ? null : value.substring(0, value.length() - 1);
                            return value;
                        }

                        @Override
                        public void setObject(String value) {
                            WicketSession.get().getStyling().setProperty("@" + property, value + ";");
                        }
                    };
                }
            };
            stylingform.addRow(property, cpp);
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
                String value = WicketSession.get().getStyling().getProperty("@" + name);
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

    public static class Styling implements Serializable {
        private char[] password;

        private String testEmail = "test@gmail.com";

        private String testString = "testString";

        @SuppressWarnings("deprecation")
        private Date testDate = new Date(2000, 4, 4);

        public String getTestString() {
            return this.testString;
        }

        public void setTestString(String testString) {
            this.testString = testString;
        }

        public Date getTestDate() {
            return this.testDate;
        }

        public void setTestDate(Date testDate) {
            this.testDate = testDate;
        }

        public String getTestEmail() {
            return this.testEmail;
        }

        public void setTestEmail(String testEmail) {
            this.testEmail = testEmail;
        }

        public char[] getPassword() {
            return this.password;
        }

        public void setPassword(char[] password) {
            this.password = password;
        }
    }
}

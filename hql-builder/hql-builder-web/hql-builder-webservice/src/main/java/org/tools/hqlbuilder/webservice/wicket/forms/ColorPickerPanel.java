package org.tools.hqlbuilder.webservice.wicket.forms;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.Spectrum;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.DefaultFormRowPanel;

/**
 * @see http://bgrins.github.io/spectrum
 */
public class ColorPickerPanel extends DefaultFormRowPanel<String, TextField<String>> {
    private static final long serialVersionUID = 3920158103962799959L;

    public ColorPickerPanel(final IModel<?> model, final String propertyPath, FormSettings formSettings, ColorPickerSettings colorPickerSettings) {
        super(model, propertyPath, formSettings, colorPickerSettings);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);
    }

    @Override
    public Class<String> getPropertyType() {
        return String.class;
    }

    @Override
    protected TextField<String> createComponent(IModel<String> model, Class<String> valueType) {
        return new TextField<String>(VALUE, model, valueType) {
            private static final long serialVersionUID = -989970628058227688L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        if (!isEnabledInHierarchy()) {
            return;
        }

        response.render(CssHeaderItem.forReference(Spectrum.SPECTRUM_CSS));
        response.render(JavaScriptHeaderItem.forReference(Spectrum.SPECTRUM_JS));
        response.render(OnDomReadyHeaderItem.forScript(createLoadScript()));
    }

    protected String createLoadScript() {
        ColorPickerSettings colorPickerSettings = ColorPickerSettings.class.cast(getComponentSettings());
        if (!(getValueModel() == null || StringUtils.isBlank(getValueModel().getObject()))) {
            colorPickerSettings.setColor(getValueModel().getObject());
        }
        return "$(\"#" + getComponent().getMarkupId() + "\").spectrum( " + getComponentSettings() + " );";
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }

    /**
     * palette: [<br>
     * ['black', 'white', 'blanchedalmond'],<br>
     * ['rgb(255, 128, 0);', 'hsv 100 70 50', 'lightyellow']<br>
     * ]<br>
     * containerClassName: 'x'<br>
     * replacerClassName: 'x'<br>
     */
    public static class ColorPickerSettings extends FormElementSettings {
        private static final long serialVersionUID = 6407342468858453586L;

        protected String color;

        protected ColorFormat preferredFormat;

        protected Boolean allowEmpty;

        protected Boolean showInput;

        protected Boolean showAlpha;

        protected Boolean disabled;

        protected Boolean showPalette;

        protected Boolean showPaletteOnly;

        protected Boolean showSelectionPalette;

        protected List<List<String>> palette;

        protected List<String> selectionPalette;

        protected Boolean clickoutFiresChange;

        protected Boolean showInitial;

        protected String chooseText;

        protected String cancelText;

        protected Boolean showButtons;

        protected Boolean flat;

        public ColorPickerSettings() {
            super();
        }

        public ColorPickerSettings(String color) {
            this.color = color;
        }

        public ColorFormat getPreferredFormat() {
            return this.preferredFormat;
        }

        public Boolean isAllowEmpty() {
            return this.allowEmpty;
        }

        public Boolean isShowInput() {
            return this.showInput;
        }

        public Boolean isShowAlpha() {
            return this.showAlpha;
        }

        public Boolean isDisabled() {
            return this.disabled;
        }

        public Boolean isShowPalette() {
            return this.showPalette;
        }

        public Boolean isShowPaletteOnly() {
            return this.showPaletteOnly;
        }

        public Boolean isShowSelectionPalette() {
            return this.showSelectionPalette;
        }

        public List<List<String>> getPalette() {
            return this.palette;
        }

        public Boolean isClickoutFiresChange() {
            return this.clickoutFiresChange;
        }

        public Boolean isShowInitial() {
            return this.showInitial;
        }

        public String getChooseText() {
            return this.chooseText;
        }

        public String getCancelText() {
            return this.cancelText;
        }

        public Boolean isShowButtons() {
            return this.showButtons;
        }

        public void setPreferredFormat(ColorFormat preferredFormat) {
            this.preferredFormat = preferredFormat;
        }

        public void setAllowEmpty(Boolean allowEmpty) {
            this.allowEmpty = allowEmpty;
        }

        public void setShowInput(Boolean showInput) {
            this.showInput = showInput;
        }

        public void setShowAlpha(Boolean showAlpha) {
            this.showAlpha = showAlpha;
        }

        public void setDisabled(Boolean disabled) {
            this.disabled = disabled;
        }

        public void setShowPalette(Boolean showPalette) {
            this.showPalette = showPalette;
        }

        public void setShowPaletteOnly(Boolean showPaletteOnly) {
            this.showPaletteOnly = showPaletteOnly;
        }

        public void setShowSelectionPalette(Boolean showSelectionPalette) {
            this.showSelectionPalette = showSelectionPalette;
        }

        public void setPalette(List<List<String>> palette) {
            this.palette = palette;
        }

        public void setClickoutFiresChange(Boolean clickoutFiresChange) {
            this.clickoutFiresChange = clickoutFiresChange;
        }

        public void setShowInitial(Boolean showInitial) {
            this.showInitial = showInitial;
        }

        public void setChooseText(String chooseText) {
            this.chooseText = chooseText;
        }

        public void setCancelText(String cancelText) {
            this.cancelText = cancelText;
        }

        public void setShowButtons(Boolean showButtons) {
            this.showButtons = showButtons;
        }

        public String getColor() {
            return this.color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public Boolean isFlat() {
            return this.flat;
        }

        public void setFlat(Boolean flat) {
            this.flat = flat;
        }

        public Boolean getAllowEmpty() {
            return this.allowEmpty;
        }

        public Boolean getShowInput() {
            return this.showInput;
        }

        public Boolean getShowAlpha() {
            return this.showAlpha;
        }

        public Boolean getDisabled() {
            return this.disabled;
        }

        public Boolean getShowPalette() {
            return this.showPalette;
        }

        public Boolean getShowPaletteOnly() {
            return this.showPaletteOnly;
        }

        public Boolean getShowSelectionPalette() {
            return this.showSelectionPalette;
        }

        public List<String> getSelectionPalette() {
            return this.selectionPalette;
        }

        public Boolean getClickoutFiresChange() {
            return this.clickoutFiresChange;
        }

        public Boolean getShowInitial() {
            return this.showInitial;
        }

        public Boolean getShowButtons() {
            return this.showButtons;
        }

        public Boolean getFlat() {
            return this.flat;
        }

        public void setSelectionPalette(List<String> selectionPalette) {
            this.selectionPalette = selectionPalette;
        }
    }

    public static enum ColorFormat {
        hex, hex3, hsl, rgb, name, none;
    }
}

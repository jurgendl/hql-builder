package org.tools.hqlbuilder.webservice.wicket.forms;

import java.util.List;

import org.apache.wicket.request.resource.ResourceReference;

import wicket.contrib.tinymce.settings.Button;
import wicket.contrib.tinymce.settings.Plugin;
import wicket.contrib.tinymce.settings.TinyMCESettings;
import wicket.contrib.tinymce.settings.TinyMCESettings.Align;
import wicket.contrib.tinymce.settings.TinyMCESettings.EntityEncoding;
import wicket.contrib.tinymce.settings.TinyMCESettings.Language;
import wicket.contrib.tinymce.settings.TinyMCESettings.Location;
import wicket.contrib.tinymce.settings.TinyMCESettings.Position;
import wicket.contrib.tinymce.settings.TinyMCESettings.Theme;
import wicket.contrib.tinymce.settings.TinyMCESettings.Toolbar;

public class TinyMCETextAreaSettings extends AbstractTextAreaSettings<TinyMCETextAreaSettings> {
    private static final long serialVersionUID = 2558455789913160626L;

    protected TinyMCESettings tinyMCESettings;

    public TinyMCETextAreaSettings() {
        super();
    }

    public TinyMCETextAreaSettings(Theme theme) {
        tinyMCESettings = new TinyMCESettings(theme);
    }

    public TinyMCESettings getTinyMCESettings() {
        if (tinyMCESettings == null) {
            tinyMCESettings = new TinyMCESettings();
        }
        return tinyMCESettings;
    }

    public TinyMCETextAreaSettings setTinyMCESettings(TinyMCESettings tinyMCESettings) {
        this.tinyMCESettings = tinyMCESettings;
        return this;
    }

    public Theme getTheme() {
        return getTinyMCESettings().getTheme();
    }

    public Language getLanguage() {
        return getTinyMCESettings().getLanguage();
    }

    public String getDocumentBaseUrl() {
        return getTinyMCESettings().getDocumentBaseUrl();
    }

    public TinyMCETextAreaSettings setDocumentBaseUrl(String documentBaseUrl) {
        getTinyMCESettings().setDocumentBaseUrl(documentBaseUrl);
        return this;
    }

    public TinyMCETextAreaSettings addCustomSetting(String customSetting) {
        getTinyMCESettings().addCustomSetting(customSetting);
        return this;
    }

    public String[] getCustomSettings() {
        return getTinyMCESettings().getCustomSettings();
    }

    public ResourceReference getContentCss() {
        return getTinyMCESettings().getContentCss();
    }

    public TinyMCETextAreaSettings setContentCss(ResourceReference contentCss) {
        getTinyMCESettings().setContentCss(contentCss);
        return this;
    }

    public String getBlockFormats() {
        return getTinyMCESettings().getBlockFormats();
    }

    public TinyMCETextAreaSettings setBlockFormats(String blockFormats) {
        getTinyMCESettings().setBlockFormats(blockFormats);
        return this;
    }

    public TinyMCETextAreaSettings setToolbarLocation(Location toolbarLocation) {
        getTinyMCESettings().setToolbarLocation(toolbarLocation);
        return this;
    }

    public Location getToolbarLocation() {
        return getTinyMCESettings().getToolbarLocation();
    }

    public TinyMCETextAreaSettings setStatusbarLocation(Location statusbarLocation) {
        getTinyMCESettings().setStatusbarLocation(statusbarLocation);
        return this;
    }

    public Location getStatusbarLocation() {
        return getTinyMCESettings().getStatusbarLocation();
    }

    public TinyMCETextAreaSettings setToolbarAlign(Align toolbarAlign) {
        getTinyMCESettings().setToolbarAlign(toolbarAlign);
        return this;
    }

    public Align getToolbarAlign() {
        return getTinyMCESettings().getToolbarAlign();
    }

    public TinyMCETextAreaSettings setEntityEncoding(EntityEncoding entityEncoding) {
        getTinyMCESettings().setEntityEncoding(entityEncoding);
        return this;
    }

    public EntityEncoding getEntityEncoding() {
        return getTinyMCESettings().getEntityEncoding();
    }

    public TinyMCETextAreaSettings setReadOnly(boolean readOnly) {
        getTinyMCESettings().setReadOnly(readOnly);
        return this;
    }

    public boolean isReadOnly() {
        return getTinyMCESettings().isReadOnly();
    }

    public TinyMCETextAreaSettings setResizing(boolean resizing) {
        getTinyMCESettings().setResizing(resizing);
        return this;
    }

    public boolean getResizing() {
        return getTinyMCESettings().getResizing();
    }

    public TinyMCETextAreaSettings setHorizontalResizing(boolean horizontalResizing) {
        getTinyMCESettings().setHorizontalResizing(horizontalResizing);
        return this;
    }

    public boolean getHorizontalResizing() {
        return getTinyMCESettings().getHorizontalResizing();
    }

    public boolean getResizingUseCookie() {
        return getTinyMCESettings().getResizingUseCookie();
    }

    public TinyMCETextAreaSettings setResizingUseCookie(boolean resizingUseCookie) {
        getTinyMCESettings().setResizingUseCookie(resizingUseCookie);
        return this;
    }

    public TinyMCETextAreaSettings setConvertUrls(boolean convertUrls) {
        getTinyMCESettings().setConvertUrls(convertUrls);
        return this;
    }

    public Boolean getConvertUrls() {
        return getTinyMCESettings().getConvertUrls();
    }

    public TinyMCETextAreaSettings setRemoveScriptHost(Boolean removeScriptHost) {
        getTinyMCESettings().setRemoveScriptHost(removeScriptHost);
        return this;
    }

    public Boolean getRemoveScriptHost() {
        return getTinyMCESettings().getRemoveScriptHost();
    }

    public TinyMCETextAreaSettings setRelativeUrls(Boolean relativeUrls) {
        getTinyMCESettings().setRelativeUrls(relativeUrls);
        return this;
    }

    public Boolean getRelativeUrls() {
        return getTinyMCESettings().getRelativeUrls();
    }

    public TinyMCETextAreaSettings add(Button button, Toolbar toolbar, Position position) {
        getTinyMCESettings().add(button, toolbar, position);
        return this;
    }

    public TinyMCETextAreaSettings disableButton(Button button) {
        getTinyMCESettings().disableButton(button);
        return this;
    }

    public TinyMCETextAreaSettings setToolbarButtons(Toolbar toolbar, List<Button> buttons) {
        getTinyMCESettings().setToolbarButtons(toolbar, buttons);
        return this;
    }

    public List<Button> getToolbarButtons(Toolbar toolbar) {
        return getTinyMCESettings().getToolbarButtons(toolbar);
    }

    public TinyMCETextAreaSettings register(Plugin plugin) {
        getTinyMCESettings().register(plugin);
        return this;
    }

    public String getLoadPluginJavaScript() {
        return getTinyMCESettings().getLoadPluginJavaScript();
    }

    public String getAdditionalPluginJavaScript() {
        return getTinyMCESettings().getAdditionalPluginJavaScript();
    }
}

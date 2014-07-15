package org.tools.hqlbuilder.webservice.wicket.forms;

public class FilePickerSettings extends FormElementSettings {
    private static final long serialVersionUID = -9053664906141038088L;

    /** accept */
    protected String mimeType;

    /** data-input */
    protected Boolean showInput;

    /** data-icon */
    protected Boolean showIcon;

    public String getMimeType() {
        return this.mimeType;
    }

    public FilePickerSettings setMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public Boolean getShowInput() {
        return this.showInput;
    }

    public Boolean getShowIcon() {
        return this.showIcon;
    }

    public FilePickerSettings setShowInput(Boolean showInput) {
        this.showInput = showInput;
        return this;
    }

    public FilePickerSettings setShowIcon(Boolean showIcon) {
        this.showIcon = showIcon;
        return this;
    }
}

package org.tools.hqlbuilder.webservice.wicket.forms;

import org.tools.hqlbuilder.webservice.jquery.ui.ckeditor.CKEditor.CKEType;

public class CKEditorTextAreaSettings extends FormElementSettings {
    private static final long serialVersionUID = 2746452642715136812L;

    private CKEType type;

    public CKEType getType() {
        return this.type;
    }

    public CKEditorTextAreaSettings setType(CKEType type) {
        this.type = type;
        return this;
    }
}

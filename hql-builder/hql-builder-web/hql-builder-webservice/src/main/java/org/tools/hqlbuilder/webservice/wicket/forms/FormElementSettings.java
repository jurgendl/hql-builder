package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

public class FormElementSettings implements Serializable {
    private static final long serialVersionUID = -2716372832273804363L;

    protected boolean required;

    public FormElementSettings() {
        super();
    }

    public FormElementSettings(boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return this.required;
    }

    public FormElementSettings setRequired(boolean required) {
        this.required = required;
        return this;
    }
}
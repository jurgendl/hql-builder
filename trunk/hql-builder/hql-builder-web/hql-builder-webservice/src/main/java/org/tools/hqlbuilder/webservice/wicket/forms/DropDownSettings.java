package org.tools.hqlbuilder.webservice.wicket.forms;

public class DropDownSettings extends FormElementSettings {
    private static final long serialVersionUID = 8867358507822882073L;

    protected boolean nullValid = false;

    public DropDownSettings() {
        super();
    }

    public DropDownSettings(DropDownSettings other) {
        super(other);
    }

    public DropDownSettings(boolean required) {
        super(required);
    }

    public boolean isNullValid() {
        return this.nullValid;
    }

    public DropDownSettings setNullValid(boolean nullValid) {
        this.nullValid = nullValid;
        return this;
    }
}

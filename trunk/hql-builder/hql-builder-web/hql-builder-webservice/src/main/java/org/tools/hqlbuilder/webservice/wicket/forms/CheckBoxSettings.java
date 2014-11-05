package org.tools.hqlbuilder.webservice.wicket.forms;

public class CheckBoxSettings extends AbstractFormElementSettings<CheckBoxSettings> {
    private static final long serialVersionUID = 8680530201586036946L;

    protected boolean nullValid = false;

    public CheckBoxSettings() {
        super();
    }

    public CheckBoxSettings(boolean required) {
        super(required);
    }

    public CheckBoxSettings(CheckBoxSettings other) {
        super(other);
    }

    public boolean isNullValid() {
        return this.nullValid;
    }

    public CheckBoxSettings setNullValid(boolean nullValid) {
        this.nullValid = nullValid;
        return castThis();
    }
}

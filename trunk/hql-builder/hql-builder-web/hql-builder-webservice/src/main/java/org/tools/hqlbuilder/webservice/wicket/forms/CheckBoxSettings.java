package org.tools.hqlbuilder.webservice.wicket.forms;

public class CheckBoxSettings extends AbstractFormElementSettings<CheckBoxSettings> {
    private static final long serialVersionUID = 8680530201586036946L;

    private boolean nice = true;

    public CheckBoxSettings() {
        super();
    }

    public CheckBoxSettings(boolean required) {
        super(required);
    }

    public CheckBoxSettings(CheckBoxSettings other) {
        super(other);
    }

    public boolean isNice() {
        return this.nice;
    }

    public CheckBoxSettings setNice(boolean nice) {
        this.nice = nice;
        return castThis();
    }
}

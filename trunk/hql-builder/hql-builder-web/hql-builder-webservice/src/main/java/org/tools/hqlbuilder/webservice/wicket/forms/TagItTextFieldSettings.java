package org.tools.hqlbuilder.webservice.wicket.forms;

public class TagItTextFieldSettings extends AbstractFormElementSettings<TagItTextFieldSettings> {
    private static final long serialVersionUID = 1098120531336315493L;

    protected boolean caseSensitive = false;

    protected char fieldDelimiter = ',';

    public TagItTextFieldSettings() {
        super();
    }

    public TagItTextFieldSettings(boolean required) {
        super(required);
    }

    public char getFieldDelimiter() {
        return this.fieldDelimiter;
    }

    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    public TagItTextFieldSettings setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }

    public TagItTextFieldSettings setFieldDelimiter(char fieldDelimiter) {
        this.fieldDelimiter = fieldDelimiter;
        return this;
    }
}

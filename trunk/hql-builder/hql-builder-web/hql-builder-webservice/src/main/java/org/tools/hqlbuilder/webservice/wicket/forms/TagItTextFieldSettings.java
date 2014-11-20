package org.tools.hqlbuilder.webservice.wicket.forms;

public class TagItTextFieldSettings extends AbstractFormElementSettings<TagItTextFieldSettings> {
    private static final long serialVersionUID = 1098120531336315493L;

    protected boolean caseSensitive = false;

    protected char fieldDelimiter = ',';

    protected int minLength = 1;

    protected long delay = 200;

    protected boolean singleField = true;

    protected boolean allowSpaces = true;

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

    public int getMinLength() {
        return this.minLength;
    }

    public TagItTextFieldSettings setMinLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    public long getDelay() {
        return this.delay;
    }

    public TagItTextFieldSettings setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public boolean isSingleField() {
        return this.singleField;
    }

    public TagItTextFieldSettings setSingleField(boolean singleField) {
        this.singleField = singleField;
        return this;
    }

    public boolean isAllowSpaces() {
        return this.allowSpaces;
    }

    public TagItTextFieldSettings setAllowSpaces(boolean allowSpaces) {
        this.allowSpaces = allowSpaces;
        return this;
    }
}

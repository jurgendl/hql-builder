package org.tools.hqlbuilder.webservice.wicket.forms;

public class TextAreaSettings extends FormElementSettings {
    private static final long serialVersionUID = 6279323396756540215L;

    private Integer rows;

    private Integer cols;

    public Integer getRows() {
        return this.rows;
    }

    public Integer getCols() {
        return this.cols;
    }

    public TextAreaSettings setRows(Integer rows) {
        this.rows = rows;
        return this;
    }

    public TextAreaSettings setCols(Integer cols) {
        this.cols = cols;
        return this;
    }
}

package org.tools.hqlbuilder.webservice.wicket.forms;

public class TextAreaSettings extends FormElementSettings {
    private static final long serialVersionUID = 6279323396756540215L;

    private Integer rows;

    private Integer cols;

    private boolean resizable = true;

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

    public boolean isResizable() {
        return this.resizable;
    }

    public TextAreaSettings setResizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }
}

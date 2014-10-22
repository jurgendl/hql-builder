package org.tools.hqlbuilder.webservice.wicket.tables;

import java.io.Serializable;

public class TableSettings implements Serializable {
    private static final long serialVersionUID = -2692137255417228253L;

    protected boolean add = true;

    protected boolean delete = true;

    protected boolean edit = true;

    protected boolean stateless = false;

    public boolean isAdd() {
        return this.add;
    }

    public TableSettings setAdd(boolean add) {
        this.add = add;
        return this;
    }

    public boolean isDelete() {
        return this.delete;
    }

    public TableSettings setDelete(boolean delete) {
        this.delete = delete;
        return this;
    }

    public boolean isEdit() {
        return this.edit;
    }

    public TableSettings setEdit(boolean edit) {
        this.edit = edit;
        return this;
    }

    public boolean isStateless() {
        return this.stateless;
    }

    public TableSettings setStateless(boolean stateless) {
        this.stateless = stateless;
        return this;
    }
}

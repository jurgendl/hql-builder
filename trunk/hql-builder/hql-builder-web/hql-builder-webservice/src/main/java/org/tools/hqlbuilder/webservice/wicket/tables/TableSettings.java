package org.tools.hqlbuilder.webservice.wicket.tables;

import java.io.Serializable;

public class TableSettings implements Serializable {
    private static final long serialVersionUID = -2692137255417228253L;

    private boolean add = true;

    private boolean delete = true;

    private boolean edit = true;

    public boolean isAdd() {
        return this.add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public boolean isDelete() {
        return this.delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isEdit() {
        return this.edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
}

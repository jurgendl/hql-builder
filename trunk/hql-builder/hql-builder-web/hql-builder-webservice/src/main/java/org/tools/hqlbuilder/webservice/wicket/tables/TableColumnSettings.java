package org.tools.hqlbuilder.webservice.wicket.tables;

import java.io.Serializable;

public class TableColumnSettings implements Serializable {
    private static final long serialVersionUID = -1547390416915008114L;

    protected Side filtering = Side.server;

    protected Side sorting = Side.server;

    public Side getFiltering() {
        return this.filtering;
    }

    public TableColumnSettings setFiltering(Side filtering) {
        this.filtering = filtering;
        return this;
    }

    public Side getSorting() {
        return this.sorting;
    }

    public TableColumnSettings setSorting(Side sorting) {
        this.sorting = sorting;
        return this;
    }
}

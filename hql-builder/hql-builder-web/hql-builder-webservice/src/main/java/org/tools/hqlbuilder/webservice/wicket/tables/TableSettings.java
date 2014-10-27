package org.tools.hqlbuilder.webservice.wicket.tables;

import java.io.Serializable;

public class TableSettings implements Serializable {
    private static final long serialVersionUID = -2692137255417228253L;

    protected boolean add = true;

    protected boolean delete = true;

    protected boolean edit = true;

    protected boolean stateless = false;

    protected int ajaxRefresh = 5;

    protected String ajaxRefreshUrl;

    protected String ajaxRefresMethod = "GET";

    public int getAjaxRefresh() {
        return this.ajaxRefresh;
    }

    public String getAjaxRefreshUrl() {
        return this.ajaxRefreshUrl;
    }

    public String getAjaxRefresMethod() {
        return this.ajaxRefresMethod;
    }

    public boolean isAdd() {
        return this.add;
    }

    public boolean isDelete() {
        return this.delete;
    }

    public boolean isEdit() {
        return this.edit;
    }

    public boolean isStateless() {
        return this.stateless;
    }

    public TableSettings setAdd(boolean add) {
        this.add = add;
        return this;
    }

    public TableSettings setAjaxRefresh(int ajaxRefresh) {
        this.ajaxRefresh = ajaxRefresh;
        return this;
    }

    public TableSettings setAjaxRefreshUrl(String ajaxRefreshUrl) {
        this.ajaxRefreshUrl = ajaxRefreshUrl;
        return this;
    }

    public TableSettings setAjaxRefresMethod(String ajaxRefresMethod) {
        this.ajaxRefresMethod = ajaxRefresMethod;
        return this;
    }

    public TableSettings setDelete(boolean delete) {
        this.delete = delete;
        return this;
    }

    public TableSettings setEdit(boolean edit) {
        this.edit = edit;
        return this;
    }

    public TableSettings setStateless(boolean stateless) {
        this.stateless = stateless;
        return this;
    }
}

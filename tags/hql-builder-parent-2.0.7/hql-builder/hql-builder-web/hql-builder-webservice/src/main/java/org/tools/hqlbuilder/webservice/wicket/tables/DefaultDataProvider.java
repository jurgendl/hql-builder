package org.tools.hqlbuilder.webservice.wicket.tables;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

@SuppressWarnings("serial")
public abstract class DefaultDataProvider<T extends Serializable> implements DataProvider<T>, ISortState<String> {
    protected Map<String, SortOrder> sort = new LinkedHashMap<String, SortOrder>();

    protected int rowsPerPage = 10;

    @Override
    public IModel<T> model(T object) {
        return Model.of(object);
    }

    @Override
    public void detach() {
        //
    }

    @Override
    public Iterator<? extends T> iterator(long first, long count) {
        return select(first, count, sort);
    }

    protected abstract Iterator<T> select(long first, long count, Map<String, SortOrder> sorting);

    @Override
    public ISortState<String> getSortState() {
        return this;
    }

    @Override
    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    @Override
    public void setPropertySortOrder(String property, SortOrder sortOrder) {
        sort.put(property, sortOrder);
    }

    @Override
    public SortOrder getPropertySortOrder(String property) {
        SortOrder sortOrder = sort.get(property);
        if (sortOrder == null) {
            sortOrder = SortOrder.NONE;
        }
        return sortOrder;
    }
}
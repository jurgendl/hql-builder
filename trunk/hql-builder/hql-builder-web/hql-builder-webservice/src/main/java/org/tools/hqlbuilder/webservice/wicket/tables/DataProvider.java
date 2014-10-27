package org.tools.hqlbuilder.webservice.wicket.tables;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.model.IModel;

public interface DataProvider<T extends Serializable> extends ISortableDataProvider<T, String> {
    /** service call to delete object & target.add( feedback ); target.add( table ); */
    public void delete(AjaxRequestTarget target, T object);

    public void edit(AjaxRequestTarget target, T object);

    public void add(AjaxRequestTarget target);

    public int getRowsPerPage();

    public String getId(IModel<T> model);

    public String getIdProperty();
}
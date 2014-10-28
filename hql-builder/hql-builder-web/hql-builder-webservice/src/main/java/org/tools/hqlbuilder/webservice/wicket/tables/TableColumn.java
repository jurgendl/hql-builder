package org.tools.hqlbuilder.webservice.wicket.tables;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.IExportableColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class TableColumn<T> implements IStyledColumn<T, String>, IExportableColumn<T, String, Object> {
    private static final long serialVersionUID = -7609015288627705664L;

    public static final String UNSORTABLE = "unsortable";

    protected IModel<String> displayModel;

    protected Side filtering = Side.none;

    protected Side sorting = Side.none;

    protected String propertyExpression;

    protected boolean dataTag = false;

    public TableColumn() {
        super();
    }

    public TableColumn(IModel<String> displayModel, String propertyExpression) {
        this();
        setDisplayModel(displayModel);
        setPropertyExpression(propertyExpression);
    }

    public Side getFiltering() {
        return filtering;
    }

    public TableColumn<T> setFiltering(Side filtering) {
        this.filtering = filtering;
        return this;
    }

    public Side getSorting() {
        return sorting;
    }

    public TableColumn<T> setSorting(Side sorting) {
        this.sorting = sorting;
        return this;
    }

    public void setDisplayModel(IModel<String> displayModel) {
        this.displayModel = displayModel;
    }

    public void setPropertyExpression(String propertyExpression) {
        this.propertyExpression = propertyExpression;
    }

    /**
     * @see org.apache.wicket.extensions.markup.html.repeater.data.table.export.IExportableColumn#getDisplayModel()
     */
    @Override
    public IModel<String> getDisplayModel() {
        return displayModel;
    }

    /**
     * @see org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn#getSortProperty()
     */
    @Override
    public String getSortProperty() {
        return sorting == Side.server ? propertyExpression : null;
    }

    /**
     * @see org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn#isSortable()
     */
    @Override
    public boolean isSortable() {
        return getSortProperty() != null;
    }

    /**
     * @see org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn#getHeader(java.lang.String)
     */
    @Override
    public Component getHeader(final String componentId) {
        Label label = new Label(componentId, getDisplayModel());
        return label;
    }

    /**
     * @see org.apache.wicket.model.IDetachable#detach()
     */
    @Override
    public void detach() {
        if (displayModel != null) {
            displayModel.detach();
        }
    }

    /**
     * @see org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn#getCssClass()
     */
    @Override
    public String getCssClass() {
        return sorting == Side.none ? UNSORTABLE : null;
    }

    /**
     * Implementation of populateItem which adds a label to the cell whose model is the provided property expression evaluated against rowModelObject
     *
     * @see ICellPopulator#populateItem(Item, String, IModel)
     */
    @Override
    public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
        IModel<Object> dataModel = getDataModel(rowModel);
        Label label = new Label(componentId, dataModel);
        item.add(label);
        if (dataTag) {
            item.add(new AttributeModifier("data", String.valueOf(dataModel.getObject())));
        }
    }

    /**
     * @return wicket property expression
     */
    public String getPropertyExpression() {
        return propertyExpression;
    }

    /**
     * Factory method for generating a model that will generated the displayed value. Typically the model is a property model using the
     * {@link #propertyExpression} specified in the constructor.
     */
    @Override
    public IModel<Object> getDataModel(IModel<T> rowModel) {
        PropertyModel<Object> propertyModel = new PropertyModel<Object>(rowModel, propertyExpression);
        return propertyModel;
    }

    public boolean isDataTag() {
        return this.dataTag;
    }

    public TableColumn<T> setDataTag(boolean dataTag) {
        this.dataTag = dataTag;
        return this;
    }
}

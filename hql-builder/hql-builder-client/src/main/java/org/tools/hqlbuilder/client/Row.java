package org.tools.hqlbuilder.client;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Jurgen
 */
public class Row implements Comparable<Row> {
    public Row(String path) {
        this.path = path;
    }

    Boolean collection = false;

    Class<?> type;

    Row parentRef = null;

    String alias = null;

    String path;

    Boolean innerjoin = false;

    Boolean select = true;

    public Boolean isCollection() {
        return this.collection;
    }

    public Boolean getCollection() {
        return isCollection();
    }

    public void setCollection(Boolean collection) {
        this.collection = collection;
    }

    public String getParent() {
        return this.parentRef == null ? null : this.parentRef.alias;
    }

    public void setParent(String parent) {
        if (this.parentRef != null) {
            this.parentRef.alias = parent;
        }
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getInnerjoin() {
        return this.innerjoin;
    }

    public void setInnerjoin(Boolean innerjoin) {
        this.innerjoin = innerjoin;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Row)) {
            return false;
        }
        Row castOther = (Row) other;
        return new EqualsBuilder().append(type, castOther.type).append(parentRef, castOther.parentRef).append(path, castOther.path).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(type).append(parentRef).append(path).toHashCode();
    }

    @Override
    public int compareTo(final Row other) {
        return new CompareToBuilder().append(parentRef, other.parentRef).append(path, other.path).toComparison();
    }

    @Override
    public String toString() {
        String simpleName = getSimpleClassName();
        if (simpleName.equals(path)) {
            return path;
        }
        return path + " (" + simpleName + ")";
    }

    public String getSimpleClassName() {
        return type.getSimpleName();
    }

    public void setSimpleClassName(@SuppressWarnings("unused") String tmp) {
        //
    }

    public String getLabel() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString()).append("collection", collection)
                .append("clazz", type).append("parent", getParent()).append("path", path).append("innerjoin", innerjoin).toString();
    }

    public Row getParentRef() {
        return this.parentRef;
    }

    public void setParentRef(Row parentRef) {
        this.parentRef = parentRef;
    }

    public Class<?> getType() {
        return this.type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getAlias() {
        return this.alias;
    }

    public Boolean getSelect() {
        return this.select;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }
}
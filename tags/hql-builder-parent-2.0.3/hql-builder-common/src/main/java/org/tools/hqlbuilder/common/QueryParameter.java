package org.tools.hqlbuilder.common;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

@XmlRootElement(name = "parameter")
public class QueryParameter implements Serializable, Comparable<QueryParameter> {
    private static final long serialVersionUID = 2308140856360992628L;

    private transient Object value;

    @XmlAttribute
    private String name;

    @XmlAttribute(name = "value", required = true)
    private String text;

    private transient String toString;

    public QueryParameter() {
        super();
    }

    public QueryParameter(String text, String name, Object value) {
        this.text = text;
        this.name = name;
        this.value = value;
        afterInit();
    }

    /**
     * afterInit
     */
    public void afterInit() {
        StringBuilder sb = new StringBuilder();

        if (name != null) {
            sb.append(name).append("=");
        }

        if (value == null) {
            sb.append("null");
        } else {
            String cb = value.getClass().getName().replaceAll("java.lang.", "");
            sb.append(cb).append(" ").append(value.toString());
        }

        toString = sb.toString();
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return StringUtils.isBlank(toString) ? "?" : toString;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
        afterInit();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        afterInit();
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
        afterInit();
    }

    public String getToString() {
        return this.toString;
    }

    public void setToString(String toString) {
        this.toString = toString;
    }

    /**
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(QueryParameter o) {
        return 0;
    }

    public void clear() {
        setValue(null);
    }

    @Deprecated
    final public String getType() {
        return null;
    }

    @Deprecated
    final public void setType(@SuppressWarnings("unused") String type) {
        //
    }
}

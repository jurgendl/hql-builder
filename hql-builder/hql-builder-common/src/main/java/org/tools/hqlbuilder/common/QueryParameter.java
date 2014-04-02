package org.tools.hqlbuilder.common;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;

@XmlRootElement(name = "parameter")
public class QueryParameter implements Serializable, Comparable<QueryParameter> {
    private static final long serialVersionUID = 2308140856360992628L;

    private transient Object value;

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String valueText;

    @XmlAttribute
    private Integer index;

    private transient String toString;

    public QueryParameter() {
        super();
    }

    public QueryParameter(String name) {
        this(null, name, null, null);
    }

    public QueryParameter(Integer index) {
        this(index, null, null, null);
    }

    public QueryParameter(String name, String valueText) {
        this(null, name, valueText, null);
    }

    public QueryParameter(Integer index, String valueText) {
        this(index, null, valueText, null);
    }

    public QueryParameter(String name, String valueText, Object value) {
        this(null, name, valueText, value);
    }

    public QueryParameter(Integer index, String valueText, Object value) {
        this(index, null, valueText, value);
    }

    private QueryParameter(Integer index, String name, String valueText, Object value) {
        this.index = index;
        this.valueText = valueText;
        this.name = name;
        this.value = value;
        afterInit();
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
        if (valueText == null) {
            valueText = String.valueOf(value);
        }
        afterInit();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        afterInit();
    }

    public String getToString() {
        return this.toString;
    }

    public void setToString(String toString) {
        this.toString = toString;
    }

    public void clear() {
        setValue(null);
    }

    public Integer getIndex() {
        return this.index;
    }

    public void setIndex(Integer index) {
        this.index = index;
        afterInit();
    }

    public String getValueText() {
        return this.valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
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
        if (index != null) {
            sb.append(index).append("=");
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
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return StringUtils.isBlank(toString) ? "?" : toString;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.index == null) ? 0 : this.index.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.valueText == null) ? 0 : this.valueText.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        QueryParameter other = (QueryParameter) obj;
        if (this.index == null) {
            if (other.index != null) {
                return false;
            }
        } else if (!this.index.equals(other.index)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.valueText == null) {
            if (other.valueText != null) {
                return false;
            }
        } else if (!this.valueText.equals(other.valueText)) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(QueryParameter o) {
        return new CompareToBuilder().append(index, o.index).append(name, o.name).toComparison();
    }
}

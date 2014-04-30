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
    private String type;

    @XmlAttribute
    private Integer index;

    private transient String toString;

    public QueryParameter() {
        super();
    }

    public QueryParameter(String name) {
        this(null, name, null, null, null);
    }

    public QueryParameter(Integer index) {
        this(index, null, null, null, null);
    }

    public QueryParameter(String name, String valueText) {
        this(null, name, valueText, null, null);
    }

    public QueryParameter(Integer index, String valueText) {
        this(index, null, valueText, null, null);
    }

    public QueryParameter(String name, String valueText, String type) {
        this(null, name, valueText, type, null);
    }

    public QueryParameter(Integer index, String valueText, String type) {
        this(index, null, valueText, type, null);
    }

    public QueryParameter(String name, String valueText, Object value) {
        this(null, name, valueText, value.getClass().getName(), value);
    }

    public QueryParameter(String name, Object value) {
        this(null, name, String.valueOf(value), value.getClass().getName(), value);
    }

    public QueryParameter(Integer index, String valueText, Object value) {
        this(index, null, valueText, value.getClass().getName(), value);
    }

    public QueryParameter(Integer index, Object value) {
        this(index, null, String.valueOf(value), value.getClass().getName(), value);
    }

    private QueryParameter(Integer index, String name, String valueText, String type, Object value) {
        this.index = index;
        this.valueText = valueText;
        this.name = name;
        this.value = value;
        this.type = type;
        afterInit();
    }

    public Object getValue() {
        return this.value;
    }

    public QueryParameter setValue(Object value) {
        this.value = value;
        if (valueText == null) {
            valueText = String.valueOf(value);
        }
        return afterInit();
    }

    public String getName() {
        return this.name;
    }

    public QueryParameter setName(String name) {
        this.name = name;
        return afterInit();
    }

    public String getToString() {
        return this.toString;
    }

    public QueryParameter setToString(String toString) {
        this.toString = toString;
        return this;
    }

    public QueryParameter clear() {
        setValue(null);
        return this;
    }

    public Integer getIndex() {
        return this.index;
    }

    public QueryParameter setIndex(Integer index) {
        this.index = index;
        return afterInit();
    }

    public String getValueText() {
        return this.valueText;
    }

    public QueryParameter setValueText(String valueText) {
        this.valueText = valueText;
        return afterInit();
    }

    public String getType() {
        return this.type;
    }

    public QueryParameter setType(String type) {
        this.type = type;
        return afterInit();
    }

    /**
     * afterInit
     */
    public QueryParameter afterInit() {
        StringBuilder sb = new StringBuilder();
        if (index != null && index != -1) {
            sb.append(index).append(":");
        }
        if (name != null) {
            sb.append(name).append("=");
        }
        if (type != null) {
            sb.append(type).append(" ");
        } else if (value != null) {
            sb.append(value.getClass().getSimpleName()).append(" ");
        }
        sb.append(value);
        if (sb.length() == 0 && StringUtils.isNotBlank(valueText)) {
            sb.append(valueText);
        }
        toString = sb.toString();
        return this;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return StringUtils.isBlank(toString) ? "?" : toString;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.index == null) ? 0 : this.index.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        result = prime * result + ((this.valueText == null) ? 0 : this.valueText.hashCode());
        return result;
    }

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
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
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

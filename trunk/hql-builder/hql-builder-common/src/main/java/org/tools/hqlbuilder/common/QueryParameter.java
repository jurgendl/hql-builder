package org.tools.hqlbuilder.common;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

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

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (toString == null) {
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
        }
        return toString;
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

    public Object getValue() {
        return this.value;
    }

    public QueryParameter setValue(Object value) {
        toString = null;
        this.value = value;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public QueryParameter setName(String name) {
        toString = null;
        this.name = name;
        return this;
    }

    public String getValueText() {
        return this.valueText;
    }

    public QueryParameter setValueText(String valueText) {
        toString = null;
        this.valueText = valueText;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public QueryParameter setType(String type) {
        toString = null;
        this.type = type;
        return this;
    }

    public Integer getIndex() {
        return this.index;
    }

    public QueryParameter setIndex(Integer index) {
        toString = null;
        this.index = index;
        return this;
    }

    public QueryParameter setValueTypeText(Object value) {
        setValue(value);
        setValueText(value == null ? "null" : String.valueOf(value));
        setType(value == null ? "java.lang.Object" : value.getClass().getName());
        return this;
    }
}

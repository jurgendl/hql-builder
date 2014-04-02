package org.tools.hqlbuilder.test;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.AccessType;

@XmlRootElement
@Entity
@AccessType("field")
@NamedQueries({ @NamedQuery(name = "PojoNQ", query = "from Pojo") })
public class Pojo extends PojoSuper implements Serializable {
    private static final long serialVersionUID = -589586518891599759L;

    private String value;

    @XmlElement
    @Embedded
    private EmbedPojo embedded = new EmbedPojo();

    private Integer from0To100;

    private String regexDigits;

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Pojo [id=" + getId() + ", version=" + getVersion() + ", value=" + getValue() + ", from0To100=" + getFrom0To100() + ", regexDigits="
                + getRegexDigits() + ", embedded=" + getEmbedded() + "]";
    }

    @Override
    public boolean equals(final Object other) {
        if (getId() == null) {
            return false;
        }
        if (!(other instanceof Pojo)) {
            return false;
        }
        Pojo castOther = (Pojo) other;
        return new EqualsBuilder().append(getId(), castOther.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public Integer getFrom0To100() {
        return from0To100;
    }

    public void setFrom0To100(Integer from0To100) {
        this.from0To100 = from0To100;
    }

    public String getRegexDigits() {
        return regexDigits;
    }

    public void setRegexDigits(String regexDigits) {
        this.regexDigits = regexDigits;
    }

    public EmbedPojo getEmbedded() {
        return embedded;
    }

    public void setEmbedded(EmbedPojo embedded) {
        this.embedded = embedded;
    }
}

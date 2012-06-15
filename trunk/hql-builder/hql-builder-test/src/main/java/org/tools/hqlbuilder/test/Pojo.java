package org.tools.hqlbuilder.test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.AccessType;
import org.hibernate.validator.Max;
import org.hibernate.validator.Min;

@Entity
@AccessType("field")
@SuppressWarnings("unused")
public class Pojo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Integer version;

    private String value;
    
    @Min(0)
    @Max(100)
    private Integer from0To100;

    private Long getId() {
        return this.id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    private Integer getVersion() {
        return this.version;
    }

    private void setVersion(Integer version) {
        this.version = version;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
	public String toString() {
		return "Pojo [id=" + id + ", version=" + version + ", value=" + value
				+ ", from0To100=" + from0To100 + "]";
	}

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Pojo)) {
            return false;
        }
        Pojo castOther = (Pojo) other;
        return new EqualsBuilder().append(id, castOther.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }
}

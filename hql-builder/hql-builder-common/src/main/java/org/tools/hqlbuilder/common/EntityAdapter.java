package org.tools.hqlbuilder.common;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@MappedSuperclass
public abstract class EntityAdapter implements EntityI {
    private static final long serialVersionUID = -2958424236876731630L;

    @Id
	// @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Version
    private Integer version;

    public EntityAdapter() {
        super();
    }

    public EntityAdapter(Long id) {
        this.id = id;
    }

    public EntityAdapter(Long id, Integer version) {
        this.id = id;
        this.version = version;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof EntityAdapter)) {
            return false;
        }
        EntityAdapter castOther = (EntityAdapter) other;
        if ((this.id != null) && (castOther.id != null)) {
            // FIXME class/proxy==
            return new EqualsBuilder().append(this.id, castOther.id).append(this.getClass(), castOther.getClass()).isEquals();
        }
        return super.equals(other);
    }

    public Long getId() {
        return this.id;
    }

    public Integer getVersion() {
        return this.version;
    }

    @Override
    public int hashCode() {
        if (this.id != null) {
            return new HashCodeBuilder().append(this.id).toHashCode();
        }
        return super.hashCode();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", this.id).append("version", this.version).toString();
    }
}

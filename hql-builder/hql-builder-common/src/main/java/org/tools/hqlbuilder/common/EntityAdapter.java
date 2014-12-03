package org.tools.hqlbuilder.common;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@MappedSuperclass
public abstract class EntityAdapter implements EntityI {
    private static final long serialVersionUID = -2958424236876731630L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Integer version;

    public EntityAdapter(Long id, Integer version) {
        this.id = id;
        this.version = version;
    }

    public EntityAdapter(Long id) {
        this.id = id;
    }

    public EntityAdapter() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof EntityAdapter)) {
            return false;
        }
        EntityAdapter castOther = (EntityAdapter) other;
        if (id != null && castOther.id != null) {
            return new EqualsBuilder().append(id, castOther.id).isEquals();
        }
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return new HashCodeBuilder().append(id).toHashCode();
        }
        return super.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("version", version).toString();
    }
}

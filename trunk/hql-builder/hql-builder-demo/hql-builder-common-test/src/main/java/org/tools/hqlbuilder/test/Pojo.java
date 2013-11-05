package org.tools.hqlbuilder.test;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

@Entity
@AccessType("field")
public class Pojo extends PojoSuper implements Serializable {
    private static final long serialVersionUID = -589586518891599759L;

    private String value;

    @Embedded
    @NotNull
    @Valid
    private EmbedPojo embedded = new EmbedPojo();

    @Min(0)
    @Max(100)
    private Integer from0To100;

    @Pattern(regexp = "\\d*")
    private String regexDigits;

    @ElementCollection(fetch = FetchType.LAZY)
    @JoinTable(name = "plainSet", joinColumns = { @JoinColumn(name = "plainSetId") })
    @Column(name = "plainSet", nullable = false)
    @Sort(type = SortType.NATURAL)
    private SortedSet<String> plainSet = new TreeSet<String>();

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

    public SortedSet<String> getPlainSet() {
        return plainSet;
    }

    public void setPlainSet(SortedSet<String> plainSet) {
        this.plainSet = plainSet;
    }

    public EmbedPojo getEmbedded() {
        return embedded;
    }

    public void setEmbedded(EmbedPojo embedded) {
        this.embedded = embedded;
    }
}

package org.tools.hqlbuilder.client;

import java.io.Serializable;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.tools.hqlbuilder.common.CommonUtils;
import org.tools.hqlbuilder.common.QueryParameter;

/**
 * @author Jurgen
 */
@XmlRootElement(name = "favorite")
public class QueryFavorite implements Serializable {
    private static final long serialVersionUID = -5587946688272447640L;

    private transient String toString;

    private String full;

    @XmlAttribute(required = true)
    private String name;

    @XmlElementWrapper
    @XmlElement(name = "parameter")
    private QueryParameter[] parameters;

    private transient String hqlPreview = null;

    private transient String parametersPreview = null;

    public QueryFavorite() {
        super();
    }

    public QueryFavorite(String name, String full, QueryParameter[] parameters) {
        setFull(full);
        setParameters(parameters);
        setName(name);
    }

    /**
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return toString;
    }

    public String getFull() {
        return this.full;
    }

    public void setFull(String full) {
        this.full = full;

        String[] parts = full.split("\n");
        this.toString = parts[0] + ((parts.length > 1) ? "..." : "");
    }

    public QueryParameter[] getParameters() {
        return this.parameters;
    }

    public void setParameters(QueryParameter[] parameters) {
        this.parameters = parameters;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof QueryFavorite)) {
            return false;
        }
        QueryFavorite castOther = (QueryFavorite) other;
        return new EqualsBuilder().append(name, castOther.name).isEquals();
    }

    /**
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).toHashCode();
    }

    public String getHqlPreview() {
        if (hqlPreview == null) {
            hqlPreview = CommonUtils.removeUnnecessaryWhiteSpaces(full);
        }
        return this.hqlPreview;
    }

    public void setHqlPreview(String hqlPreview) {
        //
    }

    public String getParametersPreview() {
        if (parametersPreview == null) {
            parametersPreview = Arrays.toString(parameters);
        }
        return this.parametersPreview;
    }

    public void setParametersPreview(String parametersPreview) {
        //
    }
}

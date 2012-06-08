package be.ugent.oasis.tools.hqlbuilder;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author jdlandsh
 */
public class QueryFavorite implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = -5587946688272447640L;

    /** info */
    private transient String toString;

    /** info */
    private String full;

    /** info */
    private String name;

    /** info */
    private QueryParameter[] parameters;

    private String hqlPreview = null;

    private String parametersPreview = null;

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
            hqlPreview = full.replaceAll("\\s", " ").replaceAll("  ", " ").replaceAll("  ", " ");
        }
        return this.hqlPreview;
    }

    public void setHqlPreview(@SuppressWarnings("unused") String hqlPreview) {
        //
    }

    public String getParametersPreview() {
        if (parametersPreview == null) {
            parametersPreview = Arrays.toString(parameters);
        }
        return this.parametersPreview;
    }

    public void setParametersPreview(@SuppressWarnings("unused") String parametersPreview) {
        //
    }
}

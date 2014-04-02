package org.tools.hqlbuilder.common;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "qparams")
public class QueryParameters implements Serializable {
    private static final long serialVersionUID = -66910825170297665L;

    @XmlAttribute(required = true)
    private String hql;

    @XmlAttribute(required = true)
    private int max = -1;

    @XmlElementWrapper
    private List<QueryParameter> parameters;

    public QueryParameters(String hql, int max, List<QueryParameter> parameters) {
        this.hql = hql;
        this.max = max;
        this.parameters = parameters;
    }

    public QueryParameters(String hql, int max, QueryParameter... parameters) {
        this.hql = hql;
        this.max = max;
        this.parameters = Arrays.asList(parameters);
    }

    public QueryParameters(String hql, List<QueryParameter> parameters) {
        this.hql = hql;
        this.parameters = parameters;
    }

    public QueryParameters(String hql, QueryParameter... parameters) {
        this.hql = hql;
        this.parameters = Arrays.asList(parameters);
    }

    public QueryParameters() {
        super();
    }

    public List<QueryParameter> getParameters() {
        return this.parameters;
    }

    public void setParameters(List<QueryParameter> parameters) {
        this.parameters = parameters;
    }

    public String getHql() {
        return this.hql;
    }

    public int getMax() {
        return this.max;
    }

    public void setHql(String hql) {
        this.hql = hql;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "QueryParameters [hql=" + this.hql + ", max=" + this.max + ", parameters=" + this.parameters + "]";
    }
}

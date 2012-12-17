package org.tools.hqlbuilder.common;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExecutionResult implements Serializable {
    private static final long serialVersionUID = 7646264311502956246L;

    private List<Object> results;

    private Map<String, String> fromAliases;

    private String sql;

    private String[] queryReturnAliases;

    private String[][] scalarColumnNames;

    private String[] sqlAliases;

    private String[] queryReturnTypeNames;

    private int size;

    private long duration;

    public ExecutionResult() {
        super();
    }

    public ExecutionResult(String sql, Map<String, String> fromAliases, int size, List<Object> results, String[] queryReturnAliases,
            String[][] scalarColumnNames, String[] sqlAliases, String[] queryReturnTypeNames) {
        this.sql = sql;
        this.fromAliases = fromAliases;
        this.results = results;
        this.queryReturnAliases = queryReturnAliases;
        this.scalarColumnNames = scalarColumnNames;
        this.sqlAliases = sqlAliases;
        this.queryReturnTypeNames = queryReturnTypeNames;
        this.size = size;
    }

    public List<Object> getResults() {
        return this.results;
    }

    public void setResults(List<Object> results) {
        this.results = results;
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String[] getQueryReturnAliases() {
        return this.queryReturnAliases;
    }

    public void setQueryReturnAliases(String[] queryReturnAliases) {
        this.queryReturnAliases = queryReturnAliases;
    }

    public String[][] getScalarColumnNames() {
        return this.scalarColumnNames;
    }

    public void setScalarColumnNames(String[][] scalarColumnNames) {
        this.scalarColumnNames = scalarColumnNames;
    }

    public Map<String, String> getFromAliases() {
        return this.fromAliases;
    }

    public void setFromAliases(Map<String, String> fromAliases) {
        this.fromAliases = fromAliases;
    }

    public String[] getSqlAliases() {
        return this.sqlAliases;
    }

    public void setSqlAliases(String[] sqlAliases) {
        this.sqlAliases = sqlAliases;
    }

    public String[] getQueryReturnTypeNames() {
        return this.queryReturnTypeNames;
    }

    public void setQueryReturnTypeNames(String[] queryReturnTypeNames) {
        this.queryReturnTypeNames = queryReturnTypeNames;
    }

    @Override
    public String toString() {
        return "ExecutionResult [sql=" + this.sql + ", fromAliases=" + this.fromAliases + ", queryReturnAliases="
                + Arrays.toString(this.queryReturnAliases) + ", scalarColumnNames=" + Arrays.toString(this.scalarColumnNames) + ", sqlAliases="
                + Arrays.toString(this.sqlAliases) + ", queryReturnTypeNames=" + Arrays.toString(this.queryReturnTypeNames) + ", results="
                + this.results + "]";
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}

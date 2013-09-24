package org.tools.hqlbuilder.common.exceptions;

import org.tools.hqlbuilder.common.ExecutionResult;

public class SqlException extends ServiceException {
    private static final long serialVersionUID = -3973877301898867276L;

    private String sql;

    private String exception;

    private String state;

    public SqlException(String sql, String exception, String state) {
        this.sql = sql;
        this.state = state;
        this.exception = exception;
    }

    public SqlException(String message, ExecutionResult partialResult, String sql, String exception, String state) {
        super(message, partialResult);
        this.sql = sql;
        this.state = state;
        this.exception = exception;
    }

    public SqlException(String message, String sql, String exception, String state) {
        super(message);
        this.sql = sql;
        this.state = state;
        this.exception = exception;
    }

    public String getSql() {
        return this.sql;
    }

    public String getException() {
        return this.exception;
    }

    public String getState() {
        return this.state;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setState(String state) {
        this.state = state;
    }
}

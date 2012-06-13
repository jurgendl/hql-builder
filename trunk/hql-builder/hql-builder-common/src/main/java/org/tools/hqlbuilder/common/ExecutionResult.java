package org.tools.hqlbuilder.common;

import java.util.List;
import java.util.Map;

public class ExecutionResult {
    private List<Object> results;

    private Map<String, String> from_aliases;

    public ExecutionResult() {
        super();
    }

    public ExecutionResult(Map<String, String> from_aliases, List<Object> results) {
        super();
        this.from_aliases = from_aliases;
        this.results = results;
    }

    public List<Object> getResults() {
        return this.results;
    }

    public Map<String, String> getFrom_aliases() {
        return this.from_aliases;
    }

    public void setResults(List<Object> results) {
        this.results = results;
    }

    public void setFrom_aliases(Map<String, String> from_aliases) {
        this.from_aliases = from_aliases;
    }

    @Override
    public String toString() {
        return "ExecutionResult [results=" + this.results + ", from_aliases=" + this.from_aliases + "]";
    }
}

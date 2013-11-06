package org.tools.hqlbuilder.common.exceptions;

import org.tools.hqlbuilder.common.ExecutionResult;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -968911838047310504L;

    private ExecutionResult partialResult;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, ExecutionResult partialResult) {
        super(message);
        setPartialResult(partialResult);
    }

    public ExecutionResult getPartialResult() {
        return this.partialResult;
    }

    public void setPartialResult(ExecutionResult partialResult) {
        this.partialResult = partialResult;
    }
}

package org.tools.hqlbuilder.common.exceptions;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -968911838047310504L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }
}

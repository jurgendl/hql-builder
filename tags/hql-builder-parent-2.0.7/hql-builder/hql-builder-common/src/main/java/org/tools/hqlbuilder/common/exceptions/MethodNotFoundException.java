package org.tools.hqlbuilder.common.exceptions;

public class MethodNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 5813185363326141162L;

    public MethodNotFoundException() {
        super();
    }

    public MethodNotFoundException(String message) {
        super(message);
    }

    public MethodNotFoundException(Throwable cause) {
        super(cause);
    }

    public MethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

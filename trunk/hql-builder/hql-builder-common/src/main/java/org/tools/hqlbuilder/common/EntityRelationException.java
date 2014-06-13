package org.tools.hqlbuilder.common;

public class EntityRelationException extends RuntimeException {
    private static final long serialVersionUID = -6660143470859271041L;

    public EntityRelationException() {
        super();
    }

    public EntityRelationException(String message) {
        super(message);
    }

    public EntityRelationException(Throwable cause) {
        super(cause);
    }

    public EntityRelationException(String message, Throwable cause) {
        super(message, cause);
    }
}

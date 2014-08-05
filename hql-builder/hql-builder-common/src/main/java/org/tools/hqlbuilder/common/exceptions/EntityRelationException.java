package org.tools.hqlbuilder.common.exceptions;

public class EntityRelationException extends RuntimeException {
    private static final long serialVersionUID = -6660143470859271041L;

    public static enum EntityRelationExceptionType {
        GENERAL, MISSING_REFERENCE, EXISTING_REFERENCE, REFERENCE;
    }

    private EntityRelationExceptionType type = EntityRelationExceptionType.GENERAL;

    public EntityRelationException(EntityRelationExceptionType type, Throwable cause) {
        super("" + type, cause);
        this.type = type;
    }

    public EntityRelationException(EntityRelationExceptionType type, String message) {
        super(type + ": " + message);
        this.type = type;
    }

    public EntityRelationException(Throwable cause) {
        this(EntityRelationExceptionType.GENERAL, cause);
    }

    public EntityRelationException(String message) {
        this(EntityRelationExceptionType.GENERAL, message);
    }

    public EntityRelationException(Object expectedParent, Object child) {
        this(EntityRelationExceptionType.MISSING_REFERENCE, "child in collection but parent is null: child=" + child + ", expectedParent="
                + expectedParent);
    }

    public EntityRelationException(Object currentParent, Object expectedParent, Object child) {
        this(EntityRelationExceptionType.EXISTING_REFERENCE, "child niet in collectie en parent verwijst naar derde: child=" + child
                + ", expectedParent=" + expectedParent + ", currentParent=" + currentParent);
    }

    public EntityRelationExceptionType getType() {
        return this.type;
    }
}

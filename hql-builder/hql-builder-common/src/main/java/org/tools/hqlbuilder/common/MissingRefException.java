package org.tools.hqlbuilder.common;

public class MissingRefException extends RefException {
    private static final long serialVersionUID = 7872228849835935117L;

    private final Object child;

    private final Object expectedParent;

    public MissingRefException(Object expectedParent, Object child) {
        super("child in collection but parent is null");
        this.expectedParent = expectedParent;
        this.child = child;
    }

    public Object getChild() {
        return this.child;
    }

    public Object getExpectedParent() {
        return this.expectedParent;
    }
}

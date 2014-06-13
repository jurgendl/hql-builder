package org.tools.hqlbuilder.common;

/**
 * IncorrecteReferentie
 * 
 * @author jdlandsh
 */
public class ExistingParentChildRefException extends RefException {
    /** serialVersionUID */
    private static final long serialVersionUID = -385595087890396808L;

    /** child */
    private final Object child;

    /** currentParent */
    private final Object currentParent;

    /** expectedParent */
    private final Object expectedParent;

    /**
     * Creates a new BestaandeParentChildReferentie object.
     * 
     * @param msg
     * @param currentParent
     * @param expectedParent
     * @param child
     */
    public ExistingParentChildRefException(Object currentParent, Object expectedParent, Object child) {
        super("child niet in collectie en parent verwijst naar derde");

        this.currentParent = currentParent;
        this.expectedParent = expectedParent;
        this.child = child;
    }

    /**
     * Getter voor child
     * 
     * @return Returns the child.
     */
    public Object getChild() {
        return this.child;
    }

    /**
     * Getter voor currentParent
     * 
     * @return Returns the currentParent.
     */
    public Object getCurrentParent() {
        return this.currentParent;
    }

    /**
     * Getter voor expectedParent
     * 
     * @return Returns the expectedParent.
     */
    public Object getExpectedParent() {
        return this.expectedParent;
    }
}

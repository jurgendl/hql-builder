package org.tools.hqlbuilder.common;

public class CollectionNeedsInitException extends RefException {
    private static final long serialVersionUID = -3554437279821574385L;

    private final Object target;

    private final String backprop;

    public CollectionNeedsInitException(Object target, String backprop) {
        super("collectie needs initialisation: " + target.getClass() + "#" + backprop);

        this.target = target;
        this.backprop = backprop;
    }

    public Object getTarget() {
        return this.target;
    }

    public String getBackprop() {
        return this.backprop;
    }
}

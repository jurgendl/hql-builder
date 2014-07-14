package org.tools.hqlbuilder.webservice.wicket.forms;

public class NumberFieldSettings<N extends Number & Comparable<N>> extends FormElementSettings {
    private static final long serialVersionUID = 5784680461033564819L;

    protected N minimum;

    protected N maximum;

    protected N step;

    public NumberFieldSettings(N minimum, N maximum, N step) {
        setMinimum(minimum);
        setMaximum(maximum);
        setStep(step);
    }

    public NumberFieldSettings(boolean required, N minimum, N maximum, N step) {
        super(required);
        setMinimum(minimum);
        setMaximum(maximum);
        setStep(step);
    }

    public N getMinimum() {
        return this.minimum;
    }

    public N getMaximum() {
        return this.maximum;
    }

    public N getStep() {
        return this.step;
    }

    public NumberFieldSettings<N> setMinimum(N minimum) {
        this.minimum = minimum;
        return this;
    }

    public NumberFieldSettings<N> setMaximum(N maximum) {
        this.maximum = maximum;
        return this;
    }

    public NumberFieldSettings<N> setStep(N step) {
        this.step = step;
        return this;
    }
}

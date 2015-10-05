package org.tools.hqlbuilder.common;

import java.util.function.Consumer;

public class Value<T> {
    private T value;

    public Value() {
        super();
    }

    public Value(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public Value<T> set(T value) {
        this.value = value;
        return this;
    }

    public Value<T> reset() {
        return set(null);
    }

    public boolean set() {
        return value != null;
    }

    public Value<T> setOr(T t, Consumer<T> f) {
        if (set()) {
            f.accept(get());
        } else {
            set(t);
        }
        return this;
    }
}

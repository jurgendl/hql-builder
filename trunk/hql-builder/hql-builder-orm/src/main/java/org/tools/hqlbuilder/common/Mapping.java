package org.tools.hqlbuilder.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Mapping<S, T> {
    protected final ClassPair<S, T> classPair;

    protected List<BiConsumer<S, T>> consumers = new ArrayList<>();

    public Mapping(ClassPair<S, T> classPair) {
        this.classPair = classPair;
    }

    public Mapping<S, T> redirect(BiConsumer<S, T> consumer) {
        consumers.add(consumer);
        return this;
    }

    public T map(S source) {
        try {
            T target = classPair.getTargetClass().newInstance();
            for (BiConsumer<S, T> consumer : consumers) {
                try {
                    consumer.accept(source, target);
                } catch (RuntimeException ex) {
                    throw new MappingException(ex);
                }
            }
            return target;
        } catch (InstantiationException | IllegalAccessException ex1) {
            throw new MappingException(ex1);
        }
    }
}

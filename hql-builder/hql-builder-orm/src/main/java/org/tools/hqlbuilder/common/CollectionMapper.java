package org.tools.hqlbuilder.common;

@FunctionalInterface
public interface CollectionMapper<S, SC> {
    SC collect(S sourceProxy);
}

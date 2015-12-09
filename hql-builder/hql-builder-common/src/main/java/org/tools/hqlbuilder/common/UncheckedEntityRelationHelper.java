package org.tools.hqlbuilder.common;

import java.util.Collection;

public interface UncheckedEntityRelationHelper<O> {
    <T> void set(String property, T target);

    <T> void clear(String property);

    <T> void add(String property, T target);

    <T> void remove(String property, T target);

    <T> void replace(String property, Collection<T> targets);

    <C extends Collection<E>, E> C get(String property, C collection);

    <C extends Collection<E>, E> C get(C collection);
}

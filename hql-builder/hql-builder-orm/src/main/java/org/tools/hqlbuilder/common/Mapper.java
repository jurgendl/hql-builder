package org.tools.hqlbuilder.common;

import java.util.Map;

@FunctionalInterface
public interface Mapper<S, T> {
    void apply(Map<Object, Object> context, S s, T t);
}

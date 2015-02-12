package org.tools.hqlbuilder.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class J8 {
    public static <T> Collector<T, ?, List<T>> list() {
        Collector<T, ?, List<T>> collection = Collectors.toCollection(ArrayList::new);
        return collection;
    }

    public static <T> Collector<T, ?, Set<T>> set() {
        Collector<T, ?, Set<T>> collection = Collectors.toCollection(HashSet::new);
        return collection;
    }

    public static <T> Collector<T, ?, SortedSet<T>> sortedset() {
        Collector<T, ?, SortedSet<T>> collection = Collectors.toCollection(TreeSet::new);
        return collection;
    }

    public static <T> Stream<T> stream(Collection<T> collection, boolean parallel) {
        return StreamSupport.stream(collection.spliterator(), parallel);
    }
}

package org.tools.hqlbuilder.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Collections8 {
    @SuppressWarnings("unchecked")
    public static <T> T[] array(Collection<T> collection) {
        return collection.toArray((T[]) new Object[collection.size()]);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Entry<K, V>[] array(Map<K, V> map) {
        return map.entrySet().toArray((Map.Entry<K, V>[]) new Map.Entry[0]);
    }

    public static <T> Collector<T, ?, List<T>> list() {
        return Collectors.toCollection(ArrayList::new);
    }

    public static <K, V> Map<K, V> map(Collection<Map.Entry<K, V>> entries) {
        return entries.stream().parallel().collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    }

    public static <T> Collector<T, ?, Set<T>> set() {
        return Collectors.toCollection(HashSet::new);
    }

    public static <T> Collector<T, ?, SortedSet<T>> sortedSet() {
        return Collectors.toCollection(TreeSet::new);
    }

    public static <T> Stream<T> stream(Collection<T> collection) {
        return stream(collection, false);
    }

    @SafeVarargs
    public static <T> Collection<T> filter(Collection<T> collection, Predicate<? super T>... predicates) {
        return filter(collection, false, predicates);
    }

    @SafeVarargs
    public static <T> Collection<T> filter(Collection<T> collection, boolean parallel, Predicate<? super T>... predicates) {
        AtomicReference<Stream<T>> streamReference = new AtomicReference<>(stream(collection, parallel));
        stream(predicates).forEach((predicate) -> streamReference.set(streamReference.get().filter(predicate)));
        return streamReference.get().collect(collector(collection));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> Collector<T, ?, Collection<T>> collector(Collection<T> collection) {
        if (collection instanceof List) {
            return (Collector) Collections8.<T> list();
        }
        if (collection instanceof SortedSet) {
            return (Collector) Collections8.<T> sortedSet();
        }
        if (collection instanceof Set) {
            return (Collector) Collections8.<T> set();
        }
        throw new UnsupportedOperationException(collection.getClass().getName());
    }

    public static <T> Stream<T> stream(Collection<T> collection, boolean parallel) {
        return StreamSupport.stream(collection.spliterator(), parallel);
    }

    public static <K, V> Stream<Map.Entry<K, V>> stream(Map<K, V> map) {
        return stream(map, false);
    }

    public static <K, V> Stream<Map.Entry<K, V>> stream(Map<K, V> map, boolean parallel) {
        return StreamSupport.stream(map.entrySet().spliterator(), parallel);
    }

    public static <T> Stream<T> stream(T[] array) {
        return stream(array, false);
    }

    public static <T> Stream<T> stream(T[] array, boolean parallel) {
        return StreamSupport.stream(Arrays.spliterator(array), parallel);
    }

    public static <T> Stream<T> streamCopy(Collection<T> collection) {
        return streamCopy(collection, false);
    }

    public static <T> Stream<T> streamCopy(Collection<T> collection, boolean parallel) {
        return stream(array(collection), parallel);
    }

    public static <K, V> Stream<Map.Entry<K, V>> streamCopy(Map<K, V> map) {
        return streamCopy(map, false);
    }

    public static <K, V> Stream<Map.Entry<K, V>> streamCopy(Map<K, V> map, boolean parallel) {
        return stream(array(map), parallel);
    }

    @Deprecated
    public static <T> T[] toArray(@SuppressWarnings("unused") Class<T> targetClass, List<T> tmp) {
        // @SuppressWarnings("unchecked")
        // T[] array = (T[]) Array.newInstance(targetClass, tmp.size());
        // return tmp.toArray(array);
        return array(tmp);
    }
}

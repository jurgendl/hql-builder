package org.tools.hqlbuilder.common;

import java.lang.reflect.Array;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class J8 {
    public static <T> Collector<T, ?, List<T>> list() {
        return Collectors.toCollection(ArrayList::new);
    }

    public static <K, V> Map<K, V> map(Collection<Map.Entry<K, V>> entries) {
        return entries.stream().parallel().collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    }

    public static <T> Collector<T, ?, Set<T>> set() {
        return Collectors.toCollection(HashSet::new);
    }

    public static <T> Collector<T, ?, SortedSet<T>> sortedset() {
        return Collectors.toCollection(TreeSet::new);
    }

    public static <T> Stream<T> stream(Collection<T> collection) {
        return stream(collection, false);
    }

    public static <T> Stream<T> stream(Collection<T> collection, boolean parallel) {
        return StreamSupport.stream(collection.spliterator(), parallel);
    }

    public static <T> Stream<T> streamEditable(Collection<T> collection) {
        return streamEditable(collection, false);
    }

    public static <T> Stream<T> streamEditable(Collection<T> collection, boolean parallel) {
        return stream(entries(collection), parallel);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] entries(Collection<T> collection) {
        return collection.toArray((T[]) new Object[collection.size()]);
    }

    public static <K, V> Stream<Map.Entry<K, V>> stream(Map<K, V> map) {
        return stream(map, false);
    }

    public static <K, V> Stream<Map.Entry<K, V>> stream(Map<K, V> map, boolean parallel) {
        return StreamSupport.stream(map.entrySet().spliterator(), parallel);
    }

    public static <K, V> Stream<Map.Entry<K, V>> streamEditable(Map<K, V> map) {
        return streamEditable(map, false);
    }

    public static <K, V> Stream<Map.Entry<K, V>> streamEditable(Map<K, V> map, boolean parallel) {
        return stream(entries(map), parallel);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Entry<K, V>[] entries(Map<K, V> map) {
        return map.entrySet().toArray((Map.Entry<K, V>[]) new Map.Entry[0]);
    }

    public static <T> Stream<T> stream(T[] array) {
        return stream(array, false);
    }

    public static <T> Stream<T> stream(T[] array, boolean parallel) {
        return StreamSupport.stream(Arrays.spliterator(array), parallel);
    }

    public static <T> T[] toArray(Class<T> targetClass, List<T> tmp) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(targetClass, tmp.size());
        return tmp.toArray(array);
    }
}

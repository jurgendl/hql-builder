package org.tools.hqlbuilder.common;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Spliterators;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Collections8 {
    public static class PathIterator implements Iterator<Path> {
        protected Path path;

        public PathIterator(Path path) {
            this.path = path;
        }

        @Override
        public boolean hasNext() {
            Path parent = this.path.getParent();
            return parent != null;
        }

        @Override
        public Path next() {
            this.path = this.path.getParent();
            return this.path;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] array(Collection<T> collection) {
        return collection.toArray((T[]) new Object[collection.size()]);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Entry<K, V>[] array(Map<K, V> map) {
        return map.entrySet().toArray((Map.Entry<K, V>[]) new Map.Entry[0]);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> Collector<T, ?, Collection<T>> collector(Collection<T> collection) {
        if (collection instanceof TransferQueue) {
            return (Collector) Collections8.<T> newTransferQueue();
        }
        if (collection instanceof BlockingDeque) {
            return (Collector) Collections8.<T> newBlockingDeque();
        }
        if (collection instanceof BlockingQueue) {
            return (Collector) Collections8.<T> newBlockingQueue();
        }
        if (collection instanceof Deque) {
            return (Collector) Collections8.<T> newDeque();
        }
        if (collection instanceof Queue) {
            return (Collector) Collections8.<T> newQueue();
        }
        if (collection instanceof List) {
            return (Collector) Collections8.<T> newList();
        }
        if (collection instanceof SortedSet) {
            return (Collector) Collections8.<T> newSortedSet();
        }
        if (collection instanceof Set) {
            return (Collector) Collections8.<T> newSet();
        }
        throw new UnsupportedOperationException(collection.getClass().getName());
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T, C extends Collection<T>> C filter(C collection, boolean parallel, Predicate<? super T>... predicates) {
        AtomicReference<Stream<T>> streamReference = new AtomicReference<>(Collections8.stream(collection, parallel));
        Collections8.stream(predicates).forEach((predicate) -> streamReference.set(streamReference.get().filter(predicate)));
        return (C) streamReference.get().collect(Collections8.collector(collection));
    }

    @SafeVarargs
    public static <T, C extends Collection<T>> C filter(C collection, Predicate<? super T>... predicates) {
        return Collections8.filter(collection, false, predicates);
    }

    public static <K, V> Map<K, V> map(Collection<Map.Entry<K, V>> entries) {
        return Collections8.map(entries, false);
    }

    public static <K, V> Map<K, V> map(Collection<Map.Entry<K, V>> entries, boolean parallel) {
        return map(Collections8.stream(entries, parallel));
    }

    /**
     * Supplier<Map<K, V>> mapSupplier = HashMap::new;
     */
    public static <K, V> Supplier<Map<K, V>> newMap() {
        return HashMap::new;
    }

    /**
     * Supplier<SortedMap<K, V>> mapSupplier = TreeMap::new;
     */
    public static <K, V> Supplier<SortedMap<K, V>> newSortedMap() {
        return TreeMap::new;
    }

    /**
     * Supplier<Map<K, V>> mapSupplier = LinkedHashMap::new;
     */
    public static <K, V> Supplier<Map<K, V>> newLinkedMap() {
        return LinkedHashMap::new;
    }

    public static <K, V> Map<K, V> map(Stream<Entry<K, V>> stream) {
        return map(stream, Collections8.<K, V> newMap());
    }

    public static <K, V> Map<K, V> map(Stream<Entry<K, V>> stream, Supplier<Map<K, V>> mapSupplier) {
        return stream.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue(), rejectDuplicateKeys(), mapSupplier));
    }

    /**
     * @throws IllegalArgumentException
     */
    public static <V> BinaryOperator<V> rejectDuplicateKeys() {
        return (k, v) -> {
            throw new IllegalArgumentException("duplicate key");
        };
    }

    /**
     * BinaryOperator<V> binaryOperator = (k, v) -> k;
     */
    public static <V> BinaryOperator<V> acceptDuplicateKeys() {
        return (k, v) -> k;
    }

    public static <T> Collector<T, ?, BlockingDeque<T>> newBlockingDeque() {
        return Collectors.toCollection(LinkedBlockingDeque::new);
    }

    public static <T> Collector<T, ?, BlockingQueue<T>> newBlockingQueue() {
        return Collectors.toCollection(LinkedBlockingDeque::new);
    }

    public static <T> Collector<T, ?, Deque<T>> newDeque() {
        return Collectors.toCollection(LinkedList::new);
    }

    public static <T> Collector<T, ?, List<T>> newList() {
        return Collectors.toCollection(ArrayList::new);
    }

    public static <T> Collector<T, ?, Queue<T>> newQueue() {
        return Collectors.toCollection(LinkedList::new);
    }

    public static <T> Collector<T, ?, Set<T>> newSet() {
        return Collectors.toCollection(HashSet::new);
    }

    public static <T> Collector<T, ?, SortedSet<T>> newSortedSet() {
        return Collectors.toCollection(TreeSet::new);
    }

    public static <T> Collector<T, ?, TransferQueue<T>> newTransferQueue() {
        return Collectors.toCollection(LinkedTransferQueue::new);
    }

    public static <T> Stream<T> stream(Collection<T> collection) {
        return Collections8.stream(collection, false);
    }

    public static <T> Stream<T> stream(Collection<T> collection, boolean parallel) {
        return StreamSupport.stream(collection.spliterator(), parallel);
    }

    public static <T> Stream<T> stream(Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }

    public static <K, V> Stream<Map.Entry<K, V>> stream(Map<K, V> map) {
        return Collections8.stream(map, false);
    }

    public static <K, V> Stream<Map.Entry<K, V>> stream(Map<K, V> map, boolean parallel) {
        return StreamSupport.stream(map.entrySet().spliterator(), parallel);
    }

    public static Stream<Path> stream(Path path) {
        return Collections8.stream(path, false);
    }

    public static Stream<Path> stream(Path path, boolean parallel) {
        return StreamSupport.stream(path.spliterator(), parallel);
    }

    public static <T> Stream<T> stream(T[] array) {
        return Collections8.stream(array, false);
    }

    public static <T> Stream<T> stream(T[] array, boolean parallel) {
        return StreamSupport.stream(Arrays.spliterator(array), parallel);
    }

    public static <T> Stream<T> streamCopy(Collection<T> collection) {
        return Collections8.streamCopy(collection, false);
    }

    public static <T> Stream<T> streamCopy(Collection<T> collection, boolean parallel) {
        return Collections8.stream(Collections8.array(collection), parallel);
    }

    public static <K, V> Stream<Map.Entry<K, V>> streamCopy(Map<K, V> map) {
        return Collections8.streamCopy(map, false);
    }

    public static <K, V> Stream<Map.Entry<K, V>> streamCopy(Map<K, V> map, boolean parallel) {
        return Collections8.stream(Collections8.array(map), parallel);
    }

    public static Stream<Path> streamFully(Path path) {
        return Collections8.stream(new PathIterator(path));
    }

    /**
     * Function<? super T, ? extends T> value = (t) -> t;
     */
    public static <T> Function<? super T, ? extends T> value() {
        return (t) -> t;
    }

    public static <T extends Comparable<? super T>> List<T> sort(Collection<T> list, boolean parallel) {
        return Collections8.stream(list, parallel).sorted().collect(newList());
    }

    public static <T> List<T> sort(Collection<T> list, boolean parallel, Comparator<? super T> comparator) {
        return Collections8.stream(list, parallel).sorted(comparator).collect(newList());
    }

    public static <T> T last(List<T> dd) {
        int size = dd.size();
        return size == 0 ? null : dd.get(size - 1);
    }

    public static <T> List<T> toList(T[] array) {
        List<T> newList = new ArrayList<>();
        if (array != null) {
            for (T el : array) {
                newList.add(el);
            }
        }
        return newList;
    }

    public static <T> Set<T> toSet(T[] array) {
        Set<T> newSet = new HashSet<>();
        if (array != null) {
            for (T el : array) {
                newSet.add(el);
            }
        }
        return newSet;
    }

    public static <T> List<T> toList(Collection<T> collection) {
        if (collection instanceof List) {
            return (List<T>) collection;
        }
        List<T> newList = new ArrayList<>();
        if (collection != null) {
            newList.addAll(collection);
        }
        return newList;
    }

    public static <T> Set<T> toSet(Collection<T> collection) {
        if (collection instanceof Set) {
            return (Set<T>) collection;
        }
        Set<T> newSet = new HashSet<>();
        if (collection != null) {
            newSet.addAll(collection);
        }
        return newSet;
    }

    public static <T> T cast(Class<T> type, Object object) {
        return type.cast(object);
    }

    public static <T> Function<Object, T> cast(Class<T> type) {
        Function<Object, T> cast = (object) -> type.cast(object);
        return cast;
    }
}

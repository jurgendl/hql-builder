package org.tools.hqlbuilder.common;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

public class MappingFactory {
    protected final Map<Class<?>, Map<String, Property<Object, Object>>> info = new HashMap<>();

    protected final Map<ClassPair<?, ?>, Mapping<?, ?>> mappings = new HashMap<>();

    protected ConversionService conversionService;

    protected boolean parallel = true;

    public MappingFactory() {
        super();
    }

    /**
     * convert <S> to <T>, create new <T>
     */
    public <S, T> T map(S source, Class<T> targetClass) {
        return this.map(new HashMap<>(), source, targetClass);
    }

    /**
     * convert <S> to <T>, reuse <T>
     */
    public <S, T> T map(S source, T target) {
        return this.map(new HashMap<>(), source, target);
    }

    /**
     * convert S[] to Collection<T>, create new Collection<T> and <T>
     */
    public <S, T, Col> Col map(S[] sourceArray, Class<T> targetClass, Collector<T, ?, Col> factory) {
        return mapArrayToCollection(new HashMap<Object, Object>(), sourceArray, targetClass, factory);
    }

    /**
     * convert Collection<S> to Collection<T>, create new Collection<T> and <T>
     */
    public <S, T, Col> Col map(Collection<S> sourceCollection, Class<T> targetClass, Collector<T, ?, Col> factory) {
        return mapCollection(new HashMap<Object, Object>(), sourceCollection, targetClass, factory);
    }

    /**
     * convert Collection<S> to <T>[], create new <T>[] and <T>
     */
    public <S, T> T[] map(Collection<S> sourceCollection, Class<T> targetClass) {
        return mapCollectionToArray(new HashMap<Object, Object>(), sourceCollection, targetClass);
    }

    /**
     * convert S[] to <T>[], create new <T>[] and <T>
     */
    public <S, T> T[] map(S[] sourceArray, Class<T> targetClass) {
        return mapArray(new HashMap<Object, Object>(), sourceArray, targetClass);
    }

    /**
     * is object mappable?
     */
    public <S, T> boolean accept(Class<S> sourceClass, Class<T> targetClass) {
        ClassPair<S, T> classPair = new ClassPair<>(sourceClass, targetClass);
        return this.mappings.containsKey(classPair);
    }

    /**
     * create and return and empty {@link Mapping} from <S> to <T>
     */
    public <S, T> Mapping<S, T> mapping(Class<S> sourceClass, Class<T> targetClass) {
        ClassPair<S, T> classPair = new ClassPair<>(sourceClass, targetClass);
        if (this.mappings.containsKey(classPair)) {
            return this.mapping(classPair);
        }
        Mapping<S, T> mapping = new Mapping<S, T>(classPair);
        this.mappings.put(classPair, mapping);
        return mapping;
    }

    /**
     * add to existing {@link Mapping} from <S> to <T>
     */
    public <S, T> void add(Class<S> sourceClass, Class<T> targetClass, Mapper<S, T> mappingRedirect) {
        this.mapping(sourceClass, targetClass).add(mappingRedirect);
    }

    /**
     * create, build and return a (new) {@link Mapping} from <S> to <T>
     */
    public <S, T> Mapping<S, T> build(Class<S> sourceClass, Class<T> targetClass) throws MappingException {
        Mapping<S, T> mapping = this.mapping(sourceClass, targetClass);
        Map<String, Property<Object, Object>> sourceInfo = this.info(sourceClass);
        mapping.setSourceInfo(sourceInfo);
        Map<String, Property<Object, Object>> targetInfo = this.info(targetClass);
        mapping.setTargetInfo(targetInfo);
        for (Entry<String, Property<Object, Object>> sourceInfoEntry : sourceInfo.entrySet()) {
            String property = sourceInfoEntry.getKey();
            if (targetInfo.containsKey(property)) {
                Property<Object, Object> sourcePD = sourceInfoEntry.getValue();
                Property<Object, Object> targetPD = targetInfo.get(property);
                Class<?> sourceType = sourcePD.type();
                Class<?> targetType = targetPD.type();
                if (Collection.class.isAssignableFrom(sourceType) && Collection.class.isAssignableFrom(targetType)) {
                    mapping.collections(property);
                } else if (targetType.equals(sourceType)) {
                    mapping.add((ctx, source, target) -> targetPD.write(target, sourcePD.read(source)));
                } else if (this.getConversionService().canConvert(sourceType, targetType)) {
                    mapping.add((ctx, source, target) -> targetPD.write(target, this.convert(sourcePD.read(source), targetType)));
                } else {
                    mapping.conditional(property);
                }
            }
        }
        return mapping;
    }

    protected Object convert(Object source, Class<?> targetType) {
        return this.getConversionService().convert(source, targetType);
    }

    /**
     * return previous set {@link ConversionService} or create new {@link DefaultConversionService}
     */
    public ConversionService getConversionService() {
        if (this.conversionService == null) {
            this.conversionService = new DefaultConversionService();
        }
        return this.conversionService;
    }

    protected Map<String, Property<Object, Object>> info(Class<?> clazz) throws MappingException {
        if (!this.info.containsKey(clazz)) {
            try {
                Map<String, PropertyDescriptor> originalMap = J8.stream(Introspector.getBeanInfo(clazz).getPropertyDescriptors(), this.parallel)
                        .filter((t) -> ((t.getWriteMethod() != null) && (t.getReadMethod() != null)))
                        .collect(Collectors.toMap(PropertyDescriptor::getName, Function.<PropertyDescriptor> identity()));
                Map<String, Property<Object, Object>> convertedMap = J8.stream(originalMap, this.parallel).collect(
                        Collectors.toMap(Entry::getKey, e -> new Property<>(e.getValue())));
                this.info.put(clazz, convertedMap);
            } catch (IntrospectionException ex) {
                throw new MappingException(ex);
            }
        }
        return this.info.get(clazz);
    }

    public boolean isParallel() {
        return this.parallel;
    }

    protected <S, T> T map(Map<Object, Object> context, S source, Class<T> targetClass) {
        try {
            return this.map(context, source, targetClass.newInstance());
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new MappingException(ex);
        }
    }

    protected <S, T> T map(Map<Object, Object> context, S source, T target) {
        @SuppressWarnings("unchecked")
        Class<S> sourceClass = (Class<S>) source.getClass();
        @SuppressWarnings("unchecked")
        Class<T> targetClass = (Class<T>) target.getClass();
        Mapping<S, T> mapping = this.mapping(sourceClass, targetClass);
        return mapping.map(context, this, source, target);
    }

    protected <S, T, Col> Col mapStream(HashMap<Object, Object> context, Stream<S> stream, Class<T> targetClass, Collector<T, ?, Col> factory) {
        return stream.map(source -> map(context, source, targetClass)).collect(factory);
    }

    protected <S, T, Col> Col mapArrayToCollection(HashMap<Object, Object> context, S[] sourceArray, Class<T> targetClass,
            Collector<T, ?, Col> factory) {
        return mapStream(context, J8.stream(sourceArray, this.parallel), targetClass, factory);
    }

    protected <S, T, Col> Col mapCollection(HashMap<Object, Object> context, Collection<S> sourceCollection, Class<T> targetClass,
            Collector<T, ?, Col> factory) {
        return mapStream(context, J8.stream(sourceCollection, this.parallel), targetClass, factory);
    }

    protected <S, T> T[] mapCollectionToArray(HashMap<Object, Object> context, Collection<S> sourceCollection, Class<T> targetClass) {
        return J8.toArray(targetClass, mapCollection(context, sourceCollection, targetClass, J8.list()));
    }

    protected <S, T> T[] mapArray(HashMap<Object, Object> context, S[] sourceArray, Class<T> targetClass) {
        return J8.toArray(targetClass, mapArrayToCollection(context, sourceArray, targetClass, J8.list()));
    }

    @SuppressWarnings("unchecked")
    protected <S, T> Mapping<S, T> mapping(ClassPair<S, T> classPair) {
        return (Mapping<S, T>) this.mappings.get(classPair);
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }
}

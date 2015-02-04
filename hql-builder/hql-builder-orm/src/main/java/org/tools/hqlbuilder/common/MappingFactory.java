package org.tools.hqlbuilder.common;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

public class MappingFactory {
    protected final Map<Class<?>, Map<String, Property<Object, Object>>> info = new HashMap<>();

    protected final Map<ClassPair<?, ?>, Mapping<?, ?>> mappings = new HashMap<>();

    protected ConversionService conversionService;

    public MappingFactory() {
        super();
    }

    public <S, T> boolean accept(Class<S> sourceClass, Class<T> targetClass) {
        ClassPair<S, T> classPair = new ClassPair<>(sourceClass, targetClass);
        return this.mappings.containsKey(classPair);
    }

    public <S, T> Mapping<S, T> mapping(Class<S> sourceClass, Class<T> targetClass) {
        ClassPair<S, T> classPair = new ClassPair<>(sourceClass, targetClass);
        if (this.mappings.containsKey(classPair)) {
            return mapping(classPair);
        }
        Mapping<S, T> mapping = new Mapping<S, T>(classPair);
        this.mappings.put(classPair, mapping);
        return mapping;
    }

    @SuppressWarnings("unchecked")
    protected <S, T> Mapping<S, T> mapping(ClassPair<S, T> classPair) {
        return (Mapping<S, T>) this.mappings.get(classPair);
    }

    public <S, T> Mapping<S, T> build(Class<S> sourceClass, Class<T> targetClass) throws MappingException {
        Mapping<S, T> mapping = mapping(sourceClass, targetClass);
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
                if (sourceType.isAssignableFrom(Collection.class) && targetType.isAssignableFrom(Collection.class)) {
                    mapping.collection(property);
                } else if (targetType.isAssignableFrom(sourceType)) {
                    mapping.add((source, target) -> targetPD.write(target, sourcePD.read(source)));
                } else if (this.getConversionService().canConvert(sourceType, targetType)) {
                    mapping.add((source, target) -> targetPD.write(target, convert(sourcePD.read(source), targetType)));
                } else {
                    mapping.conditional(property);
                }
            }
        }
        return mapping;
    }

    protected Object convert(Object source, Class<?> targetType) {
        return getConversionService().convert(source, targetType);
    }

    public ConversionService getConversionService() {
        if (this.conversionService == null) {
            this.conversionService = new DefaultConversionService();
        }
        return this.conversionService;
    }

    public Map<String, Property<Object, Object>> info(Class<?> clazz) throws MappingException {
        if (!this.info.containsKey(clazz)) {
            try {
                Map<String, PropertyDescriptor> originalMap = Arrays.asList(Introspector.getBeanInfo(clazz).getPropertyDescriptors())
                        .parallelStream().filter((t) -> ((t.getWriteMethod() != null) && (t.getReadMethod() != null)))
                        .collect(Collectors.toMap(PropertyDescriptor::getName, Function.<PropertyDescriptor> identity()));
                Map<String, Property<Object, Object>> convertedMap = originalMap.entrySet().parallelStream()
                        .collect(Collectors.toMap(Entry::getKey, e -> new Property<>(e.getValue())));
                info.put(clazz, convertedMap);
            } catch (IntrospectionException ex) {
                throw new MappingException(ex);
            }
        }
        return this.info.get(clazz);
    }

    public <S, T> T map(S source, Class<T> targetClass) {
        try {
            return map(source, targetClass.newInstance());
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new MappingException(ex);
        }
    }

    public <S, T> T map(S source, T target) {
        @SuppressWarnings("unchecked")
        Class<S> sourceClass = (Class<S>) source.getClass();
        @SuppressWarnings("unchecked")
        Class<T> targetClass = (Class<T>) target.getClass();
        Mapping<S, T> mapping = this.mapping(sourceClass, targetClass);
        return mapping.map(this, source, target);
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public <S, T> void add(Class<S> sourceClass, Class<T> targetClass, BiConsumer<S, T> mappingRedirect) {
        mapping(sourceClass, targetClass).add(mappingRedirect);
    }
}

package org.tools.hqlbuilder.common;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

public class MappingFactory {
    protected final Map<Class<?>, Map<String, PropertyDescriptor>> info = new HashMap<>();

    protected final Map<ClassPair<?, ?>, Mapping<?, ?>> mappings = new HashMap<>();

    protected ConversionService conversionService;

    public MappingFactory() {
        super();
    }

    public <S, T> Mapping<S, T> mapping(Class<S> sourceClass, Class<T> targetClass) {
        ClassPair<S, T> classPair = new ClassPair<>(sourceClass, targetClass);
        Mapping<S, T> mapping;
        if (!this.mappings.containsKey(classPair)) {
            mapping = new Mapping<S, T>(classPair);
            this.mappings.put(classPair, mapping);
        } else {
            mapping = (Mapping<S, T>) this.mappings.get(classPair);
        }
        return mapping;
    }

    public <S, T> Mapping<S, T> build(Class<S> sourceClass, Class<T> targetClass) throws MappingException {
        Mapping<S, T> mapping = mapping(sourceClass, targetClass);
        Map<String, PropertyDescriptor> sourceInfo = this.info(sourceClass);
        Map<String, PropertyDescriptor> targetInfo = this.info(targetClass);
        for (Entry<String, PropertyDescriptor> sourceInfoEntry : sourceInfo.entrySet()) {
            String property = sourceInfoEntry.getKey();
            if (targetInfo.containsKey(property)) {
                PropertyDescriptor sourcePD = sourceInfoEntry.getValue();
                PropertyDescriptor targetPD = targetInfo.get(property);
                Class<?> sourceType = sourcePD.getPropertyType();
                Class<?> targetType = targetPD.getPropertyType();
                if (targetType.isAssignableFrom(sourceType)) {
                    mapping.redirect((source, target) -> write(targetPD, target, read(sourcePD, source)));
                } else if (this.getConversionService().canConvert(sourceType, targetType)) {
                    mapping.redirect((source, target) -> write(targetPD, target, convert(read(sourcePD, source), targetType)));
                }
            }
        }
        return mapping;
    }

    protected Object convert(Object source, Class<?> targetType) {
        return getConversionService().convert(source, targetType);
    }

    protected Object read(PropertyDescriptor propertyDescriptor, Object obj) {
        try {
            return propertyDescriptor.getReadMethod().invoke(obj);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void write(PropertyDescriptor propertyDescriptor, Object obj, Object value) {
        try {
            propertyDescriptor.getWriteMethod().invoke(obj, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ConversionService getConversionService() {
        if (this.conversionService == null) {
            this.conversionService = new DefaultConversionService();
        }
        return this.conversionService;
    }

    public Map<String, PropertyDescriptor> info(Class<?> clazz) throws MappingException {
        if (!this.info.containsKey(clazz)) {
            try {
                this.info.put(
                        clazz,
                        Arrays.asList(Introspector.getBeanInfo(clazz).getPropertyDescriptors()).parallelStream()
                        .filter((t) -> ((t.getWriteMethod() != null) && (t.getReadMethod() != null)))
                        .collect(Collectors.toMap(PropertyDescriptor::getName, Function.<PropertyDescriptor> identity())));
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
        Class<S> sourceClass = (Class<S>) source.getClass();
        Class<T> targetClass = (Class<T>) target.getClass();
        Mapping<S, T> mapping = this.mapping(sourceClass, targetClass);
        return mapping.map(source);
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public <S, T> void redirect(Class<S> sourceClass, Class<T> targetClass, BiConsumer<S, T> mappingRedirect) {
        mapping(sourceClass, targetClass).redirect(mappingRedirect);
    }
}

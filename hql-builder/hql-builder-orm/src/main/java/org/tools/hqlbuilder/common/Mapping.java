package org.tools.hqlbuilder.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Mapping<S, T> {
    protected final ClassPair<S, T> classPair;

    protected final Map<String, Property<Object, Object>> sourceInfo = new HashMap<>();

    protected final Map<String, Property<Object, Object>> targetInfo = new HashMap<>();

    protected final List<BiConsumer<S, T>> consumers = new ArrayList<>();

    protected final List<String> conditionals = new ArrayList<String>();

    protected final List<String> collections = new ArrayList<String>();

    public Mapping(ClassPair<S, T> classPair) {
        this.classPair = classPair;
    }

    public Mapping<S, T> add(BiConsumer<S, T> consumer) {
        this.consumers.add(consumer);
        return this;
    }

    public Mapping<S, T> clear() {
        this.collections.clear();
        this.conditionals.clear();
        this.consumers.clear();
        return this;
    }

    @SuppressWarnings("unchecked")
    public <SCT, TCT, SC extends Collection<SCT>, TC extends Collection<TCT>> Mapping<S, T> collect(MappingFactory factory, String sourceProperty,
            String targetProperty, Supplier<TC> collectionFactory, Class<TCT> targetType) {
        this.consumers.add((S source, T target) -> {
            Property<S, SC> sourcePD = (Property<S, SC>) this.sourceInfo.get(sourceProperty);
            SC sourceCollection = sourcePD.read(source);
            TC targetCollection = StreamSupport.stream(sourceCollection.spliterator(), factory.parallel)
                    .map(sourceIt -> factory.map(sourceIt, targetType)).collect(Collectors.toCollection(collectionFactory));
            Property<T, TC> targetPD = (Property<T, TC>) this.targetInfo.get(targetProperty);
            targetPD.write(target, targetCollection);
        });
        return this;
    }

    protected void collections(String property) {
        this.collections.add(property);
    }

    protected void conditional(String property) {
        this.conditionals.add(property);
    }

    public void debug() {
        //
    }

    protected Map<String, Property<Object, Object>> getSourceInfo() {
        return this.sourceInfo;
    }

    protected Map<String, Property<Object, Object>> getTargetInfo() {
        return this.targetInfo;
    }

    protected T map(Map<Object, Object> context, MappingFactory factory, S source, T target) throws MappingException {
        try {
            context.put(source, target);
            T proxy = null;
            for (BiConsumer<S, T> consumer : this.consumers) {
                try {
                    try {
                        consumer.accept(source, target);
                    } catch (NullPointerException ex) {
                        if (proxy == null) {
                            proxy = this.proxy(target);
                        }
                        consumer.accept(source, proxy);
                    }
                } catch (RuntimeException ex) {
                    throw new MappingException(ex);
                }
            }
            for (String conditional : this.conditionals) {
                Property<Object, Object> nestedSourcePD = this.sourceInfo.get(conditional);
                Property<Object, Object> nestedTargetPD = this.targetInfo.get(conditional);
                Class<?> nestedSourceClass = nestedSourcePD.type();
                Class<?> nestedTargetClass = nestedTargetPD.type();
                if (!factory.accept(nestedSourceClass, nestedTargetClass)) {
                    continue;
                }
                Object nestedSourceValue = nestedSourcePD.read(source);
                if (nestedSourceValue == null) {
                    nestedTargetPD.write(target, null);
                    continue;
                }
                if (context.containsKey(nestedSourceValue)) {
                    nestedTargetPD.write(target, context.get(nestedSourceValue));
                    continue;
                }
                Object nestedTargetValue = nestedTargetPD.read(target);
                if (nestedTargetValue == null) {
                    nestedTargetValue = nestedTargetClass.newInstance();
                    nestedTargetPD.write(target, nestedTargetValue);
                }
                factory.map(context, nestedSourceValue, nestedTargetValue);
            }
            return target;
        } catch (InstantiationException | IllegalAccessException ex1) {
            throw new MappingException(ex1);
        }
    }

    public T map(MappingFactory factory, S source) throws MappingException {
        try {
            T target = this.classPair.getTargetClass().newInstance();
            return this.map(factory, source, target);
        } catch (InstantiationException | IllegalAccessException ex1) {
            throw new MappingException(ex1);
        }
    }

    public T map(MappingFactory factory, S source, T target) throws MappingException {
        return this.map(new HashMap<>(), factory, source, target);
    }

    protected T proxy(final T target) throws InstantiationException, IllegalAccessException {
        javassist.util.proxy.ProxyFactory f = new javassist.util.proxy.ProxyFactory();
        f.setSuperclass(this.classPair.getTargetClass());
        javassist.util.proxy.MethodHandler mi = new javassist.util.proxy.MethodHandler() {
            @Override
            public Object invoke(Object self, java.lang.reflect.Method method, java.lang.reflect.Method paramMethod2, Object[] args) throws Throwable {
                try {
                    Object invoke = method.invoke(target, args);
                    if (method.getName().startsWith("get") && (args.length == 0) && (invoke == null)) {
                        String propertyName = method.getName().substring(3);
                        propertyName = Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);
                        Property<Object, Object> pd = Mapping.this.targetInfo.get(propertyName);
                        invoke = method.getReturnType().newInstance();
                        pd.write(target, invoke);
                    }
                    return invoke;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
        };
        @SuppressWarnings("unchecked")
        T proxy = (T) f.createClass().newInstance();
        ((javassist.util.proxy.ProxyObject) proxy).setHandler(mi);
        return proxy;
    }

    protected void setSourceInfo(Map<String, Property<Object, Object>> sourceInfo) {
        this.sourceInfo.clear();
        this.sourceInfo.putAll(sourceInfo);
    }

    protected void setTargetInfo(Map<String, Property<Object, Object>> targetInfo) {
        this.targetInfo.clear();
        this.targetInfo.putAll(targetInfo);
    }
}

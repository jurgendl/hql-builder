package org.tools.hqlbuilder.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mapping<S, T> {
    protected static final Logger logger = LoggerFactory.getLogger(Mapping.class);

    protected final ClassPair<S, T> classPair;

    protected final Map<String, Property<Object, Object>> sourceInfo = new HashMap<>();

    protected final Map<String, Property<Object, Object>> targetInfo = new HashMap<>();

    protected final List<Mapper<S, T>> mappers = new ArrayList<>();

    protected final List<String> conditionals = new ArrayList<String>();

    protected final List<String> collections = new ArrayList<String>();

    public Mapping(ClassPair<S, T> classPair) {
        this.classPair = classPair;
    }

    public Mapping<S, T> add(Mapper<S, T> consumer) {
        this.mappers.add(consumer);
        return this;
    }

    public Mapping<S, T> clear() {
        this.collections.clear();
        this.conditionals.clear();
        this.mappers.clear();
        return this;
    }

    @SuppressWarnings("unchecked")
    public <SCT, TCT, SC extends Collection<SCT>, TC extends Collection<TCT>> Mapping<S, T> collect(MappingFactory factory, String sourceProperty,
            String targetProperty, Supplier<TC> collectionFactory, Class<TCT> targetType) {
        this.mappers.add((Map<Object, Object> context, S source, T target) -> {
            Property<S, SC> sourcePD = (Property<S, SC>) Mapping.this.sourceInfo.get(sourceProperty);
            SC sourceCollection = sourcePD.read(source);
            TC targetCollection = collectionFactory.get();
            for (SCT sourceIt : sourceCollection) {
                TCT targetIt;
                if (context.containsKey(sourceIt)) {
                    targetIt = (TCT) context.get(sourceIt);
                } else {
                    targetIt = factory.map(context, sourceIt, targetType);
                }
                targetCollection.add(targetIt);
            }
            Property<T, TC> targetPD = (Property<T, TC>) Mapping.this.targetInfo.get(targetProperty);
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

    protected T map(Map<Object, Object> context, MappingFactory factory, S source) throws MappingException {
        try {
            T target = this.classPair.getTargetClass().newInstance();
            return this.map(context, factory, source, target);
        } catch (InstantiationException | IllegalAccessException ex1) {
            throw new MappingException(ex1);
        }
    }

    protected T map(Map<Object, Object> context, MappingFactory factory, S source, T target) throws MappingException {
        try {
            Mapping.logger.trace("add to context: " + context.size() + ": " + System.identityHashCode(source) + ": " + source + " > " + target);
            if (context.containsKey(source)) {
                throw new RuntimeException();
            }
            context.put(source, target);
            T proxy = null;
            for (Mapper<S, T> consumer : this.mappers) {
                try {
                    try {
                        consumer.apply(context, source, target);
                    } catch (NullPointerException ex) {
                        Mapping.logger.trace("proxy for " + target);
                        if (proxy == null) {
                            proxy = this.proxy(target);
                        }
                        consumer.apply(context, source, proxy);
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
                boolean breaker = false;
                for (Entry<Object, Object> entry : context.entrySet()) {
                    if (entry.getKey() == nestedSourceValue) {
                        Mapping.logger.trace("reusing " + nestedSourceValue + " > " + entry.getValue());
                        nestedTargetPD.write(target, entry.getValue());
                        breaker = true;
                        break;
                    }
                }
                if (breaker) {
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
        return this.map(new HashMap<>(), factory, source);
    }

    public T map(MappingFactory factory, S source, T target) throws MappingException {
        return this.map(new HashMap<>(), factory, source, target);
    }

    protected T proxy(final T target) throws InstantiationException, IllegalAccessException {
        ProxyFactory f = new ProxyFactory();
        f.setSuperclass(this.classPair.getTargetClass());
        MethodHandler mi = new MethodHandler() {
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
        ((ProxyObject) proxy).setHandler(mi);
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

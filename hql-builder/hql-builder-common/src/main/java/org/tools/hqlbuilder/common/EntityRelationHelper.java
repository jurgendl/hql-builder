package org.tools.hqlbuilder.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.common.EntityRelationHelper.EntityRelationCache.EntityRelationException;
import org.tools.hqlbuilder.common.EntityRelationHelper.EntityRelationCache.EntityRelationException.EntityRelationExceptionType;

@SuppressWarnings("unused")
public class EntityRelationHelper<O> {
    @Transient
    private final transient O bean;

    public EntityRelationHelper(O bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "RelationWrapper[" + bean.getClass().getSimpleName() + "]";
    }

    @SuppressWarnings("unchecked")
    private EntityRelationCache<O> getInstance() {
        return EntityRelationCache.getInstance((Class<O>) this.bean.getClass());
    }

    public <T> void moSet(String property, T target) {
        getInstance().set(bean, property, target);
    }

    public <T> void ooSet(String property, T target) {
        getInstance().set(bean, property, target);
    }

    public <T> void mmClear(String property) {
        getInstance().clear(bean, property);
    }

    public <T> void mmAdd(String property, T target) {
        getInstance().add(bean, property, target);
    }

    public <T> void mmRemove(String property, T target) {
        getInstance().remove(bean, property, target);
    }

    public <T> void mmSet(String property, Collection<T> targets) {
        getInstance().replace(bean, property, targets);
    }

    public <T> void omClear(String property) {
        getInstance().clear(bean, property);
    }

    public <T> void omRemove(String property, T target) {
        getInstance().remove(bean, property, target);
    }

    public <T> void omAdd(String property, T target) {
        getInstance().add(bean, property, target);
    }

    public <T> void omSet(String property, Collection<T> targets) {
        getInstance().replace(bean, property, targets);
    }

    public <C extends Collection<E>, E> C omGet(String property, C collection) {
        return simpleGet(property, collection);
    }

    public <C extends Collection<E>, E> C omGet(C collection) {
        return omGet(null, collection);
    }

    public <C extends Collection<E>, E> C simpleGet(C collection) {
        return simpleGet(null, collection);
    }

    @SuppressWarnings("unchecked")
    public <C extends Collection<E>, E> C simpleGet(String property, C collection) {
        if (collection == null) {
            if (property == null) {
                throw new EntityRelationException("null collection without known propertyname");
            }
            collection = (C) getInstance().createCollection(bean, property);
        }
        if (collection instanceof SortedSet) {
            return (C) Collections.unmodifiableSortedSet((SortedSet<E>) collection);
        } else if (collection instanceof Set) {
            return (C) Collections.unmodifiableSet((Set<E>) collection);
        }
        // can only be a List at this point
        return (C) Collections.unmodifiableList((List<E>) collection);
    }

    public <C extends Collection<E>, E> C mmGet(String property, C collection) {
        return simpleGet(property, collection);
    }

    public <C extends Collection<E>, E> C mmGet(C collection) {
        return mmGet(null, collection);
    }

    public void simpleClear(String property) {
        getCollection(property).clear();
    }

    public <T> void simpleAdd(String property, T target) {
        this.<T> getCollection(property).add(target);
    }

    public <T> void simpleRemove(String property, T target) {
        this.<T> getCollection(property).remove(target);
    }

    private <I> Collection<I> getCollection(String property) {
        return getInstance().<I> getOrCreateCollection(bean, property);
    }

    public <T> void simpleSet(String property, T target) {
        getInstance().invokeSet(bean, property, target);
    }

    public <T> void simpleSet(String property, Collection<T> targets) {
        Collection<T> cast = this.<T> getCollection(property);
        cast.clear();
        if (targets != null) {
            cast.addAll(targets);
        }
    }

    @SuppressWarnings("unchecked")
    public static class EntityRelationCache<P> {
        private boolean throwLazy = false;

        @Transient
        private static final transient Map<Class<?>, EntityRelationCache<?>> cache = new HashMap<Class<?>, EntityRelationCache<?>>();

        @Transient
        private static final transient String UNMOD_SET = "java.util.Collections$UnmodifiableSet";

        @Transient
        private static final transient String UNMOD_SORT_SET = "java.util.Collections$UnmodifiableSortedSet";

        @Transient
        private static final transient String UNMOD_LIST = "java.util.Collections$UnmodifiableList";

        @Transient
        private static final transient String UNMOD_RAND_LIST = "java.util.Collections$UnmodifiableRandomAccessList";

        @Transient
        private static final transient String UNMOD_COLLECTION = "java.util.Collections$UnmodifiableCollection";

        @Transient
        private final transient Class<P> clazz;

        @Transient
        private final transient Map<Field, Object> annotations = new HashMap<Field, Object>();

        @Transient
        private final transient Map<String, Method> getters = new HashMap<String, Method>();

        @Transient
        private final transient Map<String, String> mappedBy = new HashMap<String, String>();

        @Transient
        private final transient Map<String, Field> setters = new HashMap<String, Field>();

        @Transient
        private final transient Logger logger;

        private EntityRelationCache(Class<P> clazz) {
            this.clazz = clazz;
            this.logger = LoggerFactory.getLogger(toString());
        }

        /**
         * get annotatie van gegeven type op gegeven veld
         */
        private <A extends Annotation> A getFieldAnnotation(Field field, Class<A> type) {
            Object annotation = this.annotations.get(field);
            if (annotation == null) {
                annotation = field.getAnnotation(type);
                this.annotations.put(field, annotation);
            }
            if (annotation == null) {
                return null;
            }
            if (type.isAssignableFrom(annotation.getClass())) {
                return type.cast(annotation);
            }
            return null;
        }

        /**
         * is de relatie op de property many-to-many?
         */
        private boolean isManyToMany(String property) {
            return null != getFieldAnnotation(findField(property), ManyToMany.class);
        }

        /**
         * is de relatie op de property many-to-one?
         */
        private boolean isManyToOne(String property) {
            return null != getFieldAnnotation(findField(property), ManyToOne.class);
        }

        /**
         * is de relatie op de property one-to-many?
         */
        private boolean isOneToMany(String property) {
            return null != getFieldAnnotation(findField(property), OneToMany.class);
        }

        /**
         * is de relatie op de property one-to-one?
         */
        private boolean isOneToOne(String property) {
            return null != getFieldAnnotation(findField(property), OneToOne.class);
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "CachedWrapper[" + clazz.getSimpleName() + "]";
        }

        /**
         * get cache for class
         */
        static <C> EntityRelationCache<C> getInstance(Class<C> clazz) {
            if (clazz.getName().toUpperCase().contains("CGLIB") || clazz.getName().toLowerCase().contains("javassist")) {
                clazz = (Class<C>) clazz.getSuperclass();
            }

            EntityRelationCache<C> wrapCache = (EntityRelationCache<C>) EntityRelationCache.cache.get(clazz);

            if (wrapCache == null) {
                wrapCache = new EntityRelationCache<C>(clazz);
                EntityRelationCache.cache.put(clazz, wrapCache);
            }

            return wrapCache;
        }

        /**
         * gets field from bean for property
         */
        private static Field findField(Class<?> beanClass, String property) throws EntityRelationException {
            try {
                return beanClass.getDeclaredField(property);
            } catch (java.lang.NoSuchFieldException ex) {
                Class<?> superClass = beanClass.getSuperclass();
                if (!Object.class.equals(superClass)) {
                    return findField(superClass, property);
                }
                throw new EntityRelationException(ex);
            }
        }

        /**
         * gets getter-method from bean for property
         */
        private static Method findGetMethod(Class<?> beanClass, String property) throws MethodNotFoundException {
            try {
                return beanClass.getMethod("get" + StringUtils.capitalize(property));
            } catch (NoSuchMethodException ex) {
                throw new MethodNotFoundException(ex);
            }
        }

        /**
         * gets field from bean for property (cached)
         */
        private Field findField(String property) throws EntityRelationException {
            Field field = setters.get(property);

            if (field == null) {
                field = findField(clazz, property);
                field.setAccessible(true);
                setters.put(property, field);
            }

            return field;
        }

        /**
         * gets getter-method from bean for property (cached)
         */
        private Method findGetMethod(String property) throws MethodNotFoundException {
            Method method = getters.get(property);

            if (method == null) {
                method = findGetMethod(clazz, property);
                getters.put(property, method);
            }

            return method;
        }

        /**
         * get value
         */
        Object invokeGet(P bean, String property) {
            try {
                return findGetMethod(property).invoke(bean);
            } catch (IllegalAccessException ex) {
                throw new MethodNotFoundException(ex);
            } catch (InvocationTargetException ex) {
                throw new MethodNotFoundException(ex.getCause());
            }
        }

        /**
         * set value
         */
        <T> void invokeSet(P bean, String property, T object) {
            try {
                if (bean instanceof HibernateProxy) {
                    bean = (P) ((HibernateProxy) bean).getHibernateLazyInitializer().getImplementation();
                }
                findField(property).set(bean, object);
            } catch (IllegalAccessException ex) {
                throw new EntityRelationException(ex);
            }
        }

        /**
         * one-to-one set, mappedby moet op deze relatie staan
         */
        <C> void ooSet(P bean, String property, C target) {
            ooSet(bean, property, target, ooMappedBy(property));
        }

        /**
         * one-to-one set
         */
        <C> void ooSet(P bean, String property, C target, String backprop) {
            C child = (C) invokeGet(bean, property);

            if (child == null) {
                if (target == null) {
                    // no action required
                } else {
                    // nieuwChild!=null
                    ooSet0(bean, property, target, backprop);
                }
            } else {
                if (target == null) {
                    // huidigChild!=null
                    ooUnset(bean, property, child, backprop);
                } else {
                    if (!target.equals(child)) {
                        // nieuwChild!=null && huidigChild!=null
                        ooUnset(bean, property, child, backprop);
                        ooSet0(bean, property, target, backprop);
                    } else {
                        // no action required
                    }
                }
            }
        }

        /**
         * many-to-one set
         */
        <C> void moSet(P bean, String property, C target) {
            moSet(bean, property, target, moMappedBy(property));
        }

        /**
         * many-to-one set
         */
        <C> void moSet(P bean, String property, C target, String backprop) {
            C currentValue = (C) invokeGet(bean, property);
            if (currentValue != null && currentValue.equals(target)) {
                return;
            }
            if (currentValue != null) {
                EntityRelationCache<C> targetCache = (EntityRelationCache<C>) EntityRelationCache.getInstance(currentValue.getClass());
                targetCache.omRemove(currentValue, backprop, bean, property);
            }
            if (target != null) {
                EntityRelationCache<C> targetCache = (EntityRelationCache<C>) EntityRelationCache.getInstance(target.getClass());
                targetCache.omAdd(target, backprop, bean, property);
            }
        }

        /**
         * voegt relatie toe
         */
        private <C> void ooUnset(P bean, String childProperty, C child, String parentPropertyName) {
            EntityRelationCache<C> childWrapper = (EntityRelationCache<C>) getInstance(child.getClass());

            P oldParent = (P) childWrapper.invokeGet(child, parentPropertyName);
            C oldChild = (C) invokeGet(bean, childProperty);

            if ((oldParent != null) && oldParent.equals(bean) && (oldChild != null) && oldChild.equals(child)) {
                childWrapper.invokeSet(child, parentPropertyName, null);
                invokeSet(bean, childProperty, null);
            } else {
                throw new EntityRelationException(EntityRelationException.EntityRelationExceptionType.REFERENCE,
                        "!oldParent.equals(parent) || !oldChild.equals(child");
            }
        }

        /**
         * verwijdert relatie
         */
        private <C> void ooSet0(P bean, String childProperty, C child, String parentPropertyName) {
            EntityRelationCache<C> childWrapper = (EntityRelationCache<C>) getInstance(child.getClass());

            P oldParent = (P) childWrapper.invokeGet(child, parentPropertyName);
            C oldChild = (C) invokeGet(bean, childProperty);

            if ((oldChild == null) && (oldParent == null)) {
                childWrapper.invokeSet(child, parentPropertyName, bean);
                invokeSet(bean, childProperty, child);
            } else {
                throw new EntityRelationException(EntityRelationException.EntityRelationExceptionType.REFERENCE,
                        "parentWrapper.get(childProperty) != null || childWrapper.get(parentPropertyName) != null");
            }
        }

        /**
         * many-to-many remove all, mappedby moet op deze relatie staan
         */
        <C> void mmClear(P bean, String property) {
            mmClear(bean, property, mmMappedBy(property));
        }

        /**
         * many-to-many remove all
         */
        <C> void mmClear(P bean, String property, String backprop) {
            Collection<C> collection = (Collection<C>) invokeGet(bean, property);
            // if (collection == null) {// never null because getter creates when needed
            // return;
            // }

            for (C child : (C[]) collection.toArray()) {
                mmRemove(bean, property, collection, child, backprop);
            }
        }

        /**
         * many-to-many add, mappedby moet op deze relatie staan
         */
        <C> void mmAdd(P bean, String property, C target) {
            mmAdd(bean, property, target, mmMappedBy(property));
        }

        /**
         * many-to-many add
         */
        private <C> void mmAdd(P bean, String property, C target, String backprop) {
            Collection<C> children = (Collection<C>) invokeGet(bean, property);
            // if (children == null) { // never null because getter creates when needed
            // children = createCollection(bean, property);
            // }
            mmAdd(bean, property, children, target, backprop);
        }

        private <C> void mmRemove(P bean, String property, Collection<C> children, C target, String backprop) {
            if (target == null) {
                return;
            }

            EntityRelationCache<C> targetWrapper = (EntityRelationCache<C>) getInstance(target.getClass());
            Collection<P> parents = (Collection<P>) targetWrapper.invokeGet(target, backprop);
            // if (parents == null) { // never null because getter creates when needed
            // parents = targetWrapper.createCollection(target, backprop);
            // }

            if (parents.contains(bean) && children.contains(target)) {
                targetWrapper.invokeCollectionRemove(parents, bean);
                invokeCollectionRemove(children, target);
            } else if (parents.contains(bean) && !children.contains(target)) {
                throw new EntityRelationException(bean, null);
            } else if (!parents.contains(bean) && children.contains(target)) {
                throw new EntityRelationException(null, target);
            } // else do nothing
        }

        <C> Collection<C> resolve(Collection<C> children) {
            @SuppressWarnings("rawtypes")
            Class<? extends Collection> collClass = children.getClass();
            String className = collClass.getName();

            if (UNMOD_SET.equals(className) || UNMOD_SORT_SET.equals(className) || UNMOD_LIST.equals(className) || UNMOD_RAND_LIST.equals(className)
                    || UNMOD_COLLECTION.equals(className)) {
                return (Collection<C>) new ObjectWrapper(children).get("c");
            }

            return children;
        }

        /**
         * voegt toe aan collection
         */
        private <C> void invokeCollectionAdd(Collection<C> children, C child) {
            try {
                resolve(children).add(child);
            } catch (org.hibernate.LazyInitializationException ex) {
                if (throwLazy) {
                    throw ex;
                }
            }
        }

        /**
         * verwijdert uit collection
         */
        private <C> void invokeCollectionRemove(Collection<C> children, C child) {
            try {
                resolve(children).remove(child);
            } catch (org.hibernate.LazyInitializationException ex) {
                if (throwLazy) {
                    throw ex;
                }
            }
        }

        /**
         * intern gebruik
         */
        private <C> void mmAdd(P bean, String property, Collection<C> children, C target, String backprop) {
            EntityRelationCache<C> targetWrapper = (EntityRelationCache<C>) getInstance(target.getClass());
            Collection<P> parents = (Collection<P>) targetWrapper.invokeGet(target, backprop);
            // if (parents == null) { // never null because getter creates when needed
            // parents = targetWrapper.createCollection(target, backprop);
            // }

            if (!parents.contains(bean) && !children.contains(target)) {
                targetWrapper.invokeCollectionAdd(parents, bean);
                invokeCollectionAdd(children, target);
            } else if (parents.contains(bean) && !children.contains(target)) {
                throw new EntityRelationException(bean, null);
            } else if (!parents.contains(bean) && children.contains(target)) {
                throw new EntityRelationException(null, target);
            } // else do nothing
        }

        /**
         * many-to-many remove, mappedby moet op deze relatie staan
         */
        <C> void mmRemove(P bean, String property, C target) {
            mmRemove(bean, property, target, mmMappedBy(property));
        }

        /**
         * many-to-many remove
         */
        private <C> void mmRemove(P bean, String property, C target, String backprop) {
            Collection<C> children = (Collection<C>) invokeGet(bean, property);
            // if (children == null) { // never null because getter creates when needed
            // children = createCollection(bean, property);
            // }
            mmRemove(bean, property, children, target, backprop);
        }

        /**
         * many-to-many replace all
         */
        private <C> void mmReplace(P bean, String property, Collection<C> targets, String backprop) {
            Collection<C> children = (Collection<C>) invokeGet(bean, property);
            // if (children == null) { // never null because getter creates when needed
            // children = createCollection(bean, property);
            // }
            if (targets == null) {
                targets = Collections.EMPTY_SET;
            }

            for (C child : (C[]) children.toArray()) {
                if (!targets.contains(child)) {
                    mmRemove(bean, property, children, child, backprop);
                }
            }

            for (C child : targets) {
                if (!children.contains(child)) {
                    mmAdd(bean, property, children, child, backprop);
                }
            }
        }

        /**
         * many-to-many replace all, mappedby moet op deze relatie staan
         */
        <C> void mmReplace(P bean, String property, Collection<C> targets) {
            mmReplace(bean, property, targets, mmMappedBy(property));
        }

        /**
         * one-to-many remove all
         */
        private <C> void omClear(P bean, String property, String backprop) {
            Collection<C> collection = (Collection<C>) invokeGet(bean, property);
            // if (collection == null) { // never null because getter creates when needed
            // return;
            // }

            for (C child : (C[]) collection.toArray()) {
                omRemove(bean, property, collection, child, backprop);
            }
        }

        /**
         * one-to-many remove all, mappedby moet op deze relatie staan
         */
        <C> void omClear(P bean, String property) {
            omClear(bean, property, omMappedBy(property));
        }

        /**
         * one-to-many add
         */
        private <C> void omAdd(P bean, String property, C target, String backprop) {
            if (target == null) {
                return;
            }

            Collection<C> children = (Collection<C>) invokeGet(bean, property);
            // if (children == null) {// never null because getter creates when needed
            // children = createCollection(bean, property);
            // }

            omAdd(bean, property, children, target, backprop);
        }

        /**
         * one-to-many add, mappedby moet op deze relatie staan
         */
        <C> void omAdd(P bean, String property, C target) {
            omAdd(bean, property, target, omMappedBy(property));
        }

        private <C> void omRemove(P bean, String property, Collection<C> children, C target, String backprop) {
            if (!children.contains(target)) {
                return;
            }

            EntityRelationCache<C> targetCache = (EntityRelationCache<C>) getInstance(target.getClass());
            P currentParent = (P) targetCache.invokeGet(target, backprop);

            if (currentParent == null) {
                throw new EntityRelationException(null, target);
            }

            targetCache.invokeSet(target, backprop, null);
            invokeCollectionRemove(children, target);
        }

        private <C> void omAdd(P bean, String property, Collection<C> children, C target, String backprop) {
            try {
                if (children.contains(target)) {
                    return;
                }
            } catch (org.hibernate.LazyInitializationException ex) {
                if (throwLazy) {
                    throw ex;
                }
            }

            EntityRelationCache<C> targetCache = (EntityRelationCache<C>) getInstance(target.getClass());
            P currentParent = (P) targetCache.invokeGet(target, backprop);
            if (bean != null && !bean.equals(currentParent)) {
                if (currentParent != null) {
                    throw new EntityRelationException(currentParent, bean, target);
                }

                targetCache.invokeSet(target, backprop, bean);
                invokeCollectionAdd(children, target);
            }
        }

        /**
         * one-to-many remove
         */
        private <C> void omRemove(P bean, String property, C target, String backprop) {
            if (target == null) {
                return;
            }

            Collection<C> children = (Collection<C>) invokeGet(bean, property);
            // if (children == null) { // never null because getter creates when needed
            // children = createCollection(bean, property);
            // }

            omRemove(bean, property, children, target, backprop);
        }

        /**
         * one-to-many remove, mappedby moet op deze relatie staan
         */
        <C> void omRemove(P bean, String property, C target) {
            omRemove(bean, property, target, omMappedBy(property));
        }

        /**
         * one-to-many replace all, mappedby moet op deze relatie staan
         */
        <C> void omReplace(P bean, String property, Collection<C> targets) {
            omReplace(bean, property, targets, omMappedBy(property));
        }

        /**
         * one-to-many replace all
         */
        private <C> void omReplace(P bean, String property, Collection<C> targets, String backprop) {
            Collection<C> children = (Collection<C>) invokeGet(bean, property);
            // if (children == null) { // never null because getter creates when needed
            // children = createCollection(bean, property);
            // }

            if (targets == null) {
                targets = Collections.EMPTY_SET;
            }

            for (C child : (C[]) children.toArray()) {
                if (!targets.contains(child)) {
                    omRemove(bean, property, children, child, backprop);
                }
            }

            for (C child : targets) {
                if (!children.contains(child)) {
                    omAdd(bean, property, children, child, backprop);
                }
            }
        }

        private Annotation anyAnnotation(Field field) {
            Annotation annotation = null;

            annotation = field.getAnnotation(ManyToMany.class);
            if (annotation != null) {
                return annotation;
            }

            annotation = field.getAnnotation(OneToOne.class);
            if (annotation != null) {
                return annotation;
            }

            annotation = field.getAnnotation(OneToMany.class);
            if (annotation != null) {
                return annotation;
            }

            annotation = field.getAnnotation(ManyToOne.class);
            if (annotation != null) {
                return annotation;
            }

            return null;
        }

        private String mappedBy(Annotation relationAnnotation) {
            if (relationAnnotation instanceof ManyToMany) {
                return ManyToMany.class.cast(relationAnnotation).mappedBy();
            } else if (relationAnnotation instanceof OneToOne) {
                return OneToOne.class.cast(relationAnnotation).mappedBy();
            } else if (relationAnnotation instanceof OneToMany) {
                return OneToMany.class.cast(relationAnnotation).mappedBy();
            }

            // ManyToOne does not have mappedBy
            return null;
        }

        private String mappedBy(String property) {
            String mb = mappedBy.get(property);
            if (mb == null) {
                Annotation mappedByAnno = mappedByAnything(property);
                mb = mappedBy(mappedByAnno);
                if (StringUtils.isBlank(mb)) {
                    mb = mappedByInvers(property, mappedByAnno);
                }
                if (mb == null) {
                    throw new EntityRelationException(EntityRelationExceptionType.MISSING_REFERENCE, clazz.getName() + "; " + property);
                }
                mappedBy.put(property, mb);
            }
            return mb;
        }

        /**
         * one-to-one mapped by?
         */
        private String ooMappedBy(String property) {
            return mappedBy(property);
        }

        /**
         * many-to-many mapped by?
         */
        private String mmMappedBy(String property) {
            return mappedBy(property);
        }

        /**
         * one-to-many mapped by?
         */
        private String omMappedBy(String property) {
            return mappedBy(property);
        }

        /**
         * many-to-one mapped by?
         */
        private String moMappedBy(String property) {
            return mappedBy(property);
        }

        private String mappedByInvers(String property, Annotation relationAnnotation) {
            Class<?> targetEntity = resolve(findField(property), relationAnnotation);
            Class<?> currentEntity = targetEntity;
            Class<?> inversRelation = invers(relationAnnotation);
            while (!Object.class.equals(currentEntity)) {
                for (Field field : currentEntity.getDeclaredFields()) {
                    Annotation anyAnnotation = anyAnnotation(field);
                    if (anyAnnotation != null && //
                            inversRelation.isAssignableFrom(anyAnnotation.getClass()) && //
                            resolve(field, anyAnnotation).isAssignableFrom(clazz) && //
                            property.equals(mappedBy(anyAnnotation))//
                    ) {
                        return field.getName();
                    }
                }
                currentEntity = currentEntity.getSuperclass();
            }
            return null;
        }

        private Class<?> invers(Annotation relationAnnotation) {
            if (relationAnnotation instanceof ManyToMany)
                return ManyToMany.class;
            if (relationAnnotation instanceof OneToOne)
                return OneToOne.class;
            if (relationAnnotation instanceof OneToMany)
                return ManyToOne.class;
            if (relationAnnotation instanceof ManyToOne)
                return OneToMany.class;
            throw new EntityRelationException("unsupported: " + relationAnnotation.getClass().getName());
        }

        private Annotation mappedByAnything(String property) {
            Field field = findField(property);
            Annotation relationAnnotation = null;

            relationAnnotation = field.getAnnotation(ManyToMany.class);
            if (relationAnnotation != null) {
                return relationAnnotation;
            }

            relationAnnotation = field.getAnnotation(OneToOne.class);
            if (relationAnnotation != null) {
                return relationAnnotation;
            }

            relationAnnotation = field.getAnnotation(OneToMany.class);
            if (relationAnnotation != null) {
                return relationAnnotation;
            }

            relationAnnotation = field.getAnnotation(ManyToOne.class);
            if (relationAnnotation != null) {
                return relationAnnotation;
            }

            return null;
        }

        private Class<?> targetEntity(Annotation relationAnnotation) {
            if (relationAnnotation instanceof ManyToMany) {
                return ManyToMany.class.cast(relationAnnotation).targetEntity();
            }

            if (relationAnnotation instanceof OneToOne) {
                return OneToOne.class.cast(relationAnnotation).targetEntity();
            }

            if (relationAnnotation instanceof OneToMany) {
                return OneToMany.class.cast(relationAnnotation).targetEntity();
            }

            if (relationAnnotation instanceof ManyToOne) {
                return ManyToOne.class.cast(relationAnnotation).targetEntity();
            }

            return null;
        }

        private Class<?> resolve(Field field, Annotation anyAnnotation) {
            Class<?> targetEntity = targetEntity(anyAnnotation);
            if (targetEntity != null && !void.class.equals(targetEntity)) {
                return targetEntity;
            }
            if (Collection.class.isAssignableFrom(field.getType())) {
                Type obj = ParameterizedType.class.cast(field.getGenericType()).getActualTypeArguments()[0];
                return Class.class.cast(obj);
            }
            return field.getType();
        }

        /**
         * delegatie naar functie op basis van gevonden relatietype-annotatie
         */
        <C> void add(P bean, String property, C target) {
            Objects.requireNonNull(target);
            if (isManyToMany(property)) {
                mmAdd(bean, property, target);
            } else if (isOneToMany(property)) {
                omAdd(bean, property, target);
            } else {
                throw new EntityRelationException("unsupported: " + clazz.getName() + "; " + property);
            }
        }

        /**
         * delegatie naar functie op basis van gevonden relatietype-annotatie
         */
        <C> void set(P bean, String property, C target) {
            if (isOneToOne(property)) {
                ooSet(bean, property, target);
            } else if (isManyToOne(property)) {
                moSet(bean, property, target);
            } else {
                throw new EntityRelationException("unsupported: " + clazz.getName() + "; " + property);
            }
        }

        /**
         * delegatie naar functie op basis van gevonden relatietype-annotatie
         */
        <C> void clear(P bean, String property) {
            if (isManyToMany(property)) {
                mmClear(bean, property);
            } else if (isOneToMany(property)) {
                omClear(bean, property);
            } else {
                throw new EntityRelationException("unsupported: " + clazz.getName() + "; " + property);
            }
        }

        /**
         * delegatie naar functie op basis van gevonden relatietype-annotatie
         */
        <C> void remove(P bean, String property, C target) {
            Objects.requireNonNull(target);
            if (isManyToMany(property)) {
                mmRemove(bean, property, target);
            } else if (isOneToMany(property)) {
                omRemove(bean, property, target);
            } else {
                throw new EntityRelationException("unsupported: " + clazz.getName() + "; " + property);
            }
        }

        /**
         * delegatie naar functie op basis van gevonden relatietype-annotatie
         */
        <C> void replace(P bean, String property, Collection<C> targets) {
            Objects.requireNonNull(targets);
            if (isManyToMany(property)) {
                mmReplace(bean, property, targets);
            } else if (isOneToMany(property)) {
                omReplace(bean, property, targets);
            } else {
                throw new EntityRelationException("unsupported: " + clazz.getName() + "; " + property);
            }
        }

        <T> Collection<T> createCollection(P bean, String property) {
            Class<T> type = (Class<T>) findField(bean.getClass(), property).getType();
            int modifiers = type.getModifiers();
            Collection<T> collection;
            if (!Modifier.isFinal(modifiers) && !Modifier.isInterface(modifiers) && !Modifier.isAbstract(modifiers)) {
                try {
                    collection = (Collection<T>) type.newInstance();
                } catch (InstantiationException ex) {
                    throw new EntityRelationException(ex);
                } catch (IllegalAccessException ex) {
                    throw new EntityRelationException(ex);
                }
            } else if (List.class.equals(type)) {
                collection = new ArrayList<T>();
            } else if (SortedSet.class.equals(type)) {
                collection = new TreeSet<T>();
            } else if (Set.class.equals(type)) {
                collection = new HashSet<T>();
            } else {
                throw new EntityRelationException("creation of collection not supported: " + type.getName());
            }
            invokeSet(bean, property, collection);
            return collection;
        }

        <I> Collection<I> getOrCreateCollection(P bean, String property) {
            Object object = invokeGet(bean, property);
            Collection<I> collection;
            if (object == null) {
                collection = createCollection(bean, property);
            } else {
                collection = resolve(Collection.class.cast(object));
            }
            return collection;
        }

        public static class EntityRelationException extends RuntimeException {
            private static final long serialVersionUID = -6660143470859271041L;

            public static enum EntityRelationExceptionType {
                GENERAL, MISSING_REFERENCE, EXISTING_REFERENCE, REFERENCE;
            }

            private EntityRelationExceptionType type = EntityRelationExceptionType.GENERAL;

            public EntityRelationException(EntityRelationExceptionType type, Throwable cause) {
                super("" + type, cause);
                this.type = type;
            }

            public EntityRelationException(EntityRelationExceptionType type, String message) {
                super(type + ": " + message);
                this.type = type;
            }

            public EntityRelationException(Throwable cause) {
                this(EntityRelationExceptionType.GENERAL, cause);
            }

            public EntityRelationException(String message) {
                this(EntityRelationExceptionType.GENERAL, message);
            }

            public EntityRelationException(Object expectedParent, Object child) {
                this(EntityRelationExceptionType.MISSING_REFERENCE,
                        "child in collection but parent is null: child=" + child + ", expectedParent=" + expectedParent);
            }

            public EntityRelationException(Object currentParent, Object expectedParent, Object child) {
                this(EntityRelationExceptionType.EXISTING_REFERENCE, "child niet in collectie en parent verwijst naar derde: child=" + child
                        + ", expectedParent=" + expectedParent + ", currentParent=" + currentParent);
            }

            public EntityRelationExceptionType getType() {
                return this.type;
            }
        }

        public static class MethodNotFoundException extends EntityRelationException {
            private static final long serialVersionUID = 5813185363326141162L;

            public MethodNotFoundException(String message) {
                super(message);
            }

            public MethodNotFoundException(Throwable cause) {
                super(cause);
            }

            public MethodNotFoundException(String message, Throwable cause) {
                super(message, cause);
            }
        }
    }
}
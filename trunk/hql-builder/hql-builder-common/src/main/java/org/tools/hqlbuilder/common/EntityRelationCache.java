package org.tools.hqlbuilder.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.proxy.HibernateProxy;

@SuppressWarnings("unchecked")
class EntityRelationCache<P> {
    @Transient
    protected static final transient Map<Class<?>, EntityRelationCache<?>> cache = new HashMap<Class<?>, EntityRelationCache<?>>();

    @Transient
    protected static final transient String UNMOD_SET = "java.util.Collections$UnmodifiableSet";

    @Transient
    protected static final transient String UNMOD_SORT_SET = "java.util.Collections$UnmodifiableSortedSet";

    @Transient
    protected static final transient String UNMOD_LIST = "java.util.Collections$UnmodifiableList";

    @Transient
    protected static final transient String UNMOD_RAND_LIST = "java.util.Collections$UnmodifiableRandomAccessList";

    @Transient
    protected static final transient String UNMOD_COLLECTION = "java.util.Collections$UnmodifiableCollection";

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
    private final transient Object NULL = "NULL";

    public EntityRelationCache(Class<P> clazz) {
        this.clazz = clazz;
    }

    /**
     * get annotatie van gegeven type op gegeven veld
     */
    protected <A extends Annotation> A getFieldAnnotation(Field field, Class<A> type) {
        Object annotation = this.annotations.get(field);

        if (annotation == null) {
            annotation = field.getAnnotation(type);

            if (annotation == null) {
                annotation = NULL;
            }

            this.annotations.put(field, annotation);
        }

        if (NULL.equals(annotation)) {
            return null;
        }

        return (A) annotation;
    }

    /**
     * is de relatie op de property many-to-many?
     */
    protected boolean isManyToMany(String property) {
        return null != getFieldAnnotation(findField(property), ManyToMany.class);
    }

    /**
     * is de relatie op de property many-to-one?
     */
    protected boolean isManyToOne(String property) {
        return null != getFieldAnnotation(findField(property), OneToMany.class);
    }

    /**
     * is de relatie op de property one-to-many?
     */
    protected boolean isOneToMany(String property) {
        return null != getFieldAnnotation(findField(property), OneToMany.class);
    }

    /**
     * is de relatie op de property one-to-one?
     */
    protected boolean isOneToOne(String property) {
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
    protected static <C> EntityRelationCache<C> getInstance(Class<C> clazz) {
        if (clazz.getName().toUpperCase().contains("CGLIB") || clazz.getName().toLowerCase().contains("javassist")) {
            clazz = (Class<C>) clazz.getSuperclass();
        }

        EntityRelationCache<C> wrapCache = (EntityRelationCache<C>) EntityRelationCache.cache.get(clazz);

        if (wrapCache == null) {
            wrapCache = new EntityRelationCache<C>(clazz);
        }

        return wrapCache;
    }

    /**
     * gets field from bean for property
     */
    protected static Field findField(Class<?> beanClass, String property) throws EntityRelationException {
        try {
            return beanClass.getDeclaredField(property);
        } catch (java.lang.NoSuchFieldException ex) {
            Class<?> superClass = beanClass.getSuperclass();

            if (!Object.class.equals(superClass)) {
                return findField(superClass, property);
            }

            throw new EntityRelationException(ex);
        } catch (SecurityException ex) {
            throw new EntityRelationException(ex);
        } catch (RuntimeException ex) {
            throw new EntityRelationException(ex);
        }
    }

    /**
     * gets getter-method from bean for property
     */
    protected static Method findGetMethod(Class<?> beanClass, String property) throws MethodNotFoundException {
        try {
            return beanClass.getMethod("get" + StringUtils.capitalize(property));
        } catch (RuntimeException ex) {
            throw new MethodNotFoundException(ex);
        } catch (NoSuchMethodException ex) {
            throw new MethodNotFoundException(ex);
        }
    }

    /**
     * gets field from bean for property (cached)
     */
    protected Field findField(String property) throws EntityRelationException {
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
    protected Method findGetMethod(String property) throws MethodNotFoundException {
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
    protected <T> T invokeGet(P bean, String property, Class<T> type) {
        return type.cast(invokeGet(bean, property));
    }

    /**
     * get value
     */
    protected Object invokeGet(P bean, String property) {
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
    protected <T> void invokeSet(P bean, String property, T object) {
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
    public <C> void ooSet(P bean, String property, C target) {
        // @javax.persistence.OneToOne(mappedBy = "centralObject")
        // private OneToOne oneToOne;
        //
        // @javax.persistence.OneToOne
        // private CentralObject centralObject;
        //
        ooSet(bean, property, target, ooMappedBy(property));
    }

    /**
     * one-to-one set
     */
    public <C> void ooSet(P bean, String property, C target, String backprop) {
        // @javax.persistence.OneToOne(mappedBy = "centralObject")
        // private OneToOne oneToOne;
        //
        // @javax.persistence.OneToOne
        // private CentralObject centralObject;
        //
        C child = (C) invokeGet(bean, property);

        if ((child == null) && (target == null)) {
            return;
        } else if (child == null) { // nieuwChild!=null
            ooSet0(bean, property, target, backprop);
        } else if (target == null) { // huidigChild!=null
            ooUnset(bean, property, child, backprop);
        } else if (!target.equals(child)) { // nieuwChild!=null &&
            // huidigChild!=null
            ooUnset(bean, property, child, backprop);
            ooSet0(bean, property, target, backprop);
        } // else => no action required
    }

    /**
     * many-to-one set
     */
    public <C> void moSet(P bean, String property, C target) {
        String backprop = moInverseProp(bean, property);
        moSet(bean, property, target, backprop);
    }

    /**
     * many-to-one set
     */
    public <C> void moSet(P bean, String property, C target, String backprop) {
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
    protected <C> void ooUnset(P bean, String childProperty, C child, String parentPropertyName) {
        EntityRelationCache<C> childWrapper = (EntityRelationCache<C>) getInstance(child.getClass());

        P oldParent = (P) childWrapper.invokeGet(child, parentPropertyName);
        C oldChild = (C) invokeGet(bean, childProperty);

        if ((oldParent != null) && oldParent.equals(bean) && (oldChild != null) && oldChild.equals(child)) {
            childWrapper.invokeSet(child, parentPropertyName, null);
            invokeSet(bean, childProperty, null);
        } else {
            throw new RefException("!oldParent.equals(parent) || !oldChild.equals(child");
        }
    }

    /**
     * verwijdert relatie
     */
    protected <C> void ooSet0(P bean, String childProperty, C child, String parentPropertyName) {
        EntityRelationCache<C> childWrapper = (EntityRelationCache<C>) getInstance(child.getClass());

        P oldParent = (P) childWrapper.invokeGet(child, parentPropertyName);
        C oldChild = (C) invokeGet(bean, childProperty);

        if ((oldChild == null) && (oldParent == null)) {
            childWrapper.invokeSet(child, parentPropertyName, bean);
            invokeSet(bean, childProperty, child);
        } else {
            throw new RefException("parentWrapper.get(childProperty) != null || childWrapper.get(parentPropertyName) != null");
        }
    }

    /**
     * many-to-many remove all, mappedby moet op deze relatie staan
     */
    public <C> void mmClear(P bean, String property) {
        // @javax.persistence.ManyToMany(mappedBy = "centralObjects")
        // private Set<ManyToMany> manyToManys = new HashSet<ManyToMany>();
        //
        // @javax.persistence.ManyToMany
        // private Set<CentralObject> centralObjects = new
        // HashSet<CentralObject>();
        //
        mmClear(bean, property, mmMappedBy(property));
    }

    /**
     * many-to-many remove all
     */
    public <C> void mmClear(P bean, String property, String backprop) {
        // @javax.persistence.ManyToMany
        // private Set<ManyToMany> manyToManys = new HashSet<ManyToMany>();
        //
        // @javax.persistence.ManyToMany(mappedBy = "manyToManys")
        // private Set<CentralObject> centralObjects = new
        // HashSet<CentralObject>();
        //
        // OF
        //
        // @javax.persistence.ManyToMany(mappedBy = "centralObjects")
        // private Set<ManyToMany> manyToManys = new HashSet<ManyToMany>();
        //
        // @javax.persistence.ManyToMany
        // private Set<CentralObject> centralObjects = new
        // HashSet<CentralObject>();
        //
        Collection<C> collection = (Collection<C>) invokeGet(bean, property);
        if (collection == null) {
            collection = invokeCreateCollection(bean, property);
            // throw new CollectionNeedsInitException(bean, property);
        }

        C[] array = (C[]) collection.toArray();

        for (C child : array) {
            mmRemove(bean, property, collection, child, backprop);
        }
    }

    /**
     * many-to-many add, mappedby moet op deze relatie staan
     */
    public <C> void mmAdd(P bean, String property, C target) {
        // @javax.persistence.ManyToMany(mappedBy = "centralObjects")
        // private Set<ManyToMany> manyToManys = new HashSet<ManyToMany>();
        //
        // @javax.persistence.ManyToMany
        // private Set<CentralObject> centralObjects = new
        // HashSet<CentralObject>();
        //
        mmAdd(bean, property, target, mmMappedBy(property));
    }

    /**
     * many-to-many add
     */
    public <C> void mmAdd(P bean, String property, C target, String backprop) {
        // @javax.persistence.ManyToMany
        // private Set<ManyToMany> manyToManys = new HashSet<ManyToMany>();
        //
        // @javax.persistence.ManyToMany(mappedBy = "manyToManys")
        // private Set<CentralObject> centralObjects = new
        // HashSet<CentralObject>();
        //
        // OF
        //
        // @javax.persistence.ManyToMany(mappedBy = "centralObjects")
        // private Set<ManyToMany> manyToManys = new HashSet<ManyToMany>();
        //
        // @javax.persistence.ManyToMany
        // private Set<CentralObject> centralObjects = new
        // HashSet<CentralObject>();
        //
        Collection<C> children = (Collection<C>) invokeGet(bean, property);
        if (children == null) {
            children = invokeCreateCollection(bean, property);
            // throw new CollectionNeedsInitException(bean, property);
        }

        mmAdd(bean, property, children, target, backprop);
    }

    protected <C> void mmRemove(P bean, @SuppressWarnings("unused") String property, Collection<C> children, C target, String backprop) {
        // @javax.persistence.ManyToMany
        // private Set<ManyToMany> manyToManys = new HashSet<ManyToMany>();
        //
        // @javax.persistence.ManyToMany(mappedBy = "manyToManys")
        // private Set<CentralObject> centralObjects = new
        // HashSet<CentralObject>();
        //
        if (target == null) {
            return;
        }

        EntityRelationCache<C> targetWrapper = (EntityRelationCache<C>) getInstance(target.getClass());
        Collection<P> parents = (Collection<P>) targetWrapper.invokeGet(target, backprop);
        if (parents == null) {
            parents = targetWrapper.invokeCreateCollection(target, backprop);
            // throw new CollectionNeedsInitException(target, backprop);
        }

        if (parents.contains(bean) && children.contains(target)) {
            targetWrapper.invokeCollectionRemove(parents, bean);
            invokeCollectionRemove(children, target);
        } else if (parents.contains(bean) && !children.contains(target)) {
            throw new MissingRefException(bean, null);
        } else if (!parents.contains(bean) && children.contains(target)) {
            throw new MissingRefException(null, target);
        } // else do nothing
    }

    protected <C> Collection<C> resolve(Collection<C> children) {
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
    protected <C> void invokeCollectionAdd(Collection<C> children, C child) {
        resolve(children).add(child);
    }

    /**
     * verwijdert uit collection
     */
    protected <C> void invokeCollectionRemove(Collection<C> children, C child) {
        resolve(children).remove(child);
    }

    /**
     * intern gebruik
     */
    protected <C> void mmAdd(P bean, @SuppressWarnings("unused") String property, Collection<C> children, C target, String backprop) {
        // @javax.persistence.ManyToMany
        // private Set<ManyToMany> manyToManys = new HashSet<ManyToMany>();
        //
        // @javax.persistence.ManyToMany(mappedBy = "manyToManys")
        // private Set<CentralObject> centralObjects = new
        // HashSet<CentralObject>();
        //
        if (target == null) {
            return;
        }

        EntityRelationCache<C> targetWrapper = (EntityRelationCache<C>) getInstance(target.getClass());
        Collection<P> parents = (Collection<P>) targetWrapper.invokeGet(target, backprop);

        if (parents == null) {
            parents = targetWrapper.invokeCreateCollection(target, backprop);
            // throw new CollectionNeedsInitException(target, backprop);
        }

        if (!parents.contains(bean) && !children.contains(target)) {
            targetWrapper.invokeCollectionAdd(parents, bean);
            invokeCollectionAdd(children, target);
        } else if (parents.contains(bean) && !children.contains(target)) {
            throw new MissingRefException(bean, null);
        } else if (!parents.contains(bean) && children.contains(target)) {
            throw new MissingRefException(null, target);
        } // else do nothing
    }

    /**
     * many-to-many remove, mappedby moet op deze relatie staan
     */
    public <C> void mmRemove(P bean, String property, C target) {
        // @javax.persistence.ManyToMany(mappedBy = "centralObjects")
        // private Set<ManyToMany> manyToManys = new HashSet<ManyToMany>();
        //
        // @javax.persistence.ManyToMany
        // private Set<CentralObject> centralObjects = new
        // HashSet<CentralObject>();
        //
        mmRemove(bean, property, target, mmMappedBy(property));
    }

    /**
     * many-to-many remove
     */
    public <C> void mmRemove(P bean, String property, C target, String backprop) {
        // @javax.persistence.ManyToMany
        // private Set<ManyToMany> manyToManys = new HashSet<ManyToMany>();
        //
        // @javax.persistence.ManyToMany(mappedBy = "manyToManys")
        // private Set<CentralObject> centralObjects = new
        // HashSet<CentralObject>();
        //
        // OF
        //
        // @javax.persistence.ManyToMany(mappedBy = "centralObjects")
        // private Set<ManyToMany> manyToManys = new HashSet<ManyToMany>();
        //
        // @javax.persistence.ManyToMany
        // private Set<CentralObject> centralObjects = new
        // HashSet<CentralObject>();
        //
        Collection<C> children = (Collection<C>) invokeGet(bean, property);
        if (children == null) {
            children = invokeCreateCollection(bean, property);
            // throw new CollectionNeedsInitException(bean, property);
        }

        mmRemove(bean, property, children, target, backprop);
    }

    /**
     * many-to-many replace all
     */
    public <C> void mmReplace(P bean, String property, Collection<C> targets, String backprop) {
        // @javax.persistence.ManyToMany
        // private Set<ManyToMany> manyToManys = new HashSet<ManyToMany>();
        //
        // @javax.persistence.ManyToMany(mappedBy = "manyToManys")
        // private Set<CentralObject> centralObjects = new
        // HashSet<CentralObject>();
        //
        // OF
        //
        // @javax.persistence.ManyToMany(mappedBy = "centralObjects")
        // private Set<ManyToMany> manyToManys = new HashSet<ManyToMany>();
        //
        // @javax.persistence.ManyToMany
        // private Set<CentralObject> centralObjects = new
        // HashSet<CentralObject>();
        //
        Collection<C> children = (Collection<C>) invokeGet(bean, property);
        if (children == null) {
            children = invokeCreateCollection(bean, property);
            // throw new CollectionNeedsInitException(bean, property);
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
    public <C> void mmReplace(P bean, String property, Collection<C> targets) {
        // @javax.persistence.ManyToMany(mappedBy = "centralObjects")
        // private Set<ManyToMany> manyToManys = new HashSet<ManyToMany>();
        //
        // @javax.persistence.ManyToMany
        // private Set<CentralObject> centralObjects = new
        // HashSet<CentralObject>();
        //
        mmReplace(bean, property, targets, mmMappedBy(property));
    }

    /**
     * one-to-many remove all
     */
    public <C> void omClear(P bean, String property, String backprop) {
        // @javax.persistence.OneToMany(mappedBy = "centralObject")
        // private Set<OneToMany> oneToManys = new HashSet<OneToMany>();
        //
        // @ManyToOne
        // private CentralObject centralObject;
        //
        Collection<C> collection = (Collection<C>) invokeGet(bean, property);

        if (collection == null) {
            return;
        }

        C[] array = (C[]) collection.toArray();

        for (C child : array) {
            omRemove(bean, property, collection, child, backprop);
        }
    }

    /**
     * one-to-many remove all, mappedby moet op deze relatie staan
     */
    public <C> void omClear(P bean, String property) {
        // @javax.persistence.OneToMany(mappedBy = "centralObject")
        // private Set<OneToMany> oneToManys = new HashSet<OneToMany>();
        //
        // @ManyToOne
        // private CentralObject centralObject;
        //
        omClear(bean, property, omMappedBy(property));
    }

    /**
     * one-to-many add
     */
    public <C> void omAdd(P bean, String property, C target, String backprop) {
        // @javax.persistence.OneToMany(mappedBy = "centralObject")
        // private Set<OneToMany> oneToManys = new HashSet<OneToMany>();
        //
        // @ManyToOne
        // private CentralObject centralObject;
        //
        if (target == null) {
            return;
        }

        Collection<C> children = (Collection<C>) invokeGet(bean, property);
        if (children == null) {
            children = invokeCreateCollection(bean, property);
            // throw new CollectionNeedsInitException(bean, property);
        }

        omAdd(bean, property, children, target, backprop);
    }

    /**
     * one-to-many add, mappedby moet op deze relatie staan
     */
    public <C> void omAdd(P bean, String property, C target) {
        // @javax.persistence.OneToMany(mappedBy = "centralObject")
        // private Set<OneToMany> oneToManys = new HashSet<OneToMany>();
        //
        // @ManyToOne
        // private CentralObject centralObject;
        //
        omAdd(bean, property, target, omMappedBy(property));
    }

    protected <C> void omRemove(@SuppressWarnings("unused") P bean, @SuppressWarnings("unused") String property, Collection<C> children, C target,
            String backprop) {
        // @javax.persistence.OneToMany(mappedBy = "centralObject")
        // private Set<OneToMany> oneToManys = new HashSet<OneToMany>();
        //
        // @ManyToOne
        // private CentralObject centralObject;
        //
        if (!children.contains(target)) {
            return;
        }

        EntityRelationCache<C> targetCache = (EntityRelationCache<C>) getInstance(target.getClass());
        P currentParent = (P) targetCache.invokeGet(target, backprop);

        if (currentParent == null) {
            throw new MissingRefException(null, target);
        }

        targetCache.invokeSet(target, backprop, null);
        invokeCollectionRemove(children, target);
    }

    protected <C> void omAdd(P bean, @SuppressWarnings("unused") String property, Collection<C> children, C target, String backprop) {
        // @javax.persistence.OneToMany(mappedBy = "centralObject")
        // private Set<OneToMany> oneToManys = new HashSet<OneToMany>();
        //
        // @ManyToOne
        // private CentralObject centralObject;
        //
        if (children.contains(target)) {
            return;
        }

        EntityRelationCache<C> targetCache = (EntityRelationCache<C>) getInstance(target.getClass());
        P currentParent = (P) targetCache.invokeGet(target, backprop);
        if (bean != null && !bean.equals(currentParent)) {
            if (currentParent != null) {
                throw new ExistingParentChildRefException(currentParent, bean, target);
            }

            targetCache.invokeSet(target, backprop, bean);
            invokeCollectionAdd(children, target);
        }
    }

    /**
     * one-to-many remove
     */
    public <C> void omRemove(P bean, String property, C target, String backprop) {
        // @javax.persistence.OneToMany(mappedBy = "centralObject")
        // private Set<OneToMany> oneToManys = new HashSet<OneToMany>();
        //
        // @ManyToOne
        // private CentralObject centralObject;
        //
        if (target == null) {
            return;
        }

        Collection<C> children = (Collection<C>) invokeGet(bean, property);
        if (children == null) {
            children = invokeCreateCollection(bean, property);
            // throw new CollectionNeedsInitException(bean, property);
        }

        omRemove(bean, property, children, target, backprop);
    }

    /**
     * one-to-many remove, mappedby moet op deze relatie staan
     */
    public <C> void omRemove(P bean, String property, C target) {
        // @javax.persistence.OneToMany(mappedBy = "centralObject")
        // private Set<OneToMany> oneToManys = new HashSet<OneToMany>();
        //
        // @ManyToOne
        // private CentralObject centralObject;
        //
        omRemove(bean, property, target, omMappedBy(property));
    }

    /**
     * one-to-many replace all, mappedby moet op deze relatie staan
     */
    public <C> void omReplace(P bean, String property, Collection<C> targets) {
        // @javax.persistence.OneToMany(mappedBy = "centralObject")
        // private Set<OneToMany> oneToManys = new HashSet<OneToMany>();
        //
        // @ManyToOne
        // private CentralObject centralObject;
        //
        omReplace(bean, property, targets, omMappedBy(property));
    }

    /**
     * one-to-many replace all
     */
    public <C> void omReplace(P bean, String property, Collection<C> targets, String backprop) {
        // @javax.persistence.OneToMany(mappedBy = "centralObject")
        // private Set<OneToMany> oneToManys = new HashSet<OneToMany>();
        //
        // @ManyToOne
        // private CentralObject centralObject;
        //
        Collection<C> children = (Collection<C>) invokeGet(bean, property);
        if (children == null) {
            children = invokeCreateCollection(bean, property);
            // throw new CollectionNeedsInitException(bean, property);
        }

        for (C child : (C[]) children.toArray()) {
            if (!targets.contains(child)) {
                omRemove(bean, property, children, child, backprop);
            }
        }

        try {
            for (C child : targets) {
                if (!children.contains(child)) {
                    omAdd(bean, property, children, child, backprop);
                }
            }
        } catch (NullPointerException ex) {
            throw ex;
        }
    }

    /**
     * not null check
     */
    protected <U> U nn(U object) {
        if (object == null) {
            throw new NullPointerException();
        }

        if (object instanceof String && object.toString().trim().equals("")) {
            throw new NullPointerException();
        }

        return object;
    }

    /**
     * one-to-one mapped by?
     */
    protected String ooMappedBy(String property) {
        String mb = mappedBy.get(property);

        if (mb == null) {
            mb = nn(mappedBy(property, OneToOne.class).mappedBy());
            mappedBy.put(property, mb);
        }

        return mb;
    }

    /**
     * many-to-many mapped by?
     */
    protected String mmMappedBy(String property) {
        String mb = mappedBy.get(property);

        if (mb == null) {
            mb = nn(mappedBy(property, ManyToMany.class).mappedBy());
            mappedBy.put(property, mb);
        }

        return mb;
    }

    /**
     * one-to-manye mapped by?
     */
    protected String omMappedBy(String property) {
        String mb = mappedBy.get(property);

        if (mb == null) {
            mb = nn(mappedBy(property, OneToMany.class).mappedBy());
            mappedBy.put(property, mb);
        }

        return mb;
    }

    /**
     * relatie mapped by?
     */
    protected <T> T mappedBy(String property, Class<T> annotationClass) {
        return annotationClass.cast(nn(findField(property).getAnnotation(annotationClass.asSubclass(Annotation.class))));
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     */
    public <C> void add(P bean, String property, C target, String backprop) {
        if (isManyToMany(property)) {
            mmAdd(bean, property, target, backprop);
        } else if (isOneToMany(property)) {
            omAdd(bean, property, target, backprop);
        }

        throw new IllegalArgumentException();
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     */
    public <C> void clear(P bean, String property, String backprop) {
        if (isManyToMany(property)) {
            mmClear(bean, property, backprop);
        } else if (isOneToMany(property)) {
            omClear(bean, property, backprop);
        }

        throw new IllegalArgumentException();
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     */
    public <C> void remove(P bean, String property, C target, String backprop) {
        if (isManyToMany(property)) {
            mmRemove(bean, property, target, backprop);
        } else if (isOneToMany(property)) {
            omRemove(bean, property, target, backprop);
        }

        throw new IllegalArgumentException();
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     */
    public <C> void replace(P bean, String property, Collection<C> targets, String backprop) {
        if (isManyToMany(property)) {
            mmReplace(bean, property, targets, backprop);
        } else if (isOneToMany(property)) {
            omReplace(bean, property, targets, backprop);
        }

        throw new IllegalArgumentException();
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     */
    public <C> void set(P bean, String property, C target, String backprop) {
        if (isOneToOne(property)) {
            ooSet(bean, property, target, backprop);
        } else if (isManyToOne(property)) {
            moSet(bean, property, target, backprop);
        }

        throw new IllegalArgumentException();
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     */
    public <C> void clear(P bean, String property) {
        if (isManyToMany(property)) {
            mmClear(bean, property);
        } else if (isOneToMany(property)) {
            omClear(bean, property);
        }

        throw new IllegalArgumentException();
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     */
    public <C> void remove(P bean, String property, C target) {
        if (isManyToMany(property)) {
            mmRemove(bean, property, target);
        } else if (isOneToMany(property)) {
            omRemove(bean, property, target);
        }

        throw new IllegalArgumentException();
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     */
    public <C> void replace(P bean, String property, Collection<C> targets) {
        if (isManyToMany(property)) {
            mmReplace(bean, property, targets);
        } else if (isOneToMany(property)) {
            omReplace(bean, property, targets);
        }

        throw new IllegalArgumentException();
    }

    protected String moInverseProp(P bean, String property) {
        String backProp = mappedBy.get(property);

        if (backProp == null) {
            Class<?> beanClass = bean.getClass();
            Class<?> otherBean = findField(beanClass, property).getType();

            for (Field otherField : otherBean.getDeclaredFields()) {
                OneToMany oneToMany = otherField.getAnnotation(OneToMany.class);
                if (oneToMany != null) {
                    if (property.equals(oneToMany.mappedBy())) {
                        backProp = otherField.getName();
                        break;
                    }
                }
            }

            if (backProp == null) {
                throw new IllegalArgumentException("inverse property not found: " + beanClass.getName() + "#" + property);
            }

            mappedBy.put(property, backProp);
        }

        return backProp;
    }

    protected <T> Collection<T> invokeCreateCollection(P bean, String property) {
        Class<T> type = (Class<T>) findField(bean.getClass(), property).getType();
        int modifiers = type.getModifiers();
        Collection<T> collection;
        if (!Modifier.isFinal(modifiers) && !Modifier.isInterface(modifiers) && !Modifier.isAbstract(modifiers)) {
            try {
                collection = (Collection<T>) type.newInstance();
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        } else if (List.class.equals(type)) {
            collection = new ArrayList<T>();
        } else if (Set.class.equals(type)) {
            collection = new HashSet<T>();
        } else {
            throw new IllegalArgumentException("creation of collection not supported: " + type.getName());
        }
        invokeSet(bean, property, collection);
        return collection;
    }
}

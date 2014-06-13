package org.tools.hqlbuilder.common;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.persistence.Transient;

/**
 * nieuwe relatie wrapper
 * 
 * @author jdlandsh
 */
public class EntityRelationHelper {
    /** bean */
    @Transient
    protected final transient Object bean;

    /**
     * Creates a new RelationWrapper object.
     * 
     * @param bean
     */
    public EntityRelationHelper(Object bean) {
        this.bean = bean;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RelationWrapper[" + bean.getClass().getSimpleName() + "]";
    }

    /**
     * 
     * @see EntityRelationCache#moSet(Object, String, Object, String)
     */
    public <C> void moSet(String property, C target, String backprop) {
        getInstance().moSet(bean, property, target, backprop);
    }

    /**
     * 
     * @see EntityRelationCache#ooSet(Object, String, Object, String)
     */
    public <C> void ooSet(String property, C target, String backprop) {
        getInstance().ooSet(bean, property, target, backprop);
    }

    /**
     * 
     * @see EntityRelationCache#mmClear(Object, String, String)
     */
    public <C> void mmClear(String property, String backprop) {
        getInstance().mmClear(bean, property, backprop);
    }

    /**
     * 
     * @see EntityRelationCache#mmAdd(Object, String, Object, String)
     */
    public <C> void mmAdd(String property, C target, String backprop) {
        if (target == null) {
            return;
        }

        getInstance().mmAdd(bean, property, target, backprop);
    }

    /**
     * 
     * @see EntityRelationCache#mmRemove(Object, String, Object, String)
     */
    public <C> void mmRemove(String property, C target, String backprop) {
        if (target == null) {
            return;
        }

        getInstance().mmRemove(bean, property, target, backprop);
    }

    public <C> void mmSet(String property, Collection<C> targets, String backprop) {
        mmReplace(property, targets, backprop);
    }

    /**
     * 
     * @see EntityRelationCache#mmReplace(Object, String, Collection, String)
     */
    public <C> void mmReplace(String property, Collection<C> targets, String backprop) {
        if (targets == null) {
            return;
        }

        getInstance().mmReplace(bean, property, targets, backprop);
    }

    /**
     * 
     * @see EntityRelationCache#omClear(Object, String, String)
     */
    public <C> void omClear(String property, String backprop) {
        getInstance().omClear(bean, property, backprop);
    }

    /**
     * 
     * @see EntityRelationCache#omRemove(Object, String, Object, String)
     */
    public <C> void omRemove(String property, C target, String backprop) {
        if (target == null) {
            return;
        }

        getInstance().omRemove(bean, property, target, backprop);
    }

    /**
     * 
     * @see EntityRelationCache#omAdd(Object, String, Object, String)
     */
    public <C> void omAdd(String property, C target, String backprop) {
        if (target == null) {
            return;
        }

        getInstance().omAdd(bean, property, target, backprop);
    }

    public <C> void omSet(String property, Collection<C> targets, String backprop) {
        omReplace(property, targets, backprop);
    }

    /**
     * 
     * @see EntityRelationCache#omReplace(Object, String, Collection, String)
     */
    public <C> void omReplace(String property, Collection<C> targets, String backprop) {
        if (targets == null) {
            return;
        }

        getInstance().omReplace(bean, property, targets, backprop);
    }

    /**
     * get cache for this' bean's class
     * 
     * @param <P>
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <P> EntityRelationCache<P> getInstance() {
        return (EntityRelationCache<P>) EntityRelationCache.getInstance(this.bean.getClass());
    }

    /**
     * 
     * @see EntityRelationCache#ooSet(Object, String, Object)
     */
    public <C> void ooSet(String property, C target) {
        getInstance().ooSet(bean, property, target);
    }

    /**
     * 
     * @see EntityRelationCache#mmClear(Object, String)
     */
    public <C> void mmClear(String property) {
        getInstance().mmClear(bean, property);
    }

    /**
     * 
     * @see EntityRelationCache#mmAdd(Object, String, Object)
     */
    public <C> void mmAdd(String property, C target) {
        if (target == null) {
            return;
        }

        getInstance().mmAdd(bean, property, target);
    }

    /**
     * 
     * @see EntityRelationCache#mmRemove(Object, String, Object)
     */
    public <C> void mmRemove(String property, C target) {
        if (target == null) {
            return;
        }

        getInstance().mmRemove(bean, property, target);
    }

    public <C> void mmSet(String property, Collection<C> targets) {
        mmReplace(property, targets);
    }

    /**
     * 
     * @see EntityRelationCache#mmReplace(Object, String, Collection)
     */
    public <C> void mmReplace(String property, Collection<C> targets) {
        if (targets == null) {
            return;
        }

        getInstance().mmReplace(bean, property, targets);
    }

    /**
     * 
     * @see EntityRelationCache#omClear(Object, String)
     */
    public <C> void omClear(String property) {
        getInstance().omClear(bean, property);
    }

    /**
     * 
     * @see EntityRelationCache#omRemove(Object, String, Object)
     */
    public <C> void omRemove(String property, C target) {
        if (target == null) {
            return;
        }

        getInstance().omRemove(bean, property, target);
    }

    /**
     * 
     * @see EntityRelationCache#omAdd(Object, String, Object)
     */
    public <C> void omAdd(String property, C target) {
        if (target == null) {
            return;
        }

        getInstance().omAdd(bean, property, target);
    }

    public <C> void omSet(String property, Collection<C> targets) {
        omReplace(property, targets);
    }

    /**
     * 
     * @see EntityRelationCache#omReplace(Object, String, Collection)
     */
    public <C> void omReplace(String property, Collection<C> targets) {
        if (targets == null) {
            return;
        }

        getInstance().omReplace(bean, property, targets);
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     * 
     * @param <C>
     * @param property
     * @param target
     * @param backprop
     */
    public <C> void add(String property, C target, String backprop) {
        if (target == null) {
            return;
        }

        getInstance().add(bean, property, target, backprop);
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     * 
     * @param <C>
     * @param property
     * @param backprop
     */
    public <C> void clear(String property, String backprop) {
        getInstance().clear(bean, property, backprop);
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     * 
     * @param <C>
     * @param property
     * @param target
     * @param backprop
     */
    public <C> void remove(String property, C target, String backprop) {
        if (target == null) {
            return;
        }

        getInstance().remove(bean, property, target, backprop);
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     * 
     * @param <C>
     * @param property
     * @param targets
     * @param backprop
     */
    public <C> void replace(String property, Collection<C> targets, String backprop) {
        if (targets == null) {
            return;
        }

        getInstance().replace(bean, property, targets, backprop);
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     * 
     * @param <C>
     * @param property
     * @param target
     * @param backprop
     */
    public <C> void set(String property, C target, String backprop) {
        getInstance().set(bean, property, target, backprop);
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     * 
     * @param <C>
     * @param property
     */
    public <C> void clear(String property) {
        getInstance().clear(bean, property);
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     * 
     * @param <C>
     * @param property
     * @param target
     */
    public <C> void remove(String property, C target) {
        if (target == null) {
            return;
        }

        getInstance().remove(bean, property, target);
    }

    /**
     * delegatie naar functie op basis van gevonden relatietype-annotatie
     * 
     * @param <C>
     * @param property
     * @param targets
     */
    public <C> void replace(String property, Collection<C> targets) {
        if (targets == null) {
            return;
        }

        getInstance().replace(bean, property, targets);
    }

    /**
     * single sided relation collection replace
     * 
     * @param property
     */
    public void simpleClear(String property) {
        getCollection(property).clear();
    }

    /**
     * single sided relation collection add
     * 
     * @param <C>
     * @param property
     * @param targets
     */
    @SuppressWarnings("unchecked")
    public <C> void simpleAdd(String property, Collection<C> targets) {
        if (targets == null) {
            return;
        }

        Collection<C> cast = (Collection<C>) getCollection(property);
        cast.addAll(targets);
    }

    /**
     * single sided relation collection add
     * 
     * @param <C>
     * @param property
     * @param target
     */
    @SuppressWarnings("unchecked")
    public <C> void simpleAdd(String property, C target) {
        if (target == null) {
            return;
        }

        Collection<C> cast = (Collection<C>) getCollection(property);
        cast.add(target);
    }

    /**
     * single sided relation collection remove
     * 
     * @param <C>
     * @param property
     * @param targets
     */
    @SuppressWarnings("unchecked")
    public <C> void simpleRemove(String property, Collection<C> targets) {
        if (targets == null) {
            return;
        }

        Collection<C> cast = (Collection<C>) getCollection(property);
        cast.removeAll(targets);
    }

    /**
     * single sided relation collection remove
     * 
     * @param <C>
     * @param property
     * @param target
     */
    @SuppressWarnings("unchecked")
    public <C> void simpleRemove(String property, C target) {
        if (target == null) {
            return;
        }

        Collection<C> cast = (Collection<C>) getCollection(property);
        cast.remove(target);
    }

    /**
     * single sided relation collection replace
     * 
     * @param <C>
     * @param property
     * @param targets
     */
    @SuppressWarnings("unchecked")
    public <C> void simpleReplace(String property, Collection<C> targets) {
        if (targets == null) {
            return;
        }

        Collection<C> cast = (Collection<C>) getCollection(property);
        cast.clear();
        cast.addAll(targets);
    }

    /**
     * single sided relation collection replace
     * 
     * @param <C>
     * @param property
     * @param target
     */
    @SuppressWarnings("unchecked")
    public <C> void simpleReplace(String property, C target) {
        if (target == null) {
            return;
        }

        Collection<C> cast = (Collection<C>) getCollection(property);
        cast.clear();
        cast.add(target);
    }

    /**
     * gets resolved collection via getter
     * 
     * @param property
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Collection<?> getCollection(String property) {
        EntityRelationCache<Object> instance = getInstance();
        return getInstance().resolve(Collection.class.cast(instance.invokeGet(bean, property)));
    }

    /**
     * maak unmodifiable bij return list
     */
    public <T> Set<T> omGet(Set<T> set) {
        return simpleGet(set);
    }

    public <T> Set<T> simpleGet(Set<T> set) {
        return Collections.unmodifiableSet(set);
    }

    /**
     * maak unmodifiable bij return list
     */
    public <T> List<T> omGet(List<T> list) {
        return simpleGet(list);
    }

    public <T> List<T> simpleGet(List<T> list) {
        return Collections.unmodifiableList(list);
    }

    /**
     * maak unmodifiable bij return sortedset
     */
    public <T> SortedSet<T> omGet(SortedSet<T> set) {
        return Collections.unmodifiableSortedSet(set);
    }

    /**
     * maak unmodifiable bij return list
     */
    public <T> Set<T> mmGet(Set<T> set) {
        return Collections.unmodifiableSet(set);
    }

    /**
     * maak unmodifiable bij return list
     */
    public <T> List<T> mmGet(List<T> list) {
        return Collections.unmodifiableList(list);
    }

    /**
     * maak unmodifiable bij return sortedset
     */
    public <T> SortedSet<T> mmGet(SortedSet<T> set) {
        return Collections.unmodifiableSortedSet(set);
    }
}

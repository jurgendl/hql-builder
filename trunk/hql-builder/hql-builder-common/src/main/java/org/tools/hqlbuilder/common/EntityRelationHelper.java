package org.tools.hqlbuilder.common;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.persistence.Transient;

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

    @SuppressWarnings("unchecked")
    public <C extends Collection<E>, E> C simpleGet(String property, C collection) {
        if (collection == null) {
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

    public void simpleClear(String property) {
        getCollection(property).clear();
    }

    @SuppressWarnings("unchecked")
    public <T> void simpleAdd(String property, T target) {
        ((Collection<T>) getCollection(property)).add(target);
    }

    @SuppressWarnings("unchecked")
    public <T> void simpleRemove(String property, T target) {
        ((Collection<T>) getCollection(property)).remove(target);
    }

    private Collection<?> getCollection(String property) {
        return getInstance().getOrCreateCollection(bean, property);
    }

    public <T> void simpleSet(String property, T target) {
        getInstance().invokeSet(bean, property, target);
    }

    @SuppressWarnings("unchecked")
    public <T> void simpleSet(String property, Collection<T> targets) {
        Collection<T> cast = (Collection<T>) getCollection(property);
        cast.clear();
        if (targets != null) {
            cast.addAll(targets);
        }
    }

    public <T> T simpleGet(T target) {
        return target;
    }
}

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

    public <T> Set<T> omGet(Set<T> set) {
        return simpleGet(set);
    }

    public <T> List<T> omGet(List<T> list) {
        return simpleGet(list);
    }

    public <T> Set<T> simpleGet(Set<T> set) {
        return set == null ? null : Collections.unmodifiableSet(set);
    }

    public <T> List<T> simpleGet(List<T> list) {
        return list == null ? null : Collections.unmodifiableList(list);
    }

    public <T> SortedSet<T> omGet(SortedSet<T> set) {
        return set == null ? null : Collections.unmodifiableSortedSet(set);
    }

    public <T> Set<T> mmGet(Set<T> set) {
        return set == null ? null : Collections.unmodifiableSet(set);
    }

    public <T> List<T> mmGet(List<T> list) {
        return list == null ? null : Collections.unmodifiableList(list);
    }

    public <T> SortedSet<T> mmGet(SortedSet<T> set) {
        return set == null ? null : Collections.unmodifiableSortedSet(set);
    }

    public void simpleClear(String property) {
        getCollection(property).clear();
    }

    @SuppressWarnings("unchecked")
    public <T> void simpleAdd(String property, Collection<T> targets) {
        ((Collection<T>) getCollection(property)).addAll(targets);
    }

    @SuppressWarnings("unchecked")
    public <T> void simpleAdd(String property, T target) {
        ((Collection<T>) getCollection(property)).add(target);
    }

    @SuppressWarnings("unchecked")
    public <T> void simpleRemove(String property, Collection<T> targets) {
        ((Collection<T>) getCollection(property)).removeAll(targets);
    }

    @SuppressWarnings("unchecked")
    public <T> void simpleRemove(String property, T target) {
        ((Collection<T>) getCollection(property)).remove(target);
    }

    @SuppressWarnings("unchecked")
    private Collection<?> getCollection(String property) {
        return getInstance().resolve(Collection.class.cast(getInstance().invokeGet(bean, property)));
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

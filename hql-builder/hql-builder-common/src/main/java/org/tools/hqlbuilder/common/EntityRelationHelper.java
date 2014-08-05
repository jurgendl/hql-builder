package org.tools.hqlbuilder.common;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "unchecked", "unused" })
public class EntityRelationHelper {
    @Transient
    private final transient Object bean;

    @Transient
    private final transient Logger logger;

    public EntityRelationHelper(Object bean) {
        this.bean = bean;
        this.logger = LoggerFactory.getLogger(toString());
    }

    @Override
    public String toString() {
        return "RelationWrapper[" + bean.getClass().getSimpleName() + "]";
    }

    private <C> void moSet(String property, C target, String backprop) {
        getInstance().moSet(bean, property, target, backprop);
    }

    public <C> void moSet(String property, C target) {
        getInstance().moSet(bean, property, target);
    }

    private <C> void ooSet(String property, C target, String backprop) {
        getInstance().ooSet(bean, property, target, backprop);
    }

    private <C> void mmClear(String property, String backprop) {
        getInstance().mmClear(bean, property, backprop);
    }

    // private <C> void mmAdd(String property, C target, String backprop) {
    // if (target == null) {
    // logger.warn("not permitted to mm.add " + property + " <null>");
    // return;
    // }
    // getInstance().mmAdd(bean, property, target, backprop);
    // }

    // private <C> void mmRemove(String property, C target, String backprop) {
    // if (target == null) {
    // logger.warn("not permitted to mm.remove " + property + " <null>");
    // return;
    // }
    // getInstance().mmRemove(bean, property, target, backprop);
    // }

    // private <C> void mmSet(String property, Collection<C> targets, String backprop) {
    // if (targets == null) {
    // logger.warn("not permitted to mm.clear " + property + " <null>");
    // return;
    // }
    // getInstance().mmReplace(bean, property, targets, backprop);
    // }

    // private <C> void omClear(String property, String backprop) {
    // getInstance().omClear(bean, property, backprop);
    // }

    // private <C> void omRemove(String property, C target, String backprop) {
    // if (target == null) {
    // logger.warn("not permitted to om.remove " + property + " <null>");
    // return;
    // }
    // getInstance().omRemove(bean, property, target, backprop);
    // }

    // private <C> void omAdd(String property, C target, String backprop) {
    // if (target == null) {
    // logger.warn("not permitted to om.add " + property + " <null>");
    // return;
    // }
    // getInstance().omAdd(bean, property, target, backprop);
    // }

    // private <C> void omSet(String property, Collection<C> targets, String backprop) {
    // if (targets == null) {
    // logger.warn("not permitted to om.set " + property + " <null>");
    // return;
    // }
    // getInstance().omReplace(bean, property, targets, backprop);
    // }

    private <P> EntityRelationCache<P> getInstance() {
        return (EntityRelationCache<P>) EntityRelationCache.getInstance(this.bean.getClass());
    }

    public <C> void ooSet(String property, C target) {
        getInstance().ooSet(bean, property, target);
    }

    public <C> void mmClear(String property) {
        getInstance().mmClear(bean, property);
    }

    public <C> void mmAdd(String property, C target) {
        if (target == null) {
            logger.warn("not permitted to mm.add " + property + " <null>");
            return;
        }
        getInstance().mmAdd(bean, property, target);
    }

    public <C> void mmRemove(String property, C target) {
        if (target == null) {
            logger.warn("not permitted to mm.remove " + property + " <null>");
            return;
        }
        getInstance().mmRemove(bean, property, target);
    }

    public <C> void mmSet(String property, Collection<C> targets) {
        if (targets == null) {
            logger.warn("not permitted to mm.set " + property + " <null>");
            return;
        }
        getInstance().mmReplace(bean, property, targets);
    }

    public <C> void omClear(String property) {
        getInstance().omClear(bean, property);
    }

    public <C> void omRemove(String property, C target) {
        if (target == null) {
            logger.warn("not permitted to om.remove " + property + " <null>");
            return;
        }
        getInstance().omRemove(bean, property, target);
    }

    public <C> void omAdd(String property, C target) {
        if (target == null) {
            logger.warn("not permitted to om.add " + property + " <null>");
            return;
        }
        getInstance().omAdd(bean, property, target);
    }

    public <C> void omSet(String property, Collection<C> targets) {
        if (targets == null) {
            logger.warn("not permitted to om.set " + property + " <null>");
            return;
        }
        getInstance().omReplace(bean, property, targets);
    }

    // private <C> void add(String property, C target, String backprop) {
    // if (target == null) {
    // logger.warn("not permitted to add " + property + " <null>");
    // return;
    // }
    // getInstance().add(bean, property, target, backprop);
    // }

    // private <C> void clear(String property, String backprop) {
    // getInstance().clear(bean, property, backprop);
    // }

    // private <C> void remove(String property, C target, String backprop) {
    // if (target == null) {
    // logger.warn("not permitted to remove " + property + " <null>");
    // return;
    // }
    // getInstance().remove(bean, property, target, backprop);
    // }

    // private <C> void replace(String property, Collection<C> targets, String backprop) {
    // if (targets == null) {
    // logger.warn("not permitted to replace " + property + " <null>");
    // return;
    // }
    // getInstance().replace(bean, property, targets, backprop);
    // }

    // private <C> void set(String property, C target, String backprop) {
    // getInstance().set(bean, property, target, backprop);
    // }

    private <C> void clear(String property) {
        getInstance().clear(bean, property);
    }

    private <C> void remove(String property, C target) {
        if (target == null) {
            logger.warn("not permitted to remove " + property + " <null>");
            return;
        }
        getInstance().remove(bean, property, target);
    }

    private <C> void replace(String property, Collection<C> targets) {
        if (targets == null) {
            logger.warn("not permitted to replace " + property + " <null>");
            return;
        }
        getInstance().replace(bean, property, targets);
    }

    private void simpleClear(String property) {
        getCollection(property).clear();
    }

    private <C> void simpleAdd(String property, Collection<C> targets) {
        if (targets == null) {
            logger.warn("not permitted to add " + property + " <null>");
            return;
        }
        Collection<C> cast = (Collection<C>) getCollection(property);
        cast.addAll(targets);
    }

    private <C> void simpleAdd(String property, C target) {
        if (target == null) {
            logger.warn("not permitted to add " + property + " <null>");
            return;
        }
        Collection<C> cast = (Collection<C>) getCollection(property);
        cast.add(target);
    }

    private <C> void simpleRemove(String property, Collection<C> targets) {
        if (targets == null) {
            logger.warn("not permitted to remove " + property + " <null>");
            return;
        }
        Collection<C> cast = (Collection<C>) getCollection(property);
        cast.removeAll(targets);
    }

    private <C> void simpleRemove(String property, C target) {
        if (target == null) {
            logger.warn("not permitted to remove " + property + " <null>");
            return;
        }
        Collection<C> cast = (Collection<C>) getCollection(property);
        cast.remove(target);
    }

    private <C> void simpleReplace(String property, Collection<C> targets) {
        if (targets == null) {
            logger.warn("not permitted to replace " + property + " <null>");
            return;
        }
        Collection<C> cast = (Collection<C>) getCollection(property);
        cast.clear();
        cast.addAll(targets);
    }

    private <C> void simpleReplace(String property, C target) {
        if (target == null) {
            logger.warn("not permitted to replace " + property + " <null>");
            return;
        }
        Collection<C> cast = (Collection<C>) getCollection(property);
        cast.clear();
        cast.add(target);
    }

    private Collection<?> getCollection(String property) {
        EntityRelationCache<Object> instance = getInstance();
        return getInstance().resolve(Collection.class.cast(instance.invokeGet(bean, property)));
    }

    public <T> Set<T> omGet(Set<T> set) {
        return simpleGet(set);
    }

    private <T> Set<T> simpleGet(Set<T> set) {
        return set == null ? null : Collections.unmodifiableSet(set);
    }

    public <T> List<T> omGet(List<T> list) {
        return simpleGet(list);
    }

    private <T> List<T> simpleGet(List<T> list) {
        return list == null ? null : Collections.unmodifiableList(list);
    }

    private <T> SortedSet<T> omGet(SortedSet<T> set) {
        return set == null ? null : Collections.unmodifiableSortedSet(set);
    }

    public <T> Set<T> mmGet(Set<T> set) {
        return set == null ? null : Collections.unmodifiableSet(set);
    }

    private <T> List<T> mmGet(List<T> list) {
        return list == null ? null : Collections.unmodifiableList(list);
    }

    private <T> SortedSet<T> mmGet(SortedSet<T> set) {
        return set == null ? null : Collections.unmodifiableSortedSet(set);
    }
}

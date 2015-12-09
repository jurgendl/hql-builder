package org.tools.hqlbuilder.common;

import java.util.Collection;

public interface CheckedEntityRelationHelper<O> {

    <T> void simpleSet(String property, Collection<T> targets);

    <T> void simpleSet(String property, T target);

    <T> void simpleRemove(String property, T target);

    <T> void simpleAdd(String property, T target);

    <T> void simpleClear(String property);

    <C extends Collection<E>, E> C mmGet(C collection);

    <C extends Collection<E>, E> C mmGet(String property, C collection);

    <C extends Collection<E>, E> C simpleGet(String property, C collection);

    <C extends Collection<E>, E> C simpleGet(C collection);

    <C extends Collection<E>, E> C omGet(C collection);

    <C extends Collection<E>, E> C omGet(String property, C collection);

    <T> void omSet(String property, Collection<T> targets);

    <T> void omAdd(String property, T target);

    <T> void omRemove(String property, T target);

    <T> void omClear(String property);

    <T> void mmSet(String property, Collection<T> targets);

    <T> void mmRemove(String property, T target);

    <T> void mmAdd(String property, T target);

    <T> void mmClear(String property);

    <T> void ooSet(String property, T target);

    <T> void moSet(String property, T target);

}

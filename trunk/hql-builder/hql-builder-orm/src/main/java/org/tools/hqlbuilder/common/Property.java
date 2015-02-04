package org.tools.hqlbuilder.common;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class Property<T, P> {
    protected final PropertyDescriptor descriptor;

    public Property(PropertyDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @SuppressWarnings("unchecked")
    public Class<P> type() {
        return (Class<P>) this.descriptor.getPropertyType();
    }

    public String name() {
        return this.descriptor.getName();
    }

    public void write(T obj, P value) {
        try {
            descriptor.getWriteMethod().invoke(obj, new Object[] { value });
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(descriptor.getPropertyType().getName() + "#" + descriptor.getName(), ex.getTargetException());
        } catch (IllegalAccessException | IllegalArgumentException | ClassCastException ex) {
            throw new RuntimeException(descriptor.getPropertyType().getName() + "#" + descriptor.getName(), ex);
        }
    }

    @SuppressWarnings("unchecked")
    public P read(T obj) {
        try {
            return (P) descriptor.getReadMethod().invoke(obj);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(descriptor.getPropertyType().getName() + "#" + descriptor.getName(), ex.getTargetException());
        } catch (IllegalAccessException | IllegalArgumentException | ClassCastException ex) {
            throw new RuntimeException(descriptor.getPropertyType().getName() + "#" + descriptor.getName(), ex);
        }
    }
}

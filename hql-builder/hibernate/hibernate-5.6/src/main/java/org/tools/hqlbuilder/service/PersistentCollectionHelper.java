package org.tools.hqlbuilder.service;

import org.hibernate.collection.spi.PersistentCollection;

public class PersistentCollectionHelper {
    public boolean instanceOf(Object o) {
        return o != null && PersistentCollection.class.isAssignableFrom(o.getClass());
    }

    public boolean wasInitialized(Object o) {
        return ((PersistentCollection) o).wasInitialized();
    }
}

package org.tools.hqlbuilder.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.persistence.Transient;

public abstract class ERHAdapter implements Serializable {
    private static final long serialVersionUID = 1068663389042116818L;

    @Transient
    protected transient EntityRelationHelper<? extends ERHAdapter> erh;

    public ERHAdapter() {
        initERH();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        initERH();
    }

    protected void initERH() {
        erh = new EntityRelationHelper<>(this);
    }
}

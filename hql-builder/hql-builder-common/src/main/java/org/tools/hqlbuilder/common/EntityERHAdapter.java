package org.tools.hqlbuilder.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class EntityERHAdapter extends EntityAdapter {
    private static final long serialVersionUID = -7883758465742148428L;

    @Transient
    protected transient EntityRelationHelper<? extends EntityERHAdapter> erh;

    public EntityERHAdapter(Long id, Integer version) {
        super(id, version);
        initERH();
    }

    public EntityERHAdapter(Long id) {
        super(id);
        initERH();
    }

    public EntityERHAdapter() {
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

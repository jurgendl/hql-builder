package org.tools.hqlbuilder.common;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class EntityExtAdapter extends EntityAdapter {
    private static final long serialVersionUID = -7883758465742148428L;

    @Transient
    protected transient final EntityRelationHelper erh;

    public EntityExtAdapter(Long id, Integer version) {
        super(id, version);
        erh = new EntityRelationHelper(this);
    }

    public EntityExtAdapter(Long id) {
        super(id);
        erh = new EntityRelationHelper(this);
    }

    public EntityExtAdapter() {
        erh = new EntityRelationHelper(this);
    }

    protected EntityRelationHelper getErh() {
        return this.erh;
    }
}

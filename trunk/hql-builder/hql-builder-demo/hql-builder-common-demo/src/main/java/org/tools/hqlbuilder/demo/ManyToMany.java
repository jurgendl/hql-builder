package org.tools.hqlbuilder.demo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@Entity
public class ManyToMany extends EntityERHAdapter {
    private static final long serialVersionUID = 2051664587602353615L;

    public static final String MANY_TO_MANY_BACK = "manyToManyBack";

    @javax.persistence.ManyToMany
    private Set<ManyToManyBack> manyToManyBack = new HashSet<ManyToManyBack>();

    public Set<ManyToManyBack> getManyToManyBack() {
        return erh.mmGet(this.manyToManyBack);
    }

    public void setManyToManyBack(Set<ManyToManyBack> manyToManyBack) {
        erh.mmSet(MANY_TO_MANY_BACK, manyToManyBack);
    }

    public void addManyToManyBack(@SuppressWarnings("hiding") ManyToManyBack manyToManyBack) {
        erh.mmAdd(MANY_TO_MANY_BACK, manyToManyBack);
    }

    public void removeManyToManyBack(@SuppressWarnings("hiding") ManyToManyBack manyToManyBack) {
        erh.mmRemove(MANY_TO_MANY_BACK, manyToManyBack);
    }

    public void clearManyToManyBack() {
        erh.mmClear(MANY_TO_MANY_BACK);
    }
}

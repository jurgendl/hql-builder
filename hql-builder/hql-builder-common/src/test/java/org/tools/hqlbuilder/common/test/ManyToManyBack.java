package org.tools.hqlbuilder.common.test;

import java.util.Set;

import javax.persistence.Entity;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@Entity
public class ManyToManyBack extends EntityERHAdapter {
    private static final long serialVersionUID = 4710114529721854010L;

    public static final String MANY_TO_MANY = "manyToMany";

    @javax.persistence.ManyToMany(mappedBy = ManyToMany.MANY_TO_MANY_BACK)
    private Set<ManyToMany> manyToMany;

    public Set<ManyToMany> getManyToMany() {
        return erh.mmGet(MANY_TO_MANY, this.manyToMany);
    }

    public void setManyToMany(Set<ManyToMany> manyToMany) {
        erh.mmSet(MANY_TO_MANY, manyToMany);
    }

	public void addManyToMany(ManyToMany manyToMany) {
        erh.mmAdd(MANY_TO_MANY, manyToMany);
    }

	public void removeManyToMany(ManyToMany manyToMany) {
        erh.mmRemove(MANY_TO_MANY, manyToMany);
    }

    public void clearManyToMany() {
        erh.mmClear(MANY_TO_MANY);
    }
}

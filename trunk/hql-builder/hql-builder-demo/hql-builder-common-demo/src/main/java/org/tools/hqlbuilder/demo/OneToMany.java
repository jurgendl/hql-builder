package org.tools.hqlbuilder.demo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@Entity
public class OneToMany extends EntityERHAdapter {
    private static final long serialVersionUID = -3720856012355844474L;

    public static final String MANY_TO_ONE = "manyToOne";

    @javax.persistence.OneToMany(mappedBy = ManyToOne.ONE_TO_MANY)
    private Set<ManyToOne> manyToOne = new HashSet<ManyToOne>();

    public Set<ManyToOne> getManyToOne() {
        return erh.omGet(this.manyToOne);
    }

    public void setManyToOne(Set<ManyToOne> manyToOne) {
        erh.omSet(MANY_TO_ONE, manyToOne);
    }

    public void addManyToOne(@SuppressWarnings("hiding") ManyToOne manyToOne) {
        erh.omAdd(MANY_TO_ONE, manyToOne);
    }

    public void removeManyToOne(@SuppressWarnings("hiding") ManyToOne manyToOne) {
        erh.omRemove(MANY_TO_ONE, manyToOne);
    }

    public void clearFromManyToOne() {
        erh.omClear(MANY_TO_ONE);
    }
}

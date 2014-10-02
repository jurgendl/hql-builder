package org.tools.hqlbuilder.common.test;

import java.util.SortedSet;

import javax.persistence.Entity;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@Entity
public class WithSortedSet extends EntityERHAdapter {
    private static final long serialVersionUID = 5034381888251598780L;

    public static final String DUMMY = "dummy";

    @javax.persistence.OneToMany
    private SortedSet<Dummy> dummy;

    public SortedSet<Dummy> getDummy() {
        return erh.simpleGet(DUMMY, this.dummy);
    }

    public void setDummy(SortedSet<Dummy> dummy) {
        erh.simpleSet(DUMMY, dummy);
    }
}

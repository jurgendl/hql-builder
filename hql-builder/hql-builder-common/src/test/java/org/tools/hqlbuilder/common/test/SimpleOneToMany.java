package org.tools.hqlbuilder.common.test;

import java.util.List;

import javax.persistence.Entity;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@Entity
public class SimpleOneToMany extends EntityERHAdapter {
    private static final long serialVersionUID = -2654878017323851159L;

    public static final String DUMMY = "dummy";

    @javax.persistence.OneToMany
    private List<Dummy> dummy;

    public List<Dummy> getDummy() {
        return erh.simpleGet(DUMMY, this.dummy);
    }

    public void setDummy(List<Dummy> dummy) {
        erh.simpleSet(DUMMY, dummy);
    }

	public void addDummy(Dummy dummy) {
        erh.simpleAdd(DUMMY, dummy);
    }

	public void removeDummy(Dummy dummy) {
        erh.simpleRemove(DUMMY, dummy);
    }

    public void clearDummy() {
        erh.simpleClear(DUMMY);
    }
}

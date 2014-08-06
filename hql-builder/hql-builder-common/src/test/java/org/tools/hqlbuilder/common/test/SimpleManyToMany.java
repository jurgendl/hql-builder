package org.tools.hqlbuilder.common.test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@Entity
public class SimpleManyToMany extends EntityERHAdapter {
    private static final long serialVersionUID = 8366756550189602946L;

    public static final String DUMMY = "dummy";

    @javax.persistence.ManyToMany
    private List<Dummy> dummy = new ArrayList<Dummy>();

    public List<Dummy> getDummy() {
        return erh.simpleGet(this.dummy);
    }

    public void setDummy(List<Dummy> dummy) {
        erh.simpleSet(DUMMY, dummy);
    }

    public void addDummy(@SuppressWarnings("hiding") Dummy dummy) {
        erh.simpleAdd(DUMMY, dummy);
    }

    public void removeDummy(@SuppressWarnings("hiding") Dummy dummy) {
        erh.simpleRemove(DUMMY, dummy);
    }

    public void clearDummy() {
        erh.simpleClear(DUMMY);
    }
}

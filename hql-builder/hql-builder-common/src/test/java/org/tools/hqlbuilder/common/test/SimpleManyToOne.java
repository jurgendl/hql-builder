package org.tools.hqlbuilder.common.test;

import javax.persistence.Entity;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@Entity
public class SimpleManyToOne extends EntityERHAdapter {
    private static final long serialVersionUID = 3547702006780488968L;

    public static final String DUMMY = "dummy";

    @javax.persistence.ManyToOne
    private Dummy dummy;

    public Dummy getDummy() {
        return dummy;
    }

    public void setDummy(Dummy dummy) {
        erh.simpleSet(DUMMY, dummy);
    }
}

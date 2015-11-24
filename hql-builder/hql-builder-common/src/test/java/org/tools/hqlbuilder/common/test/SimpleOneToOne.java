package org.tools.hqlbuilder.common.test;

import javax.persistence.Entity;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@Entity
public class SimpleOneToOne extends EntityERHAdapter {
    private static final long serialVersionUID = -110678454900979474L;

    public static final String DUMMY = "dummy";

    @javax.persistence.OneToOne
    private Dummy dummy;

    public Dummy getDummy() {
        return dummy;
    }

    public void setDummy(Dummy dummy) {
        erh.simpleSet(DUMMY, dummy);
    }
}

package org.tools.hqlbuilder.common.test;

import javax.persistence.Entity;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@Entity
public class OneToMany extends EntityERHAdapter {
    private static final long serialVersionUID = -5362556402940342734L;

    public static final String MANY_TO_ONE = "manyToOne";

    @javax.persistence.ManyToOne
    private ManyToOne manyToOne;

    public ManyToOne getManyToOne() {
        return this.manyToOne;
    }

    public void setManyToOne(ManyToOne manyToOne) {
        erh.moSet(MANY_TO_ONE, manyToOne);
    }
}

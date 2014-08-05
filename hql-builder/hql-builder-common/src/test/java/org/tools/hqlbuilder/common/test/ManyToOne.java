package org.tools.hqlbuilder.common.test;

import javax.persistence.Entity;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@Entity
public class ManyToOne extends EntityERHAdapter {
    private static final long serialVersionUID = -5362556402940342734L;

    public static final String ONE_TO_MANY = "oneToMany";

    @javax.persistence.ManyToOne
    private OneToMany oneToMany;

    public OneToMany getOneToMany() {
        return this.oneToMany;
    }

    public void setOneToMany(OneToMany oneToMany) {
        erh.moSet(ONE_TO_MANY, oneToMany);
    }
}

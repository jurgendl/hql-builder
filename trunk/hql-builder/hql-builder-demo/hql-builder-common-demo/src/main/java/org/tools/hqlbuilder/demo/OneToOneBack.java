package org.tools.hqlbuilder.demo;

import javax.persistence.Entity;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@Entity
public class OneToOneBack extends EntityERHAdapter {
    private static final long serialVersionUID = -988032054892003382L;

    public static final String ONE_TO_ONE = "oneToOne";

    @javax.persistence.OneToOne(mappedBy = OneToOne.ONE_TO_ONE_BACK)
    private OneToOne oneToOne;

    public OneToOne getOneToOne() {
        return this.oneToOne;
    }

    public void setOneToOne(OneToOne oneToOne) {
        erh.ooSet(ONE_TO_ONE, oneToOne);
    }
}

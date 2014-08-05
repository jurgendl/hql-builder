package org.tools.hqlbuilder.demo;

import javax.persistence.Entity;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@Entity
public class OneToOne extends EntityERHAdapter {
    private static final long serialVersionUID = -7320340985709816871L;

    public static final String ONE_TO_ONE_BACK = "oneToOneBack";

    @javax.persistence.OneToOne
    private OneToOneBack oneToOneBack;

    public OneToOneBack getOneToOneBack() {
        return this.oneToOneBack;
    }

    public void setOneToOneBack(OneToOneBack oneToOneBack) {
        erh.ooSet(ONE_TO_ONE_BACK, oneToOneBack);
    }
}

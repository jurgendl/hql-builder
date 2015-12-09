package org.tools.hqlbuilder.common.test;

import javax.persistence.ManyToOne;

import org.tools.hqlbuilder.common.EntityERHAdapter;

public class FromMany extends EntityERHAdapter {
    private static final long serialVersionUID = -1147803436741911619L;

    @ManyToOne
    private ToMany toMany;

    public ToMany getToMany() {
        return toMany;
    }

    public void setToMany(ToMany toMany) {
        erh.set("toMany", toMany);
    }
}

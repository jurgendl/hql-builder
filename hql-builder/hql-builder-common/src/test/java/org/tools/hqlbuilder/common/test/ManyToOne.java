package org.tools.hqlbuilder.common.test;

import java.util.Set;

import javax.persistence.Entity;

import org.tools.hqlbuilder.common.EntityERHAdapter;

@Entity
public class ManyToOne extends EntityERHAdapter {
    private static final long serialVersionUID = -3720856012355844474L;

    public static final String ONE_TO_MANY = "oneToMany";

    @javax.persistence.OneToMany(mappedBy = OneToMany.MANY_TO_ONE)
    private Set<OneToMany> oneToMany;

    public Set<OneToMany> getOneToMany() {
        return erh.omGet(ONE_TO_MANY, this.oneToMany);
    }

    public void setOneToMany(Set<OneToMany> oneToMany) {
        erh.omSet(ONE_TO_MANY, oneToMany);
    }

    public void addOneToMany(OneToMany oneToMany) {
        erh.omAdd(ONE_TO_MANY, oneToMany);
    }

    public void removeOneToMany(OneToMany oneToMany) {
        erh.omRemove(ONE_TO_MANY, oneToMany);
    }

    public void clearOneToMany() {
        erh.omClear(ONE_TO_MANY);
    }
}

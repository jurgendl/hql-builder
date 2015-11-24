package org.tools.hqlbuilder.common.test;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class IllegalField extends Dummy {
    private static final long serialVersionUID = -8800195748421510599L;

    @OneToOne
    private Dummy dummy;

    @OneToMany
    private Set<Dummy> dummys;

    public Dummy getDummy() {
        return dummy;
    }

    public void setDummy(Dummy dummy) {
        erh.simpleSet("illegal", dummy);
    }

    public Set<Dummy> getDummys() {
        return erh.omGet("illegal", dummys);
    }

    public void setDummys(Set<Dummy> dummys) {
        erh.omSet("illegal", dummys);
    }
}

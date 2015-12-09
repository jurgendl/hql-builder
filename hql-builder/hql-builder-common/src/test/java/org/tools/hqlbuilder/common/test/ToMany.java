package org.tools.hqlbuilder.common.test;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.OneToMany;

import org.tools.hqlbuilder.common.EntityERHAdapter;

public class ToMany extends EntityERHAdapter {
    private static final long serialVersionUID = -1858299801712283375L;

    @OneToMany
    private Set<FromMany> unidirectional = new HashSet<>();

    @OneToMany(mappedBy = "toMany")
    private Set<FromMany> bidirectional = new HashSet<>();

    public Set<FromMany> getUnidirectional() {
        return erh.get(unidirectional);
    }

    public void setUnidirectional(Set<FromMany> unidirectional) {
        erh.set("unidirectional", unidirectional);
    }

    public void addUnidirectional(FromMany unidirectional) {
        erh.add("unidirectional", unidirectional);
    }

    public void removeUnidirectional(FromMany unidirectional) {
        erh.remove("unidirectional", unidirectional);
    }

    public Set<FromMany> getBidirectional() {
        return erh.get(bidirectional);
    }

    public void setBidirectional(Set<FromMany> bidirectional) {
        erh.set("bidirectional", bidirectional);
    }

    public void addBidirectional(FromMany bidirectional) {
        erh.add("bidirectional", bidirectional);
    }

    public void removeBidirectional(FromMany bidirectional) {
        erh.remove("bidirectional", bidirectional);
    }
}

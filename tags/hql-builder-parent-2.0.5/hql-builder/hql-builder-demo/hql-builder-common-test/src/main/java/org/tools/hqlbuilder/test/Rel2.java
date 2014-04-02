package org.tools.hqlbuilder.test;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.AccessType;

@XmlRootElement
@Entity
@AccessType("field")
public class Rel2 extends Parent {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Rel1Id")
    private Rel1 rel1;

    private String rel2 = "rel2";

    @OneToMany(mappedBy = "rel2")
    private Set<Rel2Trans> trans = new HashSet<Rel2Trans>();

    public Set<Rel2Trans> getTrans() {
        return trans;
    }

    public void setTrans(Set<Rel2Trans> trans) {
        this.trans = trans;
    }

    public Rel1 getRel1() {
        return rel1;
    }

    public void setRel1(Rel1 rel1) {
        this.rel1 = rel1;
    }

    public String getRel2() {
        return rel2;
    }

    public void setRel2(String rel2) {
        this.rel2 = rel2;
    }

    @Override
    public String toString() {
        return rel2;
    }
}

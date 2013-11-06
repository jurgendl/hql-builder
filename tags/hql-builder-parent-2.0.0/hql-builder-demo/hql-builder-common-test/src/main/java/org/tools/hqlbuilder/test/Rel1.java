package org.tools.hqlbuilder.test;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.AccessType;

@Entity
@AccessType("field")
public class Rel1 extends Parent {
    @OneToOne
    private Pojo pojo;

    private String rel1 = "rel1";

    @OneToMany(mappedBy = "rel1", fetch = FetchType.LAZY)
    private Set<Rel2> rel2 = new HashSet<Rel2>();

    @OneToMany(mappedBy = "rel1", fetch = FetchType.LAZY)
    private Set<Rel1Trans> trans = new HashSet<Rel1Trans>();

    public Set<Rel1Trans> getTrans() {
        return trans;
    }

    public void setTrans(Set<Rel1Trans> trans) {
        this.trans = trans;
    }

    public Set<Rel2> getRel2() {
        return rel2;
    }

    public void setRel2(Set<Rel2> rel2) {
        this.rel2 = rel2;
    }

    public Pojo getPojo() {
        return pojo;
    }

    public void setPojo(Pojo pojo) {
        this.pojo = pojo;
    }

    public String getRel1() {
        return rel1;
    }

    public void setRel1(String rel1) {
        this.rel1 = rel1;
    }

    @Override
    public String toString() {
        return rel1;
    }
}

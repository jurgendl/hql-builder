package org.tools.hqlbuilder.test;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.AccessType;

@XmlRootElement
@Entity
@AccessType("field")
public class Rel0 extends Parent {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Rel1Id")
    private Rel1 rel1;

    private String rel0 = "rel0";

    public Rel1 getRel1() {
        return this.rel1;
    }

    public void setRel1(Rel1 rel1) {
        this.rel1 = rel1;
    }

    @Override
    public String toString() {
        return rel0;
    }

    public String getRel0() {
        return this.rel0;
    }

    public void setRel0(String rel0) {
        this.rel0 = rel0;
    }
}

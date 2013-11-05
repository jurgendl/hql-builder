package org.tools.hqlbuilder.test;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.AccessType;

@Entity
@AccessType("field")
public class Rel2Trans extends Parent {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Rel2Id")
    private Rel2 rel2;

    private String rel2trans = "rel2trans";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LangId")
    private Lang lang;

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public Rel2 getRel2() {
        return rel2;
    }

    public void setRel2(Rel2 rel2) {
        this.rel2 = rel2;
    }

    public String getRel2trans() {
        return rel2trans;
    }

    public void setRel2trans(String rel2trans) {
        this.rel2trans = rel2trans;
    }

    @Override
    public String toString() {
        return rel2trans;
    }
}

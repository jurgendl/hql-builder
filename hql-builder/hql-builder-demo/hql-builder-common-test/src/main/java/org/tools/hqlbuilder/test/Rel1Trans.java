package org.tools.hqlbuilder.test;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.AccessType;

@Entity
@AccessType("field")
public class Rel1Trans extends Parent {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Rel1Id")
    private Rel1 rel1;

    private String rel1trans = "rel1trans";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LangId")
    private Lang lang;

    public Rel1 getRel1() {
        return rel1;
    }

    public void setRel1(Rel1 rel1) {
        this.rel1 = rel1;
    }

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public String getRel1trans() {
        return rel1trans;
    }

    public void setRel1trans(String rel1trans) {
        this.rel1trans = rel1trans;
    }

    @Override
    public String toString() {
        return rel1trans;
    }
}

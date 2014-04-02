package org.tools.hqlbuilder.test;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.AccessType;

@XmlRootElement
@Entity
@AccessType("field")
public class Lang extends Parent {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}

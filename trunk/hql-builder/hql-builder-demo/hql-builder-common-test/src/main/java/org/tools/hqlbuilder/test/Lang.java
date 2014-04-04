package org.tools.hqlbuilder.test;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Lang extends Parent {
    private static final long serialVersionUID = 762036304624420098L;

    private String code;

    public Lang(String code) {
        this.code = code;
    }

    public Lang() {
        super();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return getCode();
    }
}

package org.tools.hqlbuilder.demo;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import org.tools.hqlbuilder.common.EntityExtAdapter;

@XmlRootElement
@Entity
public class Lang extends EntityExtAdapter implements LangProperties {
    private static final long serialVersionUID = 762036304624420098L;

    private String code;

    public Lang(String code) {
        setCode(code);
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

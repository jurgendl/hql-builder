package org.tools.hqlbuilder.test;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public class PojoSuper extends Parent {
    @NotNull
    private String superNotNull;

    public String getSuperNotNull() {
        return superNotNull;
    }

    public void setSuperNotNull(String superNotNull) {
        this.superNotNull = superNotNull;
    }

    @Override
    public String toString() {
        return "PojoSuper [superNotNull=" + superNotNull + "]";
    }
}

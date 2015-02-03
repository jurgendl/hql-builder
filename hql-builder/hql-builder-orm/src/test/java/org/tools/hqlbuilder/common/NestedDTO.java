package org.tools.hqlbuilder.common;

import org.apache.commons.lang.builder.ToStringBuilder;

public class NestedDTO {
    private String nestedVeld;

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("nestedVeld", nestedVeld).toString();
    }

    public String getNestedVeld() {
        return this.nestedVeld;
    }

    public void setNestedVeld(String nestedVeld) {
        this.nestedVeld = nestedVeld;
    }
}

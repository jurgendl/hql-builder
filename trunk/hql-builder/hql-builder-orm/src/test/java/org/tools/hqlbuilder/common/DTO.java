package org.tools.hqlbuilder.common;

import org.apache.commons.lang.builder.ToStringBuilder;

public class DTO {
    private String veld1;

    private Integer veld2;

    private Object veld3;

    private String nestedDTOVeld;

    private NestedDTO nestedDTO;

    public String getVeld1() {
        return this.veld1;
    }

    public Integer getVeld2() {
        return this.veld2;
    }

    public Object getVeld3() {
        return this.veld3;
    }

    public void setVeld1(String veld1) {
        this.veld1 = veld1;
    }

    public void setVeld2(Integer veld2) {
        this.veld2 = veld2;
    }

    public void setVeld3(Object veld3) {
        this.veld3 = veld3;
    }

    public String getNestedDTOVeld() {
        return this.nestedDTOVeld;
    }

    public NestedDTO getNestedDTO() {
        return this.nestedDTO;
    }

    public void setNestedDTOVeld(String nestedDTOVeld) {
        this.nestedDTOVeld = nestedDTOVeld;
    }

    public void setNestedDTO(NestedDTO nestedDTO) {
        this.nestedDTO = nestedDTO;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("veld1", veld1).append("veld2", veld2).append("veld3", veld3)
                .append("nestedDTOVeld", nestedDTOVeld).append("nestedDTO", nestedDTO).toString();
    }
}
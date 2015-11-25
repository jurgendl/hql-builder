package org.tools.hqlbuilder.common.test;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.tools.hqlbuilder.common.EntityERHAdapter;

public class ETest extends EntityERHAdapter {
    private static final long serialVersionUID = 7973688933975517537L;

    public static final String P = "p";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PId", unique = true)
    @NotNull
    private PTest p;

    public PTest getP() {
        return p;
    }

    public void setP(PTest p) {
        erh.ooSet(P, p);
    }
}

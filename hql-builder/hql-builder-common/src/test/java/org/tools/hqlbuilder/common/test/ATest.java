package org.tools.hqlbuilder.common.test;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.tools.hqlbuilder.common.EntityERHAdapter;

public class ATest extends EntityERHAdapter {
    private static final long serialVersionUID = 5702705685072114348L;

    public static final String P = "p";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PI", nullable = false)
    private PTest p;

    public PTest getP() {
        return p;
    }

    public void setP(PTest p) {
        erh.moSet(P, p);
    }
}

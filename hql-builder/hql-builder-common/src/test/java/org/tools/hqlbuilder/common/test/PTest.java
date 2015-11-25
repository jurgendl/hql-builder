package org.tools.hqlbuilder.common.test;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.tools.hqlbuilder.common.EntityERHAdapter;

public class PTest extends EntityERHAdapter {
    private static final long serialVersionUID = 2929900553382116789L;

    public static final String AS = "as";

    public static final String E = "e";

    @OneToMany(mappedBy = ATest.P, fetch = FetchType.LAZY)
    private Set<ATest> as = new HashSet<>();

    @OneToOne(mappedBy = ETest.P, optional = true, fetch = FetchType.LAZY)
    private ETest e;

    public ETest getE() {
        return e;
    }

    public void setE(ETest e) {
        erh.ooSet(E, e);
    }

    public Set<ATest> getAs() {
        return erh.omGet(as);
    }

    public void setAs(Set<ATest> as) {
        erh.omSet(AS, as);
    }
}

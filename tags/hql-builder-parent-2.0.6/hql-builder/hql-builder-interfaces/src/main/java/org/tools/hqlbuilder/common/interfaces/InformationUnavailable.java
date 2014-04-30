package org.tools.hqlbuilder.common.interfaces;

import java.io.IOException;
import java.util.List;

public class InformationUnavailable implements Information {
    public InformationUnavailable() {
        super();
    }

    @Override
    public List<String> search(String text, String typeName, int hitsPerPage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void init(String id, Object sf) throws IOException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getOrder() {
        return 99;
    }
}

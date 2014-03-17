package org.tools.hqlbuilder.common.interfaces;

import java.io.IOException;
import java.util.List;

public class InformationUnavailable implements Information {
    protected final String message;

    public InformationUnavailable(String message) {
        this.message = message;
    }

    @Override
    public List<String> search(String text, String typeName, int hitsPerPage) {
        throw new UnsupportedOperationException(message);
    }

    @Override
    public void init(String id, Object sf) throws IOException, UnsupportedOperationException {
        throw new UnsupportedOperationException(message);
    }
}

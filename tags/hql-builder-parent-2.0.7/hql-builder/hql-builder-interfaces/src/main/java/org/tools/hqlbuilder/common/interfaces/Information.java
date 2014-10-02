package org.tools.hqlbuilder.common.interfaces;

import java.io.IOException;
import java.util.List;

public interface Information {
    List<String> search(String text, String typeName, int hitsPerPage);

    void init(String id, Object sessionFactory) throws IOException, UnsupportedOperationException;

    int getOrder();
}

package org.tools.hqlbuilder.common.interfaces;

import java.io.IOException;
import java.util.List;

public interface LInformation {
    List<String> search(String text, String typeName) throws IOException;

    void setSessionFactory(Object sessionFactory) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;
}

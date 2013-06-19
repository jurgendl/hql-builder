package org.tools.hqlbuilder.client;

import java.util.List;

/**
 * @author Jurgen
 */
public interface RowProcessor {
    void process(List<Object> lijn);
}

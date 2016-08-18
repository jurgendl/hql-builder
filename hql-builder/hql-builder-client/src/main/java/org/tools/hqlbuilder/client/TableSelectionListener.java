package org.tools.hqlbuilder.client;

/**
 * @author Jurgen
 */
public interface TableSelectionListener {
    /**
     * row selection veranderd
     *
     * @param row
     */
    public void rowChanged(int row);

    /**
     * column selection veranderd
     *
     * @param column
     */
    public void columnChanged(int column);

    /**
     * cell selection veranderd
     *
     * @param row
     * @param column
     */
    public void cellChanged(int row, int column);
}

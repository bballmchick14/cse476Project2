package edu.msu.team23.project1;

import java.io.Serializable;

/**
 * Class for describing a row-column location.
 */
public class Space implements Serializable {
    /**
     * Row of this space.
     */
    private final int row;

    /**
     * Column of this space
     */
    private final int col;

    /**
     * Constructor.
     * @param row Row position
     * @param col Column position
     */
    public Space(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Getter for the row of this space.
     * @return The row of this space
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter for the column of this space.
     * @return The column of this space
     */
    public int getCol() {
        return col;
    }
}

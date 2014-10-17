package com.gregswebserver.catan.common.game.board;

import com.gregswebserver.catan.client.graphics.Graphical;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.hexarray.HexagonalArray;
import com.gregswebserver.catan.common.game.board.hexarray.TwoDimensionalArray;

/**
 * Created by Greg on 8/19/2014.
 * A generic object that can be put into a HexArray, and has knowledge of it's position.
 */
public abstract class BoardObject implements Graphical {

    private HexagonalArray hexArray;
    private TwoDimensionalArray parentArray;
    private Coordinate position;

    public BoardObject() {
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public TwoDimensionalArray getParentArray() {
        return parentArray;
    }

    public void setParentArray(TwoDimensionalArray parentArray) {
        this.parentArray = parentArray;
    }

    public HexagonalArray getHexArray() {
        return hexArray;
    }

    public void setHexArray(HexagonalArray hexArray) {
        this.hexArray = hexArray;
    }

    public abstract String toString();
}

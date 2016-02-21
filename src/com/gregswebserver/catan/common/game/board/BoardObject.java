package com.gregswebserver.catan.common.game.board;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.hexarray.HexagonalArray;

/**
 * Created by Greg on 8/19/2014.
 * A generic object that can be put into a HexArray, and has knowledge of it's position.
 */
public abstract class BoardObject {

    private HexagonalArray hexArray;
    private Coordinate position;

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    protected HexagonalArray getHexArray() {
        return hexArray;
    }

    public void setHexArray(HexagonalArray hexArray) {
        this.hexArray = hexArray;
    }

    public abstract String toString();

}

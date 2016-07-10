package catan.common.game.board;

import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.board.hexarray.HexagonalArray;

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

    public HexagonalArray getHexArray() {
        return hexArray;
    }

    public void setHexArray(HexagonalArray hexArray) {
        this.hexArray = hexArray;
    }

    public abstract String toString();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardObject)) return false;

        BoardObject that = (BoardObject) o;

        return position != null ? position.equals(that.position) : that.position == null;

    }

    @Override
    public int hashCode() {
        return position != null ? position.hashCode() : 0;
    }
}

package catan.common.game.board;

import catan.common.game.board.hexarray.Coordinate;

/**
 * Created by Greg on 8/19/2014.
 * A generic object that can be put into a HexArray, and has knowledge of it's position.
 */
public abstract class BoardObject {

    private Coordinate position;

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public abstract String toString();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardObject)) return false;

        BoardObject other = (BoardObject) o;

        return (position != null) ? position.equals(other.position) : (other.position == null);

    }

    @Override
    public int hashCode() {
        return (position != null) ? position.hashCode() : 0;
    }
}

package com.gregswebserver.catan.game.board.hexarray;

/**
 * Created by Greg on 8/9/2014.
 * Coordinate class for talking to the Dimensional Array.
 */
public class Coordinate {

    private int x, y;

    public Coordinate() {
        x = 0;
        y = 0;
    }

    public Coordinate(String input) {

    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Object o) {
        if (o instanceof Coordinate) {
            Coordinate c = (Coordinate) o;
            return c.x == x && c.y == y;
        }
        return false;
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

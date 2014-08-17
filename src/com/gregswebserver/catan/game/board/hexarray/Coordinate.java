package com.gregswebserver.catan.game.board.hexarray;

import java.io.Serializable;

/**
 * Created by Greg on 8/9/2014.
 * Coordinate class for referring to object locations.
 * Used in HexagonalArray and TwoDimensionalArray classes.
 */
public class Coordinate implements Serializable {

    public final int x, y;

    public Coordinate() {
        x = 0;
        y = 0;
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
        //Using Szudzik's Formula.
        int A = x >= 0 ? 2 * x : -2 * x - 1;
        int B = y >= 0 ? 2 * y : -2 * y - 1;
        return A >= B ? A * A + A + B : A + B * B;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

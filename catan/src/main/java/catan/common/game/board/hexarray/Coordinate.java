package catan.common.game.board.hexarray;

import java.io.Serializable;

/**
 * Created by Greg on 8/9/2014.
 * Coordinate class for referring to object locations.
 * Used in HexagonalArray and TwoDimensionalArray classes.
 */
public final class Coordinate implements Serializable {

    public final int x;
    public final int y;

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
        if (o == this)
            return true;
        if (o instanceof Coordinate) {
            Coordinate coord = (Coordinate) o;
            return (coord.x == x) && (coord.y == y);
        }
        return false;
    }

    public int hashCode() {
        //Using Szudzik's Formula.
        int a = (x >= 0) ? (2 * x) : ((-2 * x) - 1);
        int b = (y >= 0) ? (2 * y) : ((-2 * y) - 1);
        return (a >= b) ? ((a * a) + a + b) : (a + (b * b));
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

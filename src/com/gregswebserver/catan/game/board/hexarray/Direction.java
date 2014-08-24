package com.gregswebserver.catan.game.board.hexarray;

import java.util.HashSet;

/**
 * Created by Greg on 8/13/2014.
 * Direction enum for spatial relationships in the HexagonalArray.
 */
public enum Direction {
    //Edge/Space-only reference.
    up(0, 0, 1),
    down(1, 0, -1),
    //Vertex-only reference.
    left(2, -1, 0),
    right(3, 1, 0),
    //Dual reference.
    upleft(4, up, left),
    downleft(5, down, left),
    upright(6, up, right),
    downright(7, down, right);

    private final int index;
    private final int x;
    private final int y;

    Direction(int index, int x, int y) {
        this.index = index;
        this.x = x;
        this.y = y;
    }

    Direction(int index, Direction a, Direction b) {
        this.index = index;
        this.x = a.x + b.x;
        this.y = a.y + b.y;
    }

    public static Direction getAverage(HashSet<Direction> input) {
        int sumX = 0, sumY = 0;
        Direction out = null;
        for (Direction d : input) {
            sumX += d.x;
            sumY += d.y;
        }
        if (sumY == 0) {
            if (sumX == 0)
                out = null;
            if (sumX > 0)
                out = right;
            if (sumX < 0)
                out = left;
        } else if (sumY > 0) {
            if (sumX == 0)
                out = up;
            if (sumX > 0)
                out = upright;
            if (sumX < 0)
                out = upleft;
        } else if (sumY < 0) {
            if (sumX == 0)
                out = down;
            if (sumX > 0)
                out = downright;
            if (sumX < 0)
                out = downleft;
        }
        return out;
    }

    public int index() {
        return index;
    }
}

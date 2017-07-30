package catan.common.util;

import java.util.Set;

/**
 * Created by Greg on 8/13/2014.
 * Direction enum for spatial relationships in the HexagonalArray.
 */
public enum Direction {

    center(0, 0),
    up(0, 1),
    down(0, -1),
    left(-1, 0),
    right(1, 0),
    upleft(up, left),
    downleft(down, left),
    upright(up, right),
    downright(down, right);

    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Direction(Direction a, Direction b) {
        x = a.x + b.x;
        y = a.y + b.y;
    }

    public static Direction getAverage(Set<Direction> input) {
        int sumX = 0;
        int sumY = 0;
        for (Direction d : input) {
            sumX += d.x;
            sumY += d.y;
        }
        Direction out = null;
        if (sumY == 0) {
            if (sumX == 0)
                out = center;
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
}

package com.gregswebserver.catan.game.board.hexarray;

/**
 * Created by Greg on 8/13/2014.
 * Direction enum for spatial relationships in the HexagonalArray.
 */
public enum Direction {
    //Edge/Space-only reference.
    up(0, 0, 1),
    down(1, 0, -1),
    //Vertex-only reference.
    left(0, -1, 0),
    right(1, 1, 0),
    //Dual reference.
    upleft(2, up, left),
    downleft(3, down, left),
    upright(4, up, right),
    downright(5, down, right);

    private int index;
    private int x;
    private int y;

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

    public int index() {
        return index;
    }

    public boolean isEdgeValid() {
        switch (this) {
            case up:
            case down:
            case upleft:
            case downleft:
            case upright:
            case downright:
                return true;
            case left:
            case right:
            default:
                return false;
        }
    }

    public boolean isVertexValid() {
        switch (this) {
            case left:
            case right:
            case upleft:
            case downleft:
            case upright:
            case downright:
                return true;
            case up:
            case down:
            default:
                return false;
        }
    }
}

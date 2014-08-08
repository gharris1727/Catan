package com.gregswebserver.catan.game.board.locations;

import java.util.HashSet;

/**
 * Created by Greg on 8/8/2014.
 * Map system for generating locations.
 */
public class HexagonalMap {

    private final int MAX_SIZE = 100;
    private int xSize, ySize;
    private HashSet<Space> spaces;
    private HashSet<Edge> edges;
    private HashSet<Vertex> vertexes;

    public HexagonalMap(int size) {
        this(size, size);
    }

    public HexagonalMap(int x, int y) {
        if (sizeCheck(x, y)) {
            xSize = x;
            ySize = y;
            populate();
        } else {
            throw new IllegalArgumentException("Invalid size: x:" + x + " y:" + y + ".");
        }
    }

    public void populate() {
        spaces = new HashSet<>(xSize * ySize);
        HashSet<Space> out = new HashSet<>(xSize * ySize);
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                out.add(new Space(x + y * MAX_SIZE));
            }
        }
    }

    public int getX() {
        return xSize;
    }

    public int getY() {
        return ySize;
    }

    public boolean sizeCheck(int x, int y) {
        return !(x < 0 || x >= MAX_SIZE || y < 0 || y >= MAX_SIZE);
    }

    public boolean rangeCheck(int x, int y) {
        return !(x < 0 || x >= xSize || y < 0 || y >= ySize);
    }

    public boolean rangeCheck(int x, int y, Direction dir) {
        switch (dir) {
            case up:
                y--;
                break;
            case down:
                y++;
                break;
            case upleft:
                x--;
                break;
            case upright:
                x++;
                break;
            case downleft:
                y--;
                x--;
                break;
            case downright:
                y--;
                x++;
                break;
        }
        return rangeCheck(x, y);
    }

    public HashSet<Space> getSpaces() {
        return spaces;
    }

    public HashSet<Edge> getEdges() {
        return edges;
    }

    public HashSet<Vertex> getVertexes() {
        return vertexes;
    }

    private enum Direction {
        up,
        down,
        upleft,
        upright,
        downleft,
        downright
    }
}

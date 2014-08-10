package com.gregswebserver.catan.game.board.hexarray;

import com.gregswebserver.catan.game.board.buildings.Building;
import com.gregswebserver.catan.game.board.paths.Path;
import com.gregswebserver.catan.game.board.tiles.Tile;

import java.util.ArrayList;

/**
 * Created by Greg on 8/8/2014.
 * Map system for generating locations.
 */
public class HexagonalArray {
    public TwoDimensionalArray<Tile> tiles;
    public TwoDimensionalArray<Path> paths;
    public TwoDimensionalArray<Building> buildings;

    private HexagonalArray(int x, int y) {
        tiles = new TwoDimensionalArray<>(x, y); // Number of spaces in a hex map is x * y
        paths = new TwoDimensionalArray<>(4 * x, y + 1); // number of edges in a hex map is less than 4x * y+1
        buildings = new TwoDimensionalArray<>(2 * (x + 1), y + 1); // number of vertices in a hex map is less than 2(x+1) * y+1
    }

    public ArrayList<Coordinate> getAdjacentVertices(Coordinate spaceCoordinate) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (Direction d : Direction.values()) {
            try {
                coordinates.add(getVertex(spaceCoordinate, d));
            } catch (Exception e) {
                //Any errors indicate that either the direction was invalid
                //Or was trying to reference the edge of the map.
            }
        }
        return coordinates;

    }

    public ArrayList<Coordinate> getAdjacentEdges(Coordinate spaceCoordinate) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (Direction d : Direction.values()) {
            try {
                coordinates.add(getEdge(spaceCoordinate, d));
            } catch (Exception e) {
                //Any errors indicate that either the direction was invalid
                //Or was trying to reference the edge of the map.
            }
        }
        return coordinates;
    }

    public ArrayList<Coordinate> getAdjacentSpaces(Coordinate spaceCoordinate) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (Direction d : Direction.values()) {
            try {
                coordinates.add(getSpace(spaceCoordinate, d));
            } catch (Exception e) {
                //Any errors indicate that either the direction was invalid
                //Or was trying to reference the edge of the map.
            }
        }
        return coordinates;
    }

    public Coordinate getVertex(Coordinate spaceCoordinate, Direction direction) {
        // array[<even or odd>][<x or y>][<direction>] = translation
        int[][][] vertexAdditions = {
                { //Even
                        {0, 3, 1, 1, 2, 2}, //X
                        {0, 0, 0, 1, 0, 1}}, //Y
                { //Odd
                        {0, 3, 1, 1, 2, 2}, //X
                        {1, 1, 0, 1, 0, 1}}}; //Y
        checkDirection(direction, Direction.up, Direction.down);
        Coordinate out = translate(spaceCoordinate, direction, 2, vertexAdditions[spaceCoordinate.getX() % 2]);
        buildings.rangeCheck(out);
        return out;
    }

    public Coordinate getEdge(Coordinate spaceCoordinate, Direction direction) {
        // array[<even or odd>][<x or y>][<direction>] = translation
        int[][][] edgeAdditions = {
                { //Even
                        {2, 2, 0, 1, 3, 4}, //X
                        {0, 1, 0, 0, 0, 0}}, //Y
                { //Odd
                        {2, 2, 1, 0, 4, 3}, //X
                        {0, 1, 0, 1, 0, 1}}}; //Y
        checkDirection(direction, Direction.left, Direction.right);
        Coordinate out = translate(spaceCoordinate, direction, 3, edgeAdditions[spaceCoordinate.getX() % 2]);
        paths.rangeCheck(out);
        return out;
    }

    public Coordinate getSpace(Coordinate spaceCoordinate, Direction direction) {
        // array[<x or y>][<direction>] = translation
        int[][] spaceAdditions = {
                {0, 0, -1, -1, 1, 1}, //X
                {-1, 1, 0, 1, 0, 1}}; //Y
        checkDirection(direction, Direction.left, Direction.right);
        Coordinate out = translate(spaceCoordinate, direction, 1, spaceAdditions);
        tiles.rangeCheck(out);
        return out;
    }

    private Coordinate translate(Coordinate spaceCoordinate, Direction direction, int multX, int[][] additions) {
        int outX = spaceCoordinate.getX() * multX;
        int outY = spaceCoordinate.getY();
        outX += additions[0][direction.index()];
        outY += additions[1][direction.index()];
        return new Coordinate(outX, outY);
    }

    private void checkDirection(Direction direction, Direction a, Direction b) {
        // direction is the direction being tested
        // a and b are disallowed directions
        // Throws exception if direction is null or equals a or b;
        if (direction == null) {
            throw new IllegalArgumentException("Direction cannot be a null reference.");
        }
        if (direction.equals(a) || direction.equals(b)) {
            throw new IllegalArgumentException("Cannot find the feature to the " + direction.name());
        }
    }

    private enum Direction {
        //Edge/Space-only reference.
        up(0, true, false),
        down(1, true, false),
        //Vertex-only reference.
        left(0, false, true),
        right(1, false, true),
        //Dual reference.
        upleft(2, true, true),
        downleft(3, true, true),
        upright(4, true, true),
        downright(5, true, true);

        private int index;
        private boolean edge;
        private boolean vertex;

        Direction(int index, boolean edge, boolean vertex) {
            this.index = index;
            this.edge = edge;
            this.vertex = vertex;
        }

        private int index() {
            return index;
        }

        private boolean isEdgeValid() {
            return edge;
        }

        private boolean isVertexValid() {
            return vertex;
        }
    }
}

package com.gregswebserver.catan.game.board.hexarray;

import java.util.HashSet;

/**
 * Created by Greg on 8/8/2014.
 * Map system for storing board data.
 */
public class HexagonalArray<X, Y, Z> {

    public TwoDimensionalArray<X> spaces;
    public TwoDimensionalArray<Y> edges;
    public TwoDimensionalArray<Z> vertices;
    private Class xClass;
    private Class yClass;
    private Class zClass;

    public HexagonalArray(Class<X> xClass, Class<Y> yClass, Class<Z> zClass, int x, int y) {
        this.xClass = xClass;
        this.yClass = yClass;
        this.zClass = zClass;
        spaces = new TwoDimensionalArray<>(x, y); // Number of spaces in a hex map is x * y
        edges = new TwoDimensionalArray<>(4 * x, y + 1); // number of edges in a hex map is less than 4x * y+1
        vertices = new TwoDimensionalArray<>(2 * (x + 1), y + 1); // number of vertices in a hex map is less than 2(x+1) * y+1
    }

    @SuppressWarnings("unchecked")
    public static boolean isSubclass(Object o, Class c) {
        return c.isAssignableFrom(o.getClass());
    }

    @SuppressWarnings("unchecked")
    public void place(Coordinate c, Object o) {
        if (isSubclass(o, xClass)) {
            spaces.set(c, (X) o);
        } else if (isSubclass(o, yClass)) {
            edges.set(c, (Y) o);
        } else if (isSubclass(o, zClass)) {
            vertices.set(c, (Z) o);
        } else {
            throw new IllegalArgumentException("Object not an instanceof X Y or Z types.");
        }
    }

    public HashSet<Coordinate> getAdjacentSpacesFromSpace(Coordinate c) {
        HashSet<Coordinate> out = new HashSet<>();
        for (Direction d : Direction.values()) {
            try {
                out.add(getSpaceCoordinateFromSpace(c, d));
            } catch (Exception e) {
                //Any error indicates that either the direction was invalid
                //Or was trying to reference the edge of the map.
            }
        }
        return out;
    }

    public HashSet<Coordinate> getAdjacentEdgesFromSpace(Coordinate c) {
        HashSet<Coordinate> out = new HashSet<>();
        for (Direction d : Direction.values()) {
            try {
                out.add(getEdgeCoordinateFromSpace(c, d));
            } catch (Exception e) {
                //Any error indicates that either the direction was invalid
                //Or was trying to reference the edge of the map.
            }
        }
        return out;
    }

    public HashSet<Coordinate> getAdjacentVerticesFromSpace(Coordinate c) {
        HashSet<Coordinate> out = new HashSet<>();
        for (Direction d : Direction.values()) {
            try {
                out.add(getVertexCoordinateFromSpace(c, d));
            } catch (Exception e) {
                //Any error indicates that either the direction was invalid
                //Or was trying to reference the edge of the map.
            }
        }
        return out;
    }

    public Coordinate getSpaceCoordinateFromSpace(Coordinate c, Direction d) {
        if (!d.isEdgeValid()) throw new IllegalArgumentException("Direction reference not valid for space.");
        // additions[<x or y>][<direction>] = translation
        int[][] spaceAdditions = {
                {0, 0, -1, -1, 1, 1}, //X
                {-1, 1, 0, 1, 0, 1}}; //Y
        Coordinate out = translateCoordinate(c, d, 1, spaceAdditions);
        spaces.rangeCheck(out);
        return out;
    }

    public Coordinate getEdgeCoordinateFromSpace(Coordinate c, Direction d) {
        if (!d.isEdgeValid()) throw new IllegalArgumentException("Direction reference not valid for edge.");
        // additions[<even or odd>][<x or y>][<direction>] = translation
        int[][][] edgeAdditions = {
                { //Even
                        {2, 2, 0, 1, 3, 4}, //X
                        {0, 1, 0, 0, 0, 0}}, //Y
                { //Odd
                        {2, 2, 1, 0, 4, 3}, //X
                        {0, 1, 0, 1, 0, 1}}}; //Y
        Coordinate out = translateCoordinate(c, d, 3, edgeAdditions[c.getX() % 2]);
        edges.rangeCheck(out);
        return out;
    }

    public Coordinate getVertexCoordinateFromSpace(Coordinate c, Direction d) {
        if (!d.isVertexValid()) throw new IllegalArgumentException("Direction reference not valid for vertex.");
        // additions[<even or odd>][<x or y>][<direction>] = translation
        int[][][] vertexAdditions = {
                { //Even
                        {0, 3, 1, 1, 2, 2}, //X
                        {0, 0, 0, 1, 0, 1}}, //Y
                { //Odd
                        {0, 3, 1, 1, 2, 2}, //X
                        {1, 1, 0, 1, 0, 1}}}; //Y
        Coordinate out = translateCoordinate(c, d, 2, vertexAdditions[c.getX() % 2]);
        vertices.rangeCheck(out);
        return out;
    }

    private Coordinate translateCoordinate(Coordinate c, Direction d, int multX, int[][] additions) {
        int outX = c.getX() * multX;
        int outY = c.getY();
        outX += additions[0][d.index()];
        outY += additions[1][d.index()];
        return new Coordinate(outX, outY);
    }

}

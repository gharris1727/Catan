package com.gregswebserver.catan.game.board.hexarray;

import java.util.HashMap;

/**
 * Created by Greg on 8/8/2014.
 * Map system for storing board data.
 */
public class HexagonalArray<X, Y, Z> {

    // additions[<x or y>][<direction>] = translation
    private static final int[][] spaceToSpace = {
            {0, 0, 0, 0, -1, -1, 1, 1}, //X
            {-1, 1, 0, 0, 0, 1, 0, 1}}; //Y
    private static final int[][][] edgeToEdge = {
            { //ZERO
                    {1, 1, -1, 2, 0, 0, 0, 0}, //X
                    {-1, 0, 0, 0, 0, 0, 0, 0}}, //Y
            { //ONE
                    {-1, -1, -2, 1, 0, 0, 0, 0}, //X
                    {0, 1, 0, 1, 0, 0, 0, 0}}, //Y
            { //TWO
                    {0, 0, 0, 0, -1, -2, 2, 1}, //X
                    {0, 0, 0, 0, -1, 0, -1, 0}}, //Y
            { //THREE
                    {1, 1, -1, 2, 0, 0, 0, 0}, //X
                    {-1, 0, 0, 0, 0, 0, 0, 0}}, //Y
            { //FOUR
                    {-1, -1, -2, 1, 0, 0, 0, 0}, //X
                    {0, 1, 1, 0, 0, 0, 0, 0}}, //Y
            { //FIVE
                    {0, 0, 0, 0, -2, -1, 1, 2}, //X
                    {0, 0, 0, 0, 0, 0, 0, 0}}}; //Y
    private static final int[][][] vertexToVertex = {
            { //ZERO
                    {0, 0, -1, 0, 0, 0, 1, 1}, //X
                    {0, 0, 0, 0, 0, 0, 0, -1}}, //Y
            { //ONE
                    {0, 0, 0, 1, -1, -1, 0, 0}, //X
                    {0, 0, 0, 0, -1, 0, 0, 0}}, //Y
            { //TWO
                    {0, 0, -1, 0, 0, 0, 1, 1}, //X
                    {0, 0, 0, 0, 0, 0, -1, 0}}, //Y
            { //THREE
                    {0, 0, 0, 1, -1, -1, 0, 0}, //X
                    {0, 0, 0, 0, 0, 1, 0, 0}}}; //Y
    // additions[<even or odd>][<x or y>][<direction>] = translation
    private static final int[][][] spaceToEdge = {
            { //EVEN
                    {2, 2, 0, 0, 0, 1, 3, 4}, //X
                    {0, 1, 0, 0, 0, 0, 0, 0}}, //Y
            { //ODD
                    {2, 2, 0, 0, 1, 0, 4, 3}, //X
                    {0, 1, 0, 0, 0, 1, 0, 1}}}; //Y
    private static final int[][][] spaceToVertex = {
            { //EVEN
                    {0, 0, 0, 3, 1, 1, 2, 2}, //X
                    {0, 0, 0, 0, 0, 1, 0, 1}}, //Y
            { //ODD
                    {0, 0, 0, 3, 1, 1, 2, 2}, //X
                    {0, 0, 1, 1, 0, 1, 0, 1}}}; //Y
    //TODO: finish implementing more translations.
    private static final int[][][] edgeToSpace = {
            { //EVEN
                    {}, //X
                    {}}, //Y
            { //ODD
                    {}, //X
                    {}}}; //Y
    private static final int[][][] edgeToVertex = {
            { //EVEN
                    {}, //X
                    {}}, //Y
            { //ODD
                    {}, //X
                    {}}}; //Y
    private static final int[][][] vertexToSpace = {
            { //EVEN
                    {}, //X
                    {}}, //Y
            { //ODD
                    {}, //X
                    {}}}; //Y
    private static final int[][][] vertexToEdge = {
            { //EVEN
                    {}, //X
                    {}}, //Y
            { //ODD
                    {}, //X
                    {}}}; //Y

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

    private static Coordinate convert(Coordinate c, Direction d, int[][] additions) {
        int outX = c.x + additions[0][d.index()];
        int outY = c.y + additions[1][d.index()];
        return new Coordinate(outX, outY);
    }

    //Unimplemented features are marked deprecated to warn against using them.
    //They still exist though.

    @Deprecated
    private static Coordinate convertAcross(Coordinate c, Direction d, int divX, int multX, int[][] subtractions, int[][] additions) {
        return new Coordinate();
    }

    private static Coordinate convertUp(Coordinate c, Direction d, int multX, int[][] additions) {
        int outX = c.x * multX;
        int outY = c.y;
        outX += additions[0][d.index()];
        outY += additions[1][d.index()];
        return new Coordinate(outX, outY);
    }

    @Deprecated
    private static Coordinate convertDown(Coordinate c, Direction d, int divX, int[][] subtractions) {
        int outX = c.x - subtractions[0][d.index()];
        int outY = c.y - subtractions[1][d.index()];
        outX /= divX;
        return new Coordinate(outX, outY);
    }

    private static void checkDirection(Direction d, Direction[] validDirections) throws IllegalDirectionException {
        //Usable for any of the conversions, just more tedious to use.
        for (Direction valid : validDirections) {
            if (valid.equals(d)) return;
        }
        throw new IllegalDirectionException(d);
    }

    private static void checkDirection(Direction d, int[][] array) throws IllegalDirectionException {
        //If both indexes are zero, then that indicates that no motion is made.
        //only usable for the 1:1 ratio conversions (space-space, edge-edge, vertex-vertex)
        if (array[0][d.index()] == 0 && array[1][d.index()] == 0)
            throw new IllegalDirectionException(d);
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

    //Multi-translation functions

    public HashMap<Direction, Coordinate> getAdjacentSpacesFromSpace(Coordinate c) {
        HashMap<Direction, Coordinate> out = new HashMap<>(6);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getSpaceCoordinateFromSpace(c, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public HashMap<Direction, Coordinate> getAdjacentEdgesFromEdge(Coordinate c) {
        HashMap<Direction, Coordinate> out = new HashMap<>(4);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getEdgeCoordinateFromEdge(c, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public HashMap<Direction, Coordinate> getAdjacentVerticesFromVertex(Coordinate c) {
        HashMap<Direction, Coordinate> out = new HashMap<>(3);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getVertexCoordinateFromVertex(c, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public HashMap<Direction, Coordinate> getAdjacentEdgesFromSpace(Coordinate c) {
        HashMap<Direction, Coordinate> out = new HashMap<>(6);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getEdgeCoordinateFromSpace(c, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public HashMap<Direction, Coordinate> getAdjacentVerticesFromSpace(Coordinate c) {
        HashMap<Direction, Coordinate> out = new HashMap<>(6);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getVertexCoordinateFromSpace(c, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    @Deprecated
    public HashMap<Direction, Coordinate> getAdjacentSpacesFromEdge(Coordinate c) {
        HashMap<Direction, Coordinate> out = new HashMap<>(2);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getSpaceCoordinateFromEdge(c, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    @Deprecated
    public HashMap<Direction, Coordinate> getAdjacentVerticesFromEdge(Coordinate c) {
        HashMap<Direction, Coordinate> out = new HashMap<>(2);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getVertexCoordinateFromEdge(c, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    @Deprecated
    public HashMap<Direction, Coordinate> getAdjacentSpacesFromVertex(Coordinate c) {
        HashMap<Direction, Coordinate> out = new HashMap<>(3);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getSpaceCoordinateFromVertex(c, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    @Deprecated
    public HashMap<Direction, Coordinate> getAdjacentEdgesFromVertex(Coordinate c) {
        HashMap<Direction, Coordinate> out = new HashMap<>(3);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getEdgeCoordinateFromVertex(c, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    //Single translation functions

    public Coordinate getSpaceCoordinateFromSpace(Coordinate c, Direction d) throws IllegalDirectionException {
        checkDirection(d, spaceToSpace);
        Coordinate out = convert(c, d, spaceToSpace);
        spaces.rangeCheck(out);
        return out;
    }

    public Coordinate getEdgeCoordinateFromEdge(Coordinate c, Direction d) throws IllegalDirectionException {
        checkDirection(d, edgeToEdge[c.x % 6]);
        Coordinate out = convert(c, d, edgeToEdge[c.x % 6]);
        spaces.rangeCheck(out);
        return out;
    }

    public Coordinate getVertexCoordinateFromVertex(Coordinate c, Direction d) throws IllegalDirectionException {
        checkDirection(d, vertexToVertex[c.x % 4]);
        Coordinate out = convert(c, d, vertexToVertex[c.x % 4]);
        spaces.rangeCheck(out);
        return out;
    }

    public Coordinate getEdgeCoordinateFromSpace(Coordinate c, Direction d) throws IllegalDirectionException {
        Direction[] validDirections = {Direction.up, Direction.down, Direction.upleft, Direction.upright, Direction.downleft, Direction.downright};
        checkDirection(d, validDirections);
        Coordinate out = convertUp(c, d, 3, spaceToEdge[c.x % 2]);
        edges.rangeCheck(out);
        return out;
    }

    public Coordinate getVertexCoordinateFromSpace(Coordinate c, Direction d) throws IllegalDirectionException {
        Direction[] validDirections = {Direction.left, Direction.right, Direction.upleft, Direction.upright, Direction.downleft, Direction.downright};
        checkDirection(d, validDirections);
        Coordinate out = convertUp(c, d, 2, spaceToVertex[c.x % 2]);
        vertices.rangeCheck(out);
        return out;
    }

    @Deprecated
    public Coordinate getSpaceCoordinateFromEdge(Coordinate c, Direction d) throws IllegalDirectionException {
        Direction[][] validDirections = {
                {Direction.upleft, Direction.downright},
                {Direction.downleft, Direction.upright},
                {Direction.up, Direction.down},
                {Direction.downleft, Direction.upright},
                {Direction.upleft, Direction.downright},
                {Direction.up, Direction.down}};
        checkDirection(d, validDirections[c.x % 6]);
        Coordinate out = convert(c, d, edgeToSpace[c.x % 6]);
        spaces.rangeCheck(out);
        return out;
    }

    @Deprecated
    public Coordinate getVertexCoordinateFromEdge(Coordinate c, Direction d) throws IllegalDirectionException {
        Direction[][] validDirections = {
                {Direction.upright, Direction.downleft},
                {Direction.downright, Direction.upleft},
                {Direction.left, Direction.right},
                {Direction.downright, Direction.upleft},
                {Direction.upright, Direction.downleft},
                {Direction.left, Direction.right}};
        checkDirection(d, validDirections[c.x % 6]);
        Coordinate out = convert(c, d, edgeToVertex[c.x % 6]);
        spaces.rangeCheck(out);
        return out;
    }

    @Deprecated
    public Coordinate getSpaceCoordinateFromVertex(Coordinate c, Direction d) throws IllegalDirectionException {
        Direction[][] validDirections = {
                {Direction.upleft, Direction.downleft, Direction.right},
                {Direction.upright, Direction.downright, Direction.left}};
        checkDirection(d, validDirections[c.x % 2]);
        Coordinate out = convert(c, d, vertexToSpace[c.x % 4]);
        spaces.rangeCheck(out);
        return out;
    }

    @Deprecated
    public Coordinate getEdgeCoordinateFromVertex(Coordinate c, Direction d) throws IllegalDirectionException {
        Direction[][] validDirections = {
                {Direction.upright, Direction.downright, Direction.left},
                {Direction.upleft, Direction.downleft, Direction.right}};
        checkDirection(d, validDirections[c.x % 2]);
        Coordinate out = convert(c, d, vertexToEdge[c.x % 4]);
        edges.rangeCheck(out);
        return out;
    }

}

package com.gregswebserver.catan.common.game.board.hexarray;

import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.util.Direction;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Greg on 8/8/2014.
 * Map system for storing board data.
 */
public class HexagonalArray {

    // additions[<x or y>][<direction>] = translation
    private static final int[][][] spaceToSpace = {
            { //EVEN
                    {0, 0, 0, 0, 0, -1, -1, 1, 1}, //X
                    {0, -1, 1, 0, 0, -1, 0, -1, 0}}, //Y
            { //ODD
                    {0, 0, 0, 0, 0, -1, -1, 1, 1,}, //X
                    {0, -1, 1, 0, 0, 0, 1, 0, 1}}}; //Y
    private static final int[][][] edgeToEdge = {
            { //ZERO
                    {0, 1, 1, -1, 2, 0, 0, 0, 0}, //X
                    {0, -1, 0, 0, 0, 0, 0, 0, 0}}, //Y
            { //ONE
                    {0, -1, -1, -2, 1, 0, 0, 0, 0}, //X
                    {0, 0, 1, 0, 1, 0, 0, 0, 0}}, //Y
            { //TWO
                    {0, 0, 0, 0, 0, -1, -2, 2, 1}, //X
                    {0, 0, 0, 0, 0, -1, 0, -1, 0}}, //Y
            { //THREE
                    {0, 1, 1, -1, 2, 0, 0, 0, 0}, //X
                    {0, -1, 0, 0, 0, 0, 0, 0, 0}}, //Y
            { //FOUR
                    {0, -1, -1, -2, 1, 0, 0, 0, 0}, //X
                    {0, 0, 1, 1, 0, 0, 0, 0, 0}}, //Y
            { //FIVE
                    {0, 0, 0, 0, 0, -2, -1, 1, 2}, //X
                    {0, 0, 0, 0, 0, 0, 0, 0, 0}}}; //Y
    private static final int[][][] vertexToVertex = {
            { //ZERO
                    {0, 0, 0, -1, 0, 0, 0, 1, 1}, //X
                    {0, 0, 0, 0, 0, 0, 0, 0, -1}}, //Y
            { //ONE
                    {0, 0, 0, 0, 1, -1, -1, 0, 0}, //X
                    {0, 0, 0, 0, 0, -1, 0, 0, 0}}, //Y
            { //TWO
                    {0, 0, 0, -1, 0, 0, 0, 1, 1}, //X
                    {0, 0, 0, 0, 0, 0, 0, -1, 0}}, //Y
            { //THREE
                    {0, 0, 0, 0, 1, -1, -1, 0, 0}, //X
                    {0, 0, 0, 0, 0, 0, 1, 0, 0}}}; //Y
    // additions[<even or odd>][<x or y>][<direction>] = translation
    private static final int[][][] spaceToEdge = {
            { //EVEN
                    {0, 2, 2, 0, 0, 0, 1, 3, 4}, //X
                    {0, 0, 1, 0, 0, 0, 0, 0, 0}}, //Y
            { //ODD
                    {0, 2, 2, 0, 0, 0, 1, 4, 3}, //X
                    {0, 0, 1, 0, 0, 0, 1, 0, 1}}}; //Y
    private static final int[][][] spaceToVertex = {
            { //EVEN
                    {0, 0, 0, 0, 3, 1, 1, 2, 2}, //X
                    {0, 0, 0, 0, 0, 0, 1, 0, 1}}, //Y
            { //ODD
                    {0, 0, 0, 0, 3, 1, 1, 2, 2}, //X
                    {0, 0, 0, 1, 1, 0, 1, 0, 1}}}; //Y
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

    public final TwoDimensionalArray<Tile> spaces;
    public final TwoDimensionalArray<Path> edges;
    public final TwoDimensionalArray<Town> vertices;

    public HexagonalArray(int x, int y) {
        spaces = new TwoDimensionalArray<>(x, y); // Number of spaces in a hex map is x * y
        edges = new TwoDimensionalArray<>(3 * (x + 1), y + 1); // number of edges in a hex map is less than 4x * y+1
        vertices = new TwoDimensionalArray<>(2 * (x + 1), y + 1); // number of vertices in a hex map is less than 2(x+1) * y+1
    }

    private static Coordinate convert(Coordinate c, Direction d, int[][] additions) {
        int outX = c.x + additions[0][d.ordinal()];
        int outY = c.y + additions[1][d.ordinal()];
        return new Coordinate(outX, outY);
    }

    //Unimplemented features are marked deprecated to warn against using them.

    @Deprecated
    private static Coordinate convertAcross(Coordinate c, Direction d, int divX, int multX, int[][] subtractions, int[][] additions) {
        return new Coordinate();
    }

    private static Coordinate convertUp(Coordinate c, Direction d, int multX, int[][] additions) {
        int outX = c.x * multX;
        int outY = c.y;
        outX += additions[0][d.ordinal()];
        outY += additions[1][d.ordinal()];
        return new Coordinate(outX, outY);
    }

    @Deprecated
    private static Coordinate convertDown(Coordinate c, Direction d, int divX, int[][] subtractions) {
        int outX = c.x - subtractions[0][d.ordinal()];
        int outY = c.y - subtractions[1][d.ordinal()];
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
        //TODO: this function needs to be refined, fails on more cases than necessary.
        //If both indexes are zero, then that indicates that no motion is made.
        //only usable for the 1:1 ratio conversions (space-space, edge-edge, vertex-vertex)
        if (array[0][d.ordinal()] == 0 && array[1][d.ordinal()] == 0)
            throw new IllegalDirectionException(d);
    }

    public void setTile(Coordinate c, Tile t) {
        if (t == null) return;
        t.setHexArray(this);
        t.setPosition(c);
        spaces.set(c, t);
    }

    public void setPath(Coordinate c, Path p) {
        if (p == null) return;
        p.setHexArray(this);
        p.setPosition(c);
        edges.set(c, p);
    }

    public void setTown(Coordinate c, Town b) {
        if (b == null) return;
        b.setHexArray(this);
        b.setPosition(c);
        vertices.set(c, b);
    }

    public Tile getTile(Coordinate c) {
        return spaces.get(c);
    }

    public Path getPath(Coordinate c) {
        return edges.get(c);
    }

    public Town getTown(Coordinate c) {
        return vertices.get(c);
    }

    //Multi-translation functions

    public Map<Direction, Coordinate> getAdjacentSpacesFromSpace(Coordinate c) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getSpaceCoordinateFromSpace(c, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public Map<Direction, Coordinate> getAdjacentEdgesFromEdge(Coordinate c) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getEdgeCoordinateFromEdge(c, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public Map<Direction, Coordinate> getAdjacentVerticesFromVertex(Coordinate c) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getVertexCoordinateFromVertex(c, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public Map<Direction, Coordinate> getAdjacentEdgesFromSpace(Coordinate c) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getEdgeCoordinateFromSpace(c, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public Map<Direction, Coordinate> getAdjacentVerticesFromSpace(Coordinate c) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
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
    public Map<Direction, Coordinate> getAdjacentSpacesFromEdge(Coordinate c) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
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
    public Map<Direction, Coordinate> getAdjacentVerticesFromEdge(Coordinate c) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
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
    public Map<Direction, Coordinate> getAdjacentSpacesFromVertex(Coordinate c) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
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
    public Map<Direction, Coordinate> getAdjacentEdgesFromVertex(Coordinate c) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
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
        checkDirection(d, spaceToSpace[c.x % 2]);
        Coordinate out = convert(c, d, spaceToSpace[c.x % 2]);
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

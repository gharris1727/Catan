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
    private static final int[][][] spaceToSpace = { //ADDITIONS
            { //EVEN
                    {0, 0, 0, 0, 0, -1, -1, 1, 1}, //X
                    {0, -1, 1, 0, 0, -1, 0, -1, 0}}, //Y
            { //ODD
                    {0, 0, 0, 0, 0, -1, -1, 1, 1,}, //X
                    {0, -1, 1, 0, 0, 0, 1, 0, 1}}}; //Y
    private static final int[][][] edgeToEdge = { //ADDITIONS
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
    private static final int[][][] vertexToVertex = { //ADDITIONS
            { //ZERO
                    {0, 0, 0, -1, 0, 0, 0, 1, 1}, //X
                    {0, 0, 0, 0, 0, 0, 0, 0, 1}}, //Y
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
    private static final int[][][] spaceToEdge = { //ADDITIONS
            { //EVEN
                    {0, 2, 2, 0, 0, 0, 1, 3, 4}, //X
                    {0, 0, 1, 0, 0, 0, 0, 0, 0}}, //Y
            { //ODD
                    {0, 2, 2, 0, 0, 1, 0, 4, 3}, //X
                    {0, 0, 1, 0, 0, 0, 1, 0, 1}}}; //Y
    private static final int[][][] spaceToVertex = { //ADDITIONS
            { //EVEN
                    {0, 0, 0, 0, 3, 1, 1, 2, 2}, //X
                    {0, 0, 0, 0, 0, 0, 1, 0, 1}}, //Y
            { //ODD
                    {0, 0, 0, 0, 3, 1, 1, 2, 2}, //X
                    {0, 0, 0, 1, 1, 0, 1, 0, 1}}}; //Y
    //TODO: finish implementing more translations.
    private static final int[][][] edgeToSpace = { //SUBTRACTIONS
            { //ZERO
                    {0, 0, 0, 0, 0, 3, 0, 0, 0}, //X
                    {0, 0, 0, 0, 0, 1, 0, 0, 0}}, //Y
            { //ONE
                    {0, 0, 0, 0, 0, 0, 4, 0, 1}, //X
                    {0, 0, 0, 0, 0, 0, 0, 0, 0}}, //Y
            { //TWO
                    {0, 0, 0, 0, 0, 0, 0, 0, 0}, //X
                    {0, 1, 0, 0, 0, 0, 0, 0, 0}}, //Y
            { //THREE
                    {0, 0, 0, 0, 0, 0, 3, 0, 0}, //X
                    {0, 0, 0, 0, 0, 0, 0, 1, 0}}, //Y
            { //FOUR
                    {0, 0, 0, 0, 0, 4, 0, 0, 1}, //X
                    {0, 0, 0, 0, 0, 0, 0, 0, 0}}, //Y
            { //FIVE
                    {0, 2, 2, 0, 0, 0, 0, 0, 0}, //X
                    {0, 1, 0, 0, 0, 0, 0, 0, 0}}}; //Y
    private static final int[][][] edgeToVertex = { //ADDITIONS
            { //ZERO
                    {0, 0, 0, 0, 0, 0, 0, 1, 0}, //X
                    {0, 0, 0, 0, 0, 0, 0, 0, 0}}, //Y
            { //ONE
                    {0, 0, 0, 0, 0, 0, 0, 0, 1}, //X
                    {0, 0, 0, 0, 0, 0, 0, 0, 1}}, //Y
            { //TWO
                    {0, 0, 0, 1, 2, 0, 0, 0, 0}, //X
                    {0, 0, 0, 0, 0, 0, 0, 0, 0}}, //Y
            { //THREE
                    {0, 0, 0, 0, 0, 0, 0, 0, 1}, //X
                    {0, 0, 0, 0, 0, 0, 0, 0, 0}}, //Y
            { //FOUR
                    {0, 0, 0, 0, 0, 0, 0, 1, 0}, //X
                    {0, 0, 0, 0, 0, 0, 1, 0, 0}}, //Y
            { //FIVE
                    {0, 0, 0, 1, 2, 0, 0, 0, 0}, //X
                    {0, 0, 0, 0, 0, 0, 0, 0, 0}}}; //Y
    private static final int[][][] vertexToSpace = { //SUBTRACTIONS
            { //ZERO
                    {0, 0, 0, 0, 0, 2, 2, 0, 0}, //X
                    {0, 0, 0, 0, 0, 1, 0, 0, 0}}, //Y
            { //ONE
                    {0, 0, 0, 3, 0, 0, 0, 1, 1}, //X
                    {0, 0, 0, 1, 0, 0, 0, 1, 0}}, //Y
            { //TWO
                    {0, 0, 0, 0, 0, 2, 2, 0, 0}, //X
                    {0, 0, 0, 0, 1, 1, 0, 0, 0}}, //Y
            { //THREE
                    {0, 0, 0, 2, 0, 0, 0, 1, 1}, //X
                    {0, 0, 0, 0, 0, 0, 0, 1, 0}}}; //Y
    private static final int[][][] vertexToEdge = { //ADDITIONS
            { //ZERO
                    {0, 0, 0, -1, 0, 0, 0, 0, 1}, //X
                    {0, 0, 0, 0, 0, 0, 0, 0, 0}}, //Y
            { //ONE
                    {0, 0, 0, 0, 2, 1, 0, 0, 0}, //X
                    {0, 0, 0, 0, 0, -1, 0, 0, 0}}, //Y
            { //TWO
                    {0, 0, 0, -1, 0, 0, 0, 1, 0}, //X
                    {0, 0, 0, 0, 0, 0, 0, -1, 0}}, //Y
            { //THREE
                    {0, 0, 0, 0, 2, 0, 1, 0, 0}, //X
                    {0, 0, 0, 0, 0, 0, 0, 0, 0}}}; //Y

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

    private static Coordinate convertAcross(Coordinate c, Direction d, int divX, int multX, int[][] additions) {
        int outX = c.x / divX;
        int outY = c.y;
        outX *= multX;
        outX += additions[0][d.ordinal()];
        outY += additions[1][d.ordinal()];
        return new Coordinate(outX, outY);
    }

    private static Coordinate convertUp(Coordinate c, Direction d, int multX, int[][] additions) {
        int outX = c.x * multX;
        int outY = c.y;
        outX += additions[0][d.ordinal()];
        outY += additions[1][d.ordinal()];
        return new Coordinate(outX, outY);
    }

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

    public void setTile(Coordinate space, Tile tile) {
        if (tile == null) return;
        tile.setHexArray(this);
        tile.setPosition(space);
        spaces.set(space, tile);
    }

    public void setPath(Coordinate edge, Path path) {
        if (path == null) return;
        path.setHexArray(this);
        path.setPosition(edge);
        edges.set(edge, path);
    }

    public void setTown(Coordinate vertex, Town town) {
        if (town == null) return;
        town.setHexArray(this);
        town.setPosition(vertex);
        vertices.set(vertex, town);
    }

    public Tile getTile(Coordinate space) {
        return spaces.get(space);
    }

    public Path getPath(Coordinate edge) {
        return edges.get(edge);
    }

    public Town getTown(Coordinate vertex) {
        return vertices.get(vertex);
    }

    //Multi-translation functions

    public Map<Direction, Coordinate> getAdjacentSpacesFromSpace(Coordinate space) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getSpaceCoordinateFromSpace(space, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public Map<Direction, Coordinate> getAdjacentEdgesFromEdge(Coordinate edge) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getEdgeCoordinateFromEdge(edge, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public Map<Direction, Coordinate> getAdjacentVerticesFromVertex(Coordinate vertex) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getVertexCoordinateFromVertex(vertex, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public Map<Direction, Coordinate> getAdjacentEdgesFromSpace(Coordinate space) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getEdgeCoordinateFromSpace(space, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public Map<Direction, Coordinate> getAdjacentVerticesFromSpace(Coordinate space) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getVertexCoordinateFromSpace(space, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public Map<Direction, Coordinate> getAdjacentSpacesFromEdge(Coordinate edge) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getSpaceCoordinateFromEdge(edge, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public Map<Direction, Coordinate> getAdjacentVerticesFromEdge(Coordinate edge) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getVertexCoordinateFromEdge(edge, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public Map<Direction, Coordinate> getAdjacentSpacesFromVertex(Coordinate vertex) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getSpaceCoordinateFromVertex(vertex, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    public Map<Direction, Coordinate> getAdjacentEdgesFromVertex(Coordinate vertex) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getEdgeCoordinateFromVertex(vertex, d));
            } catch (Exception e) {
                //Ignore errors, most likely IllegalDirectionExceptions.
            }
        }
        return out;
    }

    //Single translation functions

    public Coordinate getSpaceCoordinateFromSpace(Coordinate space, Direction d) throws IllegalDirectionException {
        checkDirection(d, spaceToSpace[space.x % 2]);
        Coordinate out = convert(space, d, spaceToSpace[space.x % 2]);
        spaces.rangeCheck(out);
        return out;
    }

    public Coordinate getEdgeCoordinateFromEdge(Coordinate edge, Direction d) throws IllegalDirectionException {
        checkDirection(d, edgeToEdge[edge.x % 6]);
        Coordinate out = convert(edge, d, edgeToEdge[edge.x % 6]);
        edges.rangeCheck(out);
        return out;
    }

    public Coordinate getVertexCoordinateFromVertex(Coordinate vertex, Direction d) throws IllegalDirectionException {
        checkDirection(d, vertexToVertex[vertex.x % 4]);
        Coordinate out = convert(vertex, d, vertexToVertex[vertex.x % 4]);
        vertices.rangeCheck(out);
        return out;
    }

    public Coordinate getEdgeCoordinateFromSpace(Coordinate space, Direction d) throws IllegalDirectionException {
        Direction[] validDirections = {Direction.up, Direction.down, Direction.upleft, Direction.upright, Direction.downleft, Direction.downright};
        checkDirection(d, validDirections);
        Coordinate out = convertUp(space, d, 3, spaceToEdge[space.x % 2]);
        edges.rangeCheck(out);
        return out;
    }

    public Coordinate getVertexCoordinateFromSpace(Coordinate space, Direction d) throws IllegalDirectionException {
        Direction[] validDirections = {Direction.left, Direction.right, Direction.upleft, Direction.upright, Direction.downleft, Direction.downright};
        checkDirection(d, validDirections);
        Coordinate out = convertUp(space, d, 2, spaceToVertex[space.x % 2]);
        vertices.rangeCheck(out);
        return out;
    }

    public Coordinate getSpaceCoordinateFromEdge(Coordinate edge, Direction d) throws IllegalDirectionException {
        Direction[][] validDirections = {
                {Direction.upleft, Direction.downright},
                {Direction.downleft, Direction.upright},
                {Direction.up, Direction.down},
                {Direction.downleft, Direction.upright},
                {Direction.upleft, Direction.downright},
                {Direction.up, Direction.down}};
        checkDirection(d, validDirections[edge.x % 6]);
        Coordinate out = convertDown(edge, d, 3, edgeToSpace[edge.x % 6]);
        spaces.rangeCheck(out);
        return out;
    }

    public Coordinate getVertexCoordinateFromEdge(Coordinate edge, Direction d) throws IllegalDirectionException {
        Direction[][] validDirections = {
                {Direction.upright, Direction.downleft},
                {Direction.downright, Direction.upleft},
                {Direction.left, Direction.right},
                {Direction.downright, Direction.upleft},
                {Direction.upright, Direction.downleft},
                {Direction.left, Direction.right}};
        checkDirection(d, validDirections[edge.x % 6]);
        Coordinate out = convertAcross(edge, d, 3, 2, edgeToVertex[edge.x % 6]);
        vertices.rangeCheck(out);
        return out;
    }

    public Coordinate getSpaceCoordinateFromVertex(Coordinate vertex, Direction d) throws IllegalDirectionException {
        Direction[][] validDirections = {
                {Direction.upleft, Direction.downleft, Direction.right},
                {Direction.upright, Direction.downright, Direction.left}};
        checkDirection(d, validDirections[vertex.x % 2]);
        Coordinate out = convertDown(vertex, d, 2, vertexToSpace[vertex.x % 4]);
        spaces.rangeCheck(out);
        return out;
    }

    public Coordinate getEdgeCoordinateFromVertex(Coordinate vertex, Direction d) throws IllegalDirectionException {
        Direction[][] validDirections = {
                {Direction.upright, Direction.downright, Direction.left},
                {Direction.upleft, Direction.downleft, Direction.right}};
        checkDirection(d, validDirections[vertex.x % 2]);
        Coordinate out = convertAcross(vertex, d, 2, 3, vertexToEdge[vertex.x % 4]);
        edges.rangeCheck(out);
        return out;
    }

}

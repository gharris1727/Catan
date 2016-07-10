package catan.common.game.board.hexarray;

import catan.common.util.Direction;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by greg on 4/15/16.
 * A common location for all coordinate transforms used in referencing different locations in a game board.
 */
public class CoordTransforms {

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
    private static final Direction[] spaceToEdgeDirections = new Direction[]{
            Direction.up, Direction.down, Direction.upleft,
            Direction.upright, Direction.downleft, Direction.downright};
    private static final int[][][] spaceToEdge = { //ADDITIONS
            { //EVEN
                    {0, 2, 2, 0, 0, 0, 1, 3, 4}, //X
                    {0, 0, 1, 0, 0, 0, 0, 0, 0}}, //Y
            { //ODD
                    {0, 2, 2, 0, 0, 1, 0, 4, 3}, //X
                    {0, 0, 1, 0, 0, 0, 1, 0, 1}}}; //Y
    private static final Direction[] spaceToVertexDirections = new Direction[]{
            Direction.left, Direction.right, Direction.upleft,
            Direction.upright, Direction.downleft, Direction.downright};
    private static final int[][][] spaceToVertex = { //ADDITIONS
            { //EVEN
                    {0, 0, 0, 0, 3, 1, 1, 2, 2}, //X
                    {0, 0, 0, 0, 0, 0, 1, 0, 1}}, //Y
            { //ODD
                    {0, 0, 0, 0, 3, 1, 1, 2, 2}, //X
                    {0, 0, 0, 1, 1, 0, 1, 0, 1}}}; //Y
    private static final Direction[][] edgeToSpaceDirections = new Direction[][]{
            {Direction.upleft, Direction.downright},
            {Direction.downleft, Direction.upright},
            {Direction.up, Direction.down},
            {Direction.downleft, Direction.upright},
            {Direction.upleft, Direction.downright},
            {Direction.up, Direction.down}};
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
    private static final Direction[][] edgeToVertexDirections = new Direction[][]{
            {Direction.upright, Direction.downleft},
            {Direction.downright, Direction.upleft},
            {Direction.left, Direction.right},
            {Direction.downright, Direction.upleft},
            {Direction.upright, Direction.downleft},
            {Direction.left, Direction.right}};
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
    private static final Direction[][] vertexToSpaceDirections = new Direction[][]{
            {Direction.upleft, Direction.downleft, Direction.right},
            {Direction.upright, Direction.downright, Direction.left}};
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
    private static final Direction[][] vertexToEdgeCoordinates = new Direction[][]{
            {Direction.upright, Direction.downright, Direction.left},
            {Direction.upleft, Direction.downleft, Direction.right}};
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

    private static Coordinate convert(Coordinate c, Direction d, int[][] additions) {
        int outX = c.x + additions[0][d.ordinal()];
        int outY = c.y + additions[1][d.ordinal()];
        return new Coordinate(outX, outY);
    }

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
        for (Direction valid : validDirections)
            if (valid.equals(d)) return;
        throw new IllegalDirectionException(d);
    }

    private static void checkDirection(Direction d, int[][] array) throws IllegalDirectionException {
        //If both indexes are zero, then that indicates that no motion is made.
        //only usable for the 1:1 ratio conversions (space-space, edge-edge, vertex-vertex)
        if (array[0][d.ordinal()] == 0 && array[1][d.ordinal()] == 0)
            throw new IllegalDirectionException(d);
    }

    public static Coordinate getSpaceCoordinateFromSpace(Coordinate space, Direction d) throws IllegalDirectionException {
        checkDirection(d, spaceToSpace[space.x % 2]);
        return convert(space, d, spaceToSpace[space.x % 2]);
    }

    public static Coordinate getEdgeCoordinateFromEdge(Coordinate edge, Direction d) throws IllegalDirectionException {
        checkDirection(d, edgeToEdge[edge.x % 6]);
        return convert(edge, d, edgeToEdge[edge.x % 6]);
    }

    public static Coordinate getVertexCoordinateFromVertex(Coordinate vertex, Direction d) throws IllegalDirectionException {
        checkDirection(d, vertexToVertex[vertex.x % 4]);
        return convert(vertex, d, vertexToVertex[vertex.x % 4]);
    }

    public static Coordinate getEdgeCoordinateFromSpace(Coordinate space, Direction d) throws IllegalDirectionException {
        checkDirection(d, spaceToEdgeDirections);
        return convertUp(space, d, 3, spaceToEdge[space.x % 2]);
    }

    public static Coordinate getVertexCoordinateFromSpace(Coordinate space, Direction d) throws IllegalDirectionException {
        checkDirection(d, spaceToVertexDirections);
        return convertUp(space, d, 2, spaceToVertex[space.x % 2]);
    }

    public static Coordinate getSpaceCoordinateFromEdge(Coordinate edge, Direction d) throws IllegalDirectionException {
        checkDirection(d, edgeToSpaceDirections[edge.x % 6]);
        return convertDown(edge, d, 3, edgeToSpace[edge.x % 6]);
    }

    public static Coordinate getVertexCoordinateFromEdge(Coordinate edge, Direction d) throws IllegalDirectionException {
        checkDirection(d, edgeToVertexDirections[edge.x % 6]);
        return convertAcross(edge, d, 3, 2, edgeToVertex[edge.x % 6]);
    }

    public static Coordinate getSpaceCoordinateFromVertex(Coordinate vertex, Direction d) throws IllegalDirectionException {
        checkDirection(d, vertexToSpaceDirections[vertex.x % 2]);
        return convertDown(vertex, d, 2, vertexToSpace[vertex.x % 4]);
    }

    public static Coordinate getEdgeCoordinateFromVertex(Coordinate vertex, Direction d) throws IllegalDirectionException {
        checkDirection(d, vertexToEdgeCoordinates[vertex.x % 2]);
        return convertAcross(vertex, d, 2, 3, vertexToEdge[vertex.x % 4]);
    }

    public static Map<Direction, Coordinate> getAdjacentSpacesFromSpace(Coordinate space) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getSpaceCoordinateFromSpace(space, d));
            } catch (IllegalDirectionException ignored) { }
        }
        return out;
    }

    public static Map<Direction, Coordinate> getAdjacentEdgesFromEdge(Coordinate edge) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getEdgeCoordinateFromEdge(edge, d));
            } catch (IllegalDirectionException ignored) { }
        }
        return out;
    }

    public static Map<Direction, Coordinate> getAdjacentVerticesFromVertex(Coordinate vertex) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getVertexCoordinateFromVertex(vertex, d));
            } catch (IllegalDirectionException ignored) { }
        }
        return out;
    }

    public static Map<Direction, Coordinate> getAdjacentEdgesFromSpace(Coordinate space) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getEdgeCoordinateFromSpace(space, d));
            } catch (IllegalDirectionException ignored) { }
        }
        return out;
    }

    public static Map<Direction, Coordinate> getAdjacentVerticesFromSpace(Coordinate space) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getVertexCoordinateFromSpace(space, d));
            } catch (IllegalDirectionException ignored) { }
        }
        return out;
    }

    public static Map<Direction, Coordinate> getAdjacentSpacesFromEdge(Coordinate edge) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getSpaceCoordinateFromEdge(edge, d));
            } catch (IllegalDirectionException ignored) { }
        }
        return out;
    }

    public static Map<Direction, Coordinate> getAdjacentVerticesFromEdge(Coordinate edge) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getVertexCoordinateFromEdge(edge, d));
            } catch (IllegalDirectionException ignored) { }
        }
        return out;
    }

    public static Map<Direction, Coordinate> getAdjacentSpacesFromVertex(Coordinate vertex) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getSpaceCoordinateFromVertex(vertex, d));
            } catch (IllegalDirectionException ignored) { }
        }
        return out;
    }

    public static Map<Direction, Coordinate> getAdjacentEdgesFromVertex(Coordinate vertex) {
        Map<Direction, Coordinate> out = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            try {
                out.put(d, getEdgeCoordinateFromVertex(vertex, d));
            } catch (IllegalDirectionException ignored) { }
        }
        return out;
    }
}

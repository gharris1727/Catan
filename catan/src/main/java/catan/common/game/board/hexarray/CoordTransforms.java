package catan.common.game.board.hexarray;

import catan.common.util.Direction;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by greg on 4/15/16.
 * A common location for all coordinate transforms used in referencing different locations in a game board.
 */
public class CoordTransforms {

    private static final List<Direction> spaceToSpaceDirections = Arrays.asList(
            Direction.up, Direction.down, Direction.upleft,
            Direction.downleft, Direction.upright, Direction.downright
        );
    private static final int[][][] spaceToSpace = { //ADDITIONS
            { //EVEN
                    {0, 0, 0, 0, 0, -1, -1, 1, 1}, //X
                    {0, -1, 1, 0, 0, -1, 0, -1, 0}}, //Y
            { //ODD
                    {0, 0, 0, 0, 0, -1, -1, 1, 1,}, //X
                    {0, -1, 1, 0, 0, 0, 1, 0, 1}}}; //Y
    private static final List<List<Direction>> edgeToEdgeDirections = Arrays.asList(
        Arrays.asList(Direction.up, Direction.down, Direction.left, Direction.right),
        Arrays.asList(Direction.up, Direction.down, Direction.left, Direction.right),
        Arrays.asList(Direction.upleft, Direction.downleft, Direction.upright, Direction.downright)
    );
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
    private static final List<List<Direction>> vertexToVertexDirections = Arrays.asList(
        Arrays.asList(Direction.left, Direction.upright, Direction.downright),
        Arrays.asList(Direction.right, Direction.upleft, Direction.downleft)
    );
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
    private static final List<Direction> spaceToEdgeDirections = Arrays.asList(
            Direction.up, Direction.down, Direction.upleft,
            Direction.upright, Direction.downleft, Direction.downright);
    private static final int[][][] spaceToEdge = { //ADDITIONS
            { //EVEN
                    {0, 2, 2, 0, 0, 0, 1, 3, 4}, //X
                    {0, 0, 1, 0, 0, 0, 0, 0, 0}}, //Y
            { //ODD
                    {0, 2, 2, 0, 0, 1, 0, 4, 3}, //X
                    {0, 0, 1, 0, 0, 0, 1, 0, 1}}}; //Y
    private static final List<Direction> spaceToVertexDirections = Arrays.asList(
            Direction.left, Direction.right, Direction.upleft,
            Direction.upright, Direction.downleft, Direction.downright);
    private static final int[][][] spaceToVertex = { //ADDITIONS
            { //EVEN
                    {0, 0, 0, 0, 3, 1, 1, 2, 2}, //X
                    {0, 0, 0, 0, 0, 0, 1, 0, 1}}, //Y
            { //ODD
                    {0, 0, 0, 0, 3, 1, 1, 2, 2}, //X
                    {0, 0, 0, 1, 1, 0, 1, 0, 1}}}; //Y
    private static final List<List<Direction>> edgeToSpaceDirections = Arrays.asList(
            Arrays.asList(Direction.upleft, Direction.downright),
            Arrays.asList(Direction.downleft, Direction.upright),
            Arrays.asList(Direction.up, Direction.down),
            Arrays.asList(Direction.downleft, Direction.upright),
            Arrays.asList(Direction.upleft, Direction.downright),
            Arrays.asList(Direction.up, Direction.down));
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
    private static final List<List<Direction>> edgeToVertexDirections = Arrays.asList(
            Arrays.asList(Direction.upright, Direction.downleft),
            Arrays.asList(Direction.downright, Direction.upleft),
            Arrays.asList(Direction.left, Direction.right),
            Arrays.asList(Direction.downright, Direction.upleft),
            Arrays.asList(Direction.upright, Direction.downleft),
            Arrays.asList(Direction.left, Direction.right));
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
    private static final List<List<Direction>> vertexToSpaceDirections = Arrays.asList(
            Arrays.asList(Direction.upleft, Direction.downleft, Direction.right),
            Arrays.asList(Direction.upright, Direction.downright, Direction.left));
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
    private static final List<List<Direction>> vertexToEdgeDirections = Arrays.asList(
            Arrays.asList(Direction.upright, Direction.downright, Direction.left),
            Arrays.asList(Direction.upleft, Direction.downleft, Direction.right));
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

    public static Coordinate getSpaceCoordinateFromSpace(Coordinate space, Direction d) {
        return spaceToSpaceDirections.contains(d) ?
            convert(space, d, spaceToSpace[space.x % spaceToSpace.length]) : null;
    }

    public static Coordinate getEdgeCoordinateFromEdge(Coordinate edge, Direction d) {
        return edgeToEdgeDirections.get(edge.x % edgeToEdgeDirections.size()).contains(d) ?
            convert(edge, d, edgeToEdge[edge.x % edgeToEdge.length]) : null;
    }

    public static Coordinate getVertexCoordinateFromVertex(Coordinate vertex, Direction d) {
        return vertexToVertexDirections.get(vertex.x % vertexToVertexDirections.size()).contains(d) ?
            convert(vertex, d, vertexToVertex[vertex.x % vertexToVertex.length]) : null;
    }

    public static Coordinate getEdgeCoordinateFromSpace(Coordinate space, Direction d) {
        return spaceToEdgeDirections.contains(d) ?
            convertUp(space, d, 3, spaceToEdge[space.x % spaceToEdge.length]) : null;
    }

    public static Coordinate getVertexCoordinateFromSpace(Coordinate space, Direction d) {
        return spaceToVertexDirections.contains(d) ?
            convertUp(space, d, 2, spaceToVertex[space.x % spaceToVertex.length]) : null;
    }

    public static Coordinate getSpaceCoordinateFromEdge(Coordinate edge, Direction d) {
        return edgeToSpaceDirections.get(edge.x % edgeToSpaceDirections.size()).contains(d) ?
            convertDown(edge, d, 3, edgeToSpace[edge.x % edgeToSpace.length]) : null;
    }

    public static Coordinate getVertexCoordinateFromEdge(Coordinate edge, Direction d) {
        return edgeToVertexDirections.get(edge.x % edgeToVertexDirections.size()).contains(d) ?
            convertAcross(edge, d, 3, 2, edgeToVertex[edge.x % edgeToVertex.length]) : null;
    }

    public static Coordinate getSpaceCoordinateFromVertex(Coordinate vertex, Direction d) {
        return vertexToSpaceDirections.get(vertex.x % vertexToSpaceDirections.size()).contains(d) ?
            convertDown(vertex, d, 2, vertexToSpace[vertex.x % vertexToSpace.length]) : null;
    }

    public static Coordinate getEdgeCoordinateFromVertex(Coordinate vertex, Direction d) {
        return vertexToEdgeDirections.get(vertex.x % vertexToEdgeDirections.size()).contains(d) ?
            convertAcross(vertex, d, 2, 3, vertexToEdge[vertex.x % vertexToEdge.length]) : null;
    }

    public static Map<Direction, Coordinate> getAdjacentSpacesFromSpace(Coordinate space) {
        return spaceToSpaceDirections.stream()
            .collect(Collectors.toMap(d->d, d-> getSpaceCoordinateFromSpace(space, d)));
    }

    public static Map<Direction, Coordinate> getAdjacentEdgesFromEdge(Coordinate edge) {
        return edgeToEdgeDirections.get(edge.x % edgeToEdgeDirections.size()).stream()
            .collect(Collectors.toMap(d->d, d-> getEdgeCoordinateFromEdge(edge, d)));
    }

    public static Map<Direction, Coordinate> getAdjacentVerticesFromVertex(Coordinate vertex) {
        return vertexToVertexDirections.get(vertex.x % vertexToVertexDirections.size()).stream()
            .collect(Collectors.toMap(d->d, d-> getVertexCoordinateFromVertex(vertex, d)));
    }

    public static Map<Direction, Coordinate> getAdjacentEdgesFromSpace(Coordinate space) {
        return spaceToEdgeDirections.stream()
            .collect(Collectors.toMap(d->d, d-> getEdgeCoordinateFromSpace(space, d)));
    }

    public static Map<Direction, Coordinate> getAdjacentVerticesFromSpace(Coordinate space) {
        return spaceToVertexDirections.stream()
            .collect(Collectors.toMap(d->d, d-> getVertexCoordinateFromSpace(space, d)));
    }

    public static Map<Direction, Coordinate> getAdjacentSpacesFromEdge(Coordinate edge) {
        return edgeToSpaceDirections.get(edge.x % edgeToSpaceDirections.size()).stream()
            .collect(Collectors.toMap(d->d, d-> getSpaceCoordinateFromEdge(edge, d)));
    }

    public static Map<Direction, Coordinate> getAdjacentVerticesFromEdge(Coordinate edge) {
        return edgeToVertexDirections.get(edge.x % edgeToVertexDirections.size()).stream()
            .collect(Collectors.toMap(d->d, d-> getVertexCoordinateFromEdge(edge, d)));
    }

    public static Map<Direction, Coordinate> getAdjacentSpacesFromVertex(Coordinate vertex) {
        return vertexToSpaceDirections.get(vertex.x % vertexToSpaceDirections.size()).stream()
            .collect(Collectors.toMap(d->d, d-> getSpaceCoordinateFromVertex(vertex, d)));
    }

    public static Map<Direction, Coordinate> getAdjacentEdgesFromVertex(Coordinate vertex) {
        return vertexToEdgeDirections.get(vertex.x % vertexToEdgeDirections.size()).stream()
            .collect(Collectors.toMap(d->d, d-> getEdgeCoordinateFromVertex(vertex, d)));
    }
}

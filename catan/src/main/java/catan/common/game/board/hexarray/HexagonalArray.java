package catan.common.game.board.hexarray;

import catan.common.game.board.paths.Path;
import catan.common.game.board.tiles.Tile;
import catan.common.game.board.towns.Town;

import java.util.Set;

/**
 * Created by Greg on 8/8/2014.
 * Map system for storing board data.
 */
public class HexagonalArray {

    private final TwoDimensionalArray<Tile> spaces;
    private final TwoDimensionalArray<Path> edges;
    private final TwoDimensionalArray<Town> vertices;

    public HexagonalArray(int x, int y) {
        spaces = new TwoDimensionalArray<>(x, y); // Number of spaces in a hex map is x * y
        edges = new TwoDimensionalArray<>(3 * (x + 1), y + 1); // number of edges in a hex map is less than 4x * y+1
        vertices = new TwoDimensionalArray<>(2 * (x + 1), y + 1); // number of vertices in a hex map is less than 2(x+1) * y+1
    }

    public Set<Coordinate> getSpaceCoordinates() {
        return spaces.coordinates();
    }

    public Set<Coordinate> getEdgeCoordinates() {
        return edges.coordinates();
    }

    public Set<Coordinate> getVertexCoordinates() {
        return vertices.coordinates();
    }

    public void setTile(Coordinate space, Tile tile) {
        if (tile == null) return;
        tile.setPosition(space);
        spaces.set(space, tile);
    }

    public void setPath(Coordinate edge, Path path) {
        if (path == null) return;
        path.setPosition(edge);
        edges.set(edge, path);
    }

    public void setTown(Coordinate vertex, Town town) {
        if (town == null) return;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        HexagonalArray other = (HexagonalArray) o;

        if (!spaces.equals(other.spaces)) return false;
        if (!edges.equals(other.edges)) return false;
        return vertices.equals(other.vertices);

    }

    @Override
    public int hashCode() {
        int result = spaces.hashCode();
        result = 31 * result + edges.hashCode();
        result = 31 * result + vertices.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "HexagonalArray{" +
                "spaces=" + spaces +
                ", edges=" + edges +
                ", vertices=" + vertices +
                '}';
    }
}

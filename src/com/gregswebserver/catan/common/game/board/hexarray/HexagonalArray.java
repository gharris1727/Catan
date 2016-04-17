package com.gregswebserver.catan.common.game.board.hexarray;

import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.towns.Town;

/**
 * Created by Greg on 8/8/2014.
 * Map system for storing board data.
 */
public class HexagonalArray {

    public final TwoDimensionalArray<Tile> spaces;
    public final TwoDimensionalArray<Path> edges;
    public final TwoDimensionalArray<Town> vertices;

    public HexagonalArray(int x, int y) {
        spaces = new TwoDimensionalArray<>(x, y); // Number of spaces in a hex map is x * y
        edges = new TwoDimensionalArray<>(3 * (x + 1), y + 1); // number of edges in a hex map is less than 4x * y+1
        vertices = new TwoDimensionalArray<>(2 * (x + 1), y + 1); // number of vertices in a hex map is less than 2(x+1) * y+1
    }

    public Tile setTile(Coordinate space, Tile tile) {
        if (tile == null) return null;
        tile.setHexArray(this);
        tile.setPosition(space);
        spaces.set(space, tile);
        return tile;
    }

    public Path setPath(Coordinate edge, Path path) {
        if (path == null) return null;
        path.setHexArray(this);
        path.setPosition(edge);
        edges.set(edge, path);
        return path;
    }

    public Town setTown(Coordinate vertex, Town town) {
        if (town == null) return null;
        town.setHexArray(this);
        town.setPosition(vertex);
        vertices.set(vertex, town);
        return town;
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
        if (o == null || getClass() != o.getClass()) return false;

        HexagonalArray that = (HexagonalArray) o;

        if (!spaces.equals(that.spaces)) return false;
        if (!edges.equals(that.edges)) return false;
        return vertices.equals(that.vertices);

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

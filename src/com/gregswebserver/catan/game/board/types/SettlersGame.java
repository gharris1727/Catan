package com.gregswebserver.catan.game.board.types;

import com.gregswebserver.catan.game.board.locations.Edge;
import com.gregswebserver.catan.game.board.locations.Space;
import com.gregswebserver.catan.game.board.locations.Vertex;

import java.util.HashSet;

/**
 * Created by Greg on 8/8/2014.
 * Contains all of the locations for a normal Settlers of Catan game.
 */
public class SettlersGame {

    tiles[][] map = {
            {tiles.ocean, tiles.ocean, tiles.ocean, tiles.ocean, tiles.ocean, tiles.ocean, tiles.ocean},
            {tiles.ocean, tiles.ocean, tiles.ocean, tiles.hexes, tiles.ocean, tiles.ocean, tiles.ocean},
            {tiles.ocean, tiles.ocean, tiles.hexes, tiles.ocean, tiles.hexes, tiles.ocean, tiles.ocean},
            {tiles.ocean, tiles.hexes, tiles.ocean, tiles.hexes, tiles.ocean, tiles.hexes, tiles.ocean},
            {tiles.ocean, tiles.ocean, tiles.hexes, tiles.ocean, tiles.hexes, tiles.ocean, tiles.ocean},
            {tiles.ocean, tiles.hexes, tiles.ocean, tiles.hexes, tiles.ocean, tiles.hexes, tiles.ocean},
            {tiles.ocean, tiles.ocean, tiles.hexes, tiles.ocean, tiles.hexes, tiles.ocean, tiles.ocean},
            {tiles.ocean, tiles.hexes, tiles.ocean, tiles.hexes, tiles.ocean, tiles.hexes, tiles.ocean},
            {tiles.ocean, tiles.ocean, tiles.hexes, tiles.ocean, tiles.hexes, tiles.ocean, tiles.ocean},
            {tiles.ocean, tiles.ocean, tiles.ocean, tiles.hexes, tiles.ocean, tiles.ocean, tiles.ocean},
            {tiles.ocean, tiles.ocean, tiles.ocean, tiles.ocean, tiles.ocean, tiles.ocean, tiles.ocean}
    };
    HashSet<Space> spaces;
    HashSet<Edge> edges;
    HashSet<Vertex> vertexes;

    public SettlersGame() {
    }

    HashSet<Space> getSpaces() {
        return spaces;
    }

    HashSet<Edge> getEdges() {
        return edges;
    }

    HashSet<Vertex> getVertexes() {
        return vertexes;
    }

    private enum tiles {
        hexes,
        trade,
        ocean
    }
}

package com.gregswebserver.catan.game.board;

import com.gregswebserver.catan.game.board.buildings.Building;
import com.gregswebserver.catan.game.board.locations.Edge;
import com.gregswebserver.catan.game.board.locations.Space;
import com.gregswebserver.catan.game.board.locations.Vertex;
import com.gregswebserver.catan.game.board.paths.Path;
import com.gregswebserver.catan.game.board.tiles.Tile;

import java.util.HashMap;

/**
 * Created by Greg on 8/8/2014.
 * Main board to save the state of the game.
 */
public class GameBoard {

    private HashMap<Edge, Path> paths;
    private HashMap<Space, Tile> tiles;
    private HashMap<Vertex, Building> buildings;

    public GameBoard() {

    }


}

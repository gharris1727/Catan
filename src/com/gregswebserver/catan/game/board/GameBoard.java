package com.gregswebserver.catan.game.board;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.game.board.buildings.Building;
import com.gregswebserver.catan.game.board.hexarray.HexagonalArray;
import com.gregswebserver.catan.game.board.paths.Path;
import com.gregswebserver.catan.game.board.tiles.Tile;

/**
 * Created by Greg on 8/10/2014.
 * Game Board handling all different functions with adding and moving pieces.
 */
public class GameBoard {

    public HexagonalArray<Tile, Path, Building> hexArray;
    private int numPlayers;
    private Graphic graphic;

    public GameBoard() {
    }

    public void init(int x, int y, int players) {
        hexArray = new HexagonalArray<>(Tile.class, Path.class, Building.class, x, y);
        this.numPlayers = players;
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public void render() {
        graphic = new Graphic(0, 0);
        //Render the entire board.
        //Only used for first-time render, all other renders are modifications to the original.
        //TODO: render the map.
    }

    public void updateRender() {
        //Render only the sections of the map that need re-rendering. Leave everything else alone.
        //TODO: implement rerendering.
    }
}

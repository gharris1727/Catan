package com.gregswebserver.catan.client.input.clickables;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.tiles.Tile;

/**
 * Created by Greg on 8/21/2014.
 * represents a clickable tile on the game board.
 */
public class ClickableTile extends ClickableBoardObject {

    private Tile tile;

    public ClickableTile(Coordinate position, Clickable mapDrag, Tile tile) {
        super(position, mapDrag);
        this.tile = tile;
    }

    public String toString() {
        return super.toString() + " Tile: " + tile;
    }
}

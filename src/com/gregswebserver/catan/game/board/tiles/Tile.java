package com.gregswebserver.catan.game.board.tiles;

import com.gregswebserver.catan.game.cards.Resource;

/**
 * Created by Greg on 8/8/2014.
 * Generic hex tile on the Catan GameBoard.
 */
public class Tile {

    private Terrain terrain;
    private int diceNumber;
    private boolean robber;

    public Tile(Terrain terrain, int diceNumber) {
        this.terrain = terrain;
        this.diceNumber = diceNumber;
        this.robber = false;
    }

    public void placeRobber() {
        robber = true;
    }

    public void removeRobber() {
        robber = false;
    }

    public Resource getResource() {
        if (robber) return null;
        return terrain.getResource();
    }

    public int getDiceNumber() {
        return diceNumber;
    }
}

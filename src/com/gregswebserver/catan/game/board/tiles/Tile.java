package com.gregswebserver.catan.game.board.tiles;

import com.gregswebserver.catan.game.cards.Resource;
import com.gregswebserver.catan.game.gameplay.DiceRoll;

/**
 * Created by Greg on 8/8/2014.
 * Generic hex tile on the Catan GameBoard.
 */
public class Tile {

    private Terrain terrain;
    private DiceRoll diceRoll;
    private boolean robber;

    public Tile(Terrain terrain, DiceRoll diceRoll) {
        this.terrain = terrain;
        this.diceRoll = diceRoll;
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

    public DiceRoll getDiceRoll() {
        return diceRoll;
    }
}

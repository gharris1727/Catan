package com.gregswebserver.catan.game.board.tiles;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.game.board.BoardObject;
import com.gregswebserver.catan.game.gameplay.DiceRoll;
import com.gregswebserver.catan.game.gameplay.enums.Resource;

/**
 * Created by Greg on 8/8/2014.
 * Generic hex tile on the Catan GameBoard.
 */
public class Tile extends BoardObject {

    private DiceRoll diceRoll;
    private Terrain terrain;
    private boolean robber;

    public Tile(Terrain terrain) {
        this.terrain = terrain;
        this.robber = false;
    }

    public DiceRoll getDiceRoll() {
        return diceRoll;
    }

    public void setDiceRoll(DiceRoll diceRoll) {
        this.diceRoll = diceRoll;
    }

    public void placeRobber() {
        robber = true;
    }

    public void removeRobber() {
        robber = false;
    }

    public boolean hasRobber() {
        return robber;
    }

    public Resource getResource() {
        if (robber || terrain == null) return null;
        return terrain.resource;
    }

    public Graphic getGraphic() {
        return terrain.image;
    }
}

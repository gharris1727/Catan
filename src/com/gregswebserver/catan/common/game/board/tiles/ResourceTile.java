package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.Resource;
import com.gregswebserver.catan.common.game.gameplay.enums.Terrain;

/**
 * Created by Greg on 8/22/2014.
 * A tile for producing resources on the map.
 */
public class ResourceTile extends Tile {

    private DiceRoll diceRoll;
    private Terrain terrain;
    private boolean robber;

    public ResourceTile(Terrain terrain, DiceRoll diceRoll) {
        this.terrain = terrain;
        this.diceRoll = diceRoll;
    }

    public DiceRoll getDiceRoll() {
        return diceRoll;
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

    public String toString() {
        return "ResourceTile " + terrain;
    }
}

package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.GameResource;
import com.gregswebserver.catan.common.game.gameplay.enums.Terrain;

import java.awt.*;

/**
 * Created by Greg on 8/22/2014.
 * A tile for producing resources on the map.
 */
public class ResourceTile extends Tile {

    private final DiceRoll diceRoll;
    private final Terrain terrain;
    private Graphic graphic;
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
        graphic = null;
    }

    public void removeRobber() {
        robber = false;
        graphic = null;
    }

    public boolean hasRobber() {
        return robber;
    }

    public GameResource getResource() {
        if (robber || terrain == null) return null;
        return terrain.gameResource;
    }

    public String toString() {
        return "ResourceTile " + terrain;
    }

    @Override
    public Graphic getGraphic() {
        if (graphic == null) {
            graphic = new Graphic(tileMask);
            terrain.gameResource.getGraphic().renderTo(graphic, new Point(), 0);
            diceRoll.getGraphic().renderTo(graphic, diceRoll.getOffset(), 0);
            //Figure out how to render the robber.
        }
        return graphic;
    }
}

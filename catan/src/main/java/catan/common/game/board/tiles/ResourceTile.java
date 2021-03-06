package catan.common.game.board.tiles;

import catan.common.game.board.Terrain;
import catan.common.game.gamestate.DiceRoll;
import catan.common.game.util.GameResource;

/**
 * Created by Greg on 8/22/2014.
 * A tile for producing resources on the map.
 */
public class ResourceTile extends Tile {

    private final DiceRoll diceRoll;
    private final Terrain terrain;
    private boolean robber;

    public ResourceTile(Terrain terrain, DiceRoll diceRoll) {
        this.terrain = terrain;
        this.diceRoll = diceRoll;
        robber = false;
    }

    public Terrain getTerrain() {
        return terrain;
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

    public GameResource getResource() {
        if (robber || (terrain == null)) return null;
        return terrain.gameResource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceTile)) return false;

        ResourceTile other = (ResourceTile) o;

        if (robber != other.robber) return false;
        if (diceRoll != other.diceRoll) return false;
        return terrain == other.terrain;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + diceRoll.hashCode();
        result = (31 * result) + terrain.hashCode();
        result = (31 * result) + (robber ? 1 : 0);
        return result;
    }

    public String toString() {
        return "ResourceTile " + terrain;
    }
}

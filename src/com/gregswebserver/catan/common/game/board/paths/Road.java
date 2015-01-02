package com.gregswebserver.catan.common.game.board.paths;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.game.player.Player;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of Path, rests on the edges of hexagons.
 */
public class Road extends Path implements Tradeable {

    public Road(Player owner) {
        super(owner);
    }

    public Graphic getGraphic() {
        return getOwner().getTeam().paths[getPosition().x % 3];
    }

    public String toString() {
        return "Road: " + getOwner();
    }
}

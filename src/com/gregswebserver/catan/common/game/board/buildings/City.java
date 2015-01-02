package com.gregswebserver.catan.common.game.board.buildings;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.game.player.Player;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of building that gives two resources per roll.
 */
public class City extends Building implements Tradeable {

    public City(Player owner) {
        super(owner);
    }

    public int getResourceNumber() {
        return 2;
    }

    public Graphic getGraphic() {
        return getOwner().getTeam().city[getPosition().x % 2];
    }

    public String toString() {
        return "City: " + getOwner();
    }
}

package com.gregswebserver.catan.game.board.buildings;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.game.player.Player;

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

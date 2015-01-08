package com.gregswebserver.catan.common.game.board.buildings;

import com.gregswebserver.catan.common.game.gameplay.VictoryFactor;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.game.player.Player;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of building that gives two resources per roll.
 */
public class City extends Building implements Tradeable, VictoryFactor {

    public City(Player owner) {
        super(owner);
    }

    public int getResourceNumber() {
        return 2;
    }

    public int getVictoryPoints() {
        return 2;
    }

    public String toString() {
        return "City: " + getOwner();
    }
}

package com.gregswebserver.catan.common.game.board.buildings;

import com.gregswebserver.catan.common.game.gameplay.VictoryFactor;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.game.player.Player;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of Building that receives one resource per roll.
 */
public class Settlement extends Building implements Tradeable, VictoryFactor {

    public Settlement(Player owner) {
        super(owner);
    }

    public int getResourceNumber() {
        return 1;
    }

    public int getVictoryPoints() {
        return 1;
    }

    public String toString() {
        return "Settlement: " + getOwner();
    }

}

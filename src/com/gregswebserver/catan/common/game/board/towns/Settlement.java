package com.gregswebserver.catan.common.game.board.towns;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.common.game.gameplay.VictoryFactor;
import com.gregswebserver.catan.common.game.player.Team;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of Building that receives one resource per roll.
 */
public class Settlement extends Town implements VictoryFactor {

    public Settlement(Team owner) {
        super(owner);
    }

    public int getResourceNumber() {
        return 1;
    }

    public int getVictoryPoints() {
        return 1;
    }

    public String toString() {
        return "Settlement: " + getTeam();
    }

    public Graphic getGraphic() {
        return getTeam().getSettlementGraphic();
    }
}

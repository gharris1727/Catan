package com.gregswebserver.catan.common.game.board.towns;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.common.game.gameplay.VictoryFactor;
import com.gregswebserver.catan.common.game.player.Team;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of building that gives two resources per roll.
 */
public class City extends Town implements VictoryFactor {

    public City(Team owner) {
        super(owner);
    }

    public int getResourceNumber() {
        return 2;
    }

    public int getVictoryPoints() {
        return 2;
    }

    public String toString() {
        return "City: " + getTeam();
    }

    public Graphic getGraphic() {
        return getTeam().getCityGraphic();
    }
}

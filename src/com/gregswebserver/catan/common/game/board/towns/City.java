package com.gregswebserver.catan.common.game.board.towns;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.common.game.player.Team;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of building that gives two resources per roll.
 */
public class City extends Town {

    public City(Team owner) {
        super(owner);
    }

    public String toString() {
        return "City: " + getTeam();
    }

    @Override
    public Graphic getGraphic() {
        return getTeam().getCityGraphic();
    }
}

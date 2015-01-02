package com.gregswebserver.catan.common.game.board.buildings;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.common.game.player.Team;

/**
 * Created by Greg on 8/9/2014.
 * Ocean building object to prevent players from building off the map.
 */
public class OceanBuilding extends Building {


    public OceanBuilding() {
        super(null);
    }

    public Graphic getGraphic() {
        return Team.Ocean.settlement[getPosition().x % 2];
    }

    public int getResourceNumber() {
        return 0;
    }

    public String toString() {
        return "OceanBuilding";
    }
}

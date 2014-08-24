package com.gregswebserver.catan.game.board.paths;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.game.player.Team;

/**
 * Created by Greg on 8/9/2014.
 * Ocean path object to prevent players from building off the map.
 */
public class OceanPath extends Path {

    public OceanPath() {
        super(null);
    }

    public Graphic getGraphic() {
        return Team.Ocean.paths[getPosition().x % 3];
    }

    public String toString() {
        return "OceanPath";
    }
}

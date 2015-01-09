package com.gregswebserver.catan.common.game.board.paths;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.common.game.player.Team;
import com.gregswebserver.catan.common.game.player.TeamOwned;

/**
 * Created by Greg on 8/8/2014.
 * Superclass of all object placed on the edges of hexagons, connecting vertexes.
 */
public abstract class Path extends TeamOwned {

    public Path(Team owner) {
        super(owner);
    }

    public Graphic getGraphic() {
        int orientation = getHexArray().getEdgeOrientation(getPosition());
        return getTeam().getRoadGraphic(orientation);
    }

}

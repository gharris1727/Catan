package com.gregswebserver.catan.common.game.board.towns;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.common.game.player.Team;
import com.sun.istack.internal.NotNull;

/**
 * Created by Greg on 8/9/2014.
 * Ocean building object to prevent players from building off the map.
 */
public class EmptyTown extends Town {

    public EmptyTown() {
        super(Team.None);
    }

    @Override
    public int getResourceNumber() {
        return 0;
    }

    public String toString() {
        return "Empty Building";
    }

    @NotNull
    @Override
    public Graphic getGraphic() {
        return getTeam().getSettlementGraphic();
    }
}

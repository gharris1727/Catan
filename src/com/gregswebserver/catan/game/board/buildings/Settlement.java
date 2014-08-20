package com.gregswebserver.catan.game.board.buildings;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.game.player.Player;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of Building that receives one resource per roll.
 */
public class Settlement extends Building implements Tradeable {

    public Settlement(Player owner) {
        super(owner);
    }

    public int getResourceNumber() {
        return 2;
    }

    public Graphic getGraphic() {
        return getOwner().getTeam().settlement[getPosition().x % 2];
    }
}

package com.gregswebserver.catan.common.game.gameplay.trade;

import com.gregswebserver.catan.common.game.util.GameResource;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Greg on 8/13/2014.
 * Class containing information about the trades offered at a trading post.
 * Used in a HashMap where two locations point to the same object.
 */
public enum TradingPostType {

    Brick(GameResource.Brick),
    Lumber(GameResource.Lumber),
    Wool(GameResource.Wool),
    Ore(GameResource.Ore),
    Grain(GameResource.Grain),
    Wildcard(null);

    private final GameResource gameResource;
    private final Set<Trade> trades;

    TradingPostType(GameResource r) {
        gameResource = r;
        trades = new HashSet<>();
        for (GameResource request : GameResource.values()) {
            if (r == null) {
                for (GameResource offer : GameResource.values())
                    trades.add(new Trade(offer, request, 3));
            } else {
                trades.add(new Trade(r, request, 2));
            }
        }
    }

    @Override
    public String toString() {
        if (gameResource != null)
            return gameResource.toString();
        return "Any";
    }

    public Set<Trade> getTrades() {
        return trades;
    }
}

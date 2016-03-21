package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.common.game.gameplay.trade.PermanentTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;

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

    public final GameResource gameResource;
    public final Set<Trade> trades;

    TradingPostType(GameResource r) {
        gameResource = r;
        trades = new HashSet<>();
        for (GameResource source : GameResource.values()) {
            if (r == null) {
                for (GameResource target : GameResource.values())
                    trades.add(new PermanentTrade(source, target, 3));
            } else {
                trades.add(new PermanentTrade(source, r, 2));
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

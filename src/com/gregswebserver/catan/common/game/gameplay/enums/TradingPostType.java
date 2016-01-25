package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.gameplay.trade.Trader;

import java.util.HashSet;

/**
 * Created by Greg on 8/13/2014.
 * Class containing information about the trades offered at a trading post.
 * Used in a HashMap where two locations point to the same object.
 */
public enum TradingPostType implements Trader {

    Brick(GameResource.Brick),
    Lumber(GameResource.Lumber),
    Wool(GameResource.Wool),
    Grain(GameResource.Grain),
    Ore(GameResource.Ore),
    Wildcard(null);

    private final HashSet<Trade> trades;
    private final GameResource gameResource;

    TradingPostType(GameResource r) {
        gameResource = r;
        trades = new HashSet<>();
        for (GameResource a : GameResource.values()) {
            if (r == null) {
                //Trade any 3 of a resource for any other.
                for (GameResource b : GameResource.values()) {
                    Trade trade = new Trade();
                    trade.request.put(a, 3);
                    trade.offer.put(b, 1);
                    trades.add(trade);
                }
            } else {
                //Trade two of any resource for the specified one.
                Trade trade = new Trade();
                trade.request.put(a, 2);
                trade.offer.put(r, 1);
                trades.add(trade);
            }
        }
    }

    public GameResource getGameResource() {
        return gameResource;
    }
}

package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.gameplay.trade.Trader;

import java.util.HashSet;

/**
 * Created by Greg on 8/13/2014.
 * Class containing information about the trades offered at a trading post.
 * Used in a HashMap where two locations point to the same object.
 */
public enum TradingPost implements Trader {

    Brick(Resource.Brick),
    Lumber(Resource.Lumber),
    Wool(Resource.Wool),
    Grain(Resource.Grain),
    Ore(Resource.Ore),
    Wildcard(null);

    private HashSet<Trade> trades;
    private Resource resource;

    private TradingPost(Resource r) {
        resource = r;
        trades = new HashSet<>();
        for (Resource a : Resource.values()) {
            if (r == null) {
                //Trade any 3 of a resource for any other.
                for (Resource b : Resource.values()) {
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

    public Resource getResource() {
        return resource;
    }
}

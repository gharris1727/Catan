package com.gregswebserver.catan.common.game.gameplay.trade;

import com.gregswebserver.catan.common.game.gameplay.enums.Resource;

import java.util.HashSet;

/**
 * Created by Greg on 8/13/2014.
 * Game banker, acts as a trading entity for all players.
 */
public class Bank implements Trader {

    private final HashSet<Trade> trades;

    public Bank() {
        //Add all 4-to-1 trading possibilities.
        trades = new HashSet<>();
        for (Resource a : Resource.values()) {
            for (Resource b : Resource.values()) {
                if (!a.equals(b)) {
                    Trade trade = new Trade();
                    trade.request.put(a, 4);
                    trade.offer.put(b, 1);
                    trades.add(trade);
                }
            }
        }
    }
}

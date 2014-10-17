package com.gregswebserver.catan.common.game.gameplay.trade;

import com.gregswebserver.catan.common.game.gameplay.enums.Purchase;
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

        Trade road = new Trade();
        road.request.put(Resource.Brick, 1);
        road.request.put(Resource.Lumber, 1);
        road.offer.put(Purchase.Road, 1);
        trades.add(road);

        Trade settlement = new Trade();
        settlement.request.put(Resource.Brick, 1);
        settlement.request.put(Resource.Lumber, 1);
        settlement.request.put(Resource.Grain, 1);
        settlement.request.put(Resource.Wool, 1);
        settlement.offer.put(Purchase.Settlement, 1);
        trades.add(settlement);

        Trade city = new Trade();
        city.request.put(Resource.Grain, 2);
        city.request.put(Resource.Ore, 3);
        city.offer.put(Purchase.City, 1);
        trades.add(city);

        Trade devCard = new Trade();
        devCard.request.put(Resource.Wool, 1);
        devCard.request.put(Resource.Grain, 1);
        devCard.request.put(Resource.Ore, 1);
        devCard.offer.put(Purchase.DevelopmentCard, 1);
        trades.add(devCard);

    }

    //can offer a trade if it was created by the Bank.
    public boolean canOffer(Trade t) {
        return trades.contains(t);
    }

    //The bank cannot accept any other trades.
    public boolean canFillRequest(Trade t) {
        return false;
    }

    public HashSet<Trade> getTrades() {
        return null;
    }
}

package com.gregswebserver.catan.common.game.gameplay.trade;

import java.util.HashMap;

/**
 * Created by Greg on 8/13/2014.
 * Trading framework with sub-types of trading.
 */
public class Trade {

    public HashMap<Tradeable, Integer> offer;
    public HashMap<Tradeable, Integer> request;

    public Trade() {
        offer = new HashMap<>();
        request = new HashMap<>();
    }

    public int hashCode() {
        return offer.hashCode() + request.hashCode();
    }
}

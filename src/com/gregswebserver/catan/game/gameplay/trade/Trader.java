package com.gregswebserver.catan.game.gameplay.trade;

import java.util.HashSet;

/**
 * Created by Greg on 8/13/2014.
 * Indicates that this object can make trades with other Traders.
 */
public interface Trader {

    public boolean canOffer(Trade t);

    public boolean canFillRequest(Trade t);

    public HashSet<Trade> getTrades();
}

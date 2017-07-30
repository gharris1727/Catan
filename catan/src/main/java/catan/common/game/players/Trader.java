package catan.common.game.players;

import catan.common.game.gameplay.trade.Trade;

import java.util.Set;

/**
 * Created by greg on 7/29/17.
 * An entity that can participate in trades.
 */
public interface Trader {

    Set<Trade> getTrades();

    boolean canMakeTrade(Trade t);
}

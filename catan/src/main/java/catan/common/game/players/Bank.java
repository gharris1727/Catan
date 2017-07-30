package catan.common.game.players;

import catan.common.game.gameplay.trade.Trade;
import catan.common.game.util.GameResource;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by greg on 6/26/16.
 * A Player object that represents the bank, for purposes of trading.
 */
public class Bank implements Trader {

    private final Set<Trade> trades;

    public Bank() {
        Set<Trade> bankTrades = new TreeSet<>();
        for (GameResource target : GameResource.values())
            for (GameResource source : GameResource.values())
                if (target != source)
                    bankTrades.add(new Trade(source, target, 4));
        trades = Collections.unmodifiableSet(bankTrades);
    }

    @Override
    public Set<Trade> getTrades() {
        return trades;
    }

    @Override
    public boolean canMakeTrade(Trade t) {
        return trades.contains(t);
    }

}

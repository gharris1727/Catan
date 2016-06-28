package com.gregswebserver.catan.common.game.players;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.gamestate.DevelopmentCard;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.game.test.EqualityException;
import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.game.util.GameResource;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by greg on 6/26/16.
 * Implmentation of the Bank player.
 */
public class Bank implements Player {

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
    public Username getName() {
        return null;
    }

    @Override
    public TeamColor getTeamColor() {
        return null;
    }

    @Override
    public EnumCounter<GameResource> getInventory() {
        return null;
    }

    @Override
    public EnumCounter<DevelopmentCard> getBoughtCards() {
        return null;
    }

    @Override
    public EnumCounter<DevelopmentCard> getDevelopmentCards() {
        return null;
    }

    @Override
    public Set<Trade> getTrades() {
        return trades;
    }

    @Override
    public boolean canMakeTrade(Trade t) {
        return false;
    }

    @Override
    public int getDiscardCount() {
        return 0;
    }

    @Override
    public void undo() throws EventConsumerException {

    }

    @Override
    public void test(PlayerEvent event) throws EventConsumerException {

    }

    @Override
    public void execute(PlayerEvent event) throws EventConsumerException {

    }

    @Override
    public void assertEquals(Player other) throws EqualityException {
        if (this == other)
            return;
        if (other == null)
            throw new EqualityException("BankObject", this, null);
        if (getClass() != other.getClass())
            throw new EqualityException("BankClass", getClass(), other.getClass());

        Bank o = (Bank) other;

        if (!trades.equals(o.trades))
            throw new EqualityException("BankTrades", trades, o.trades);
    }
}

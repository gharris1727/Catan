package catan.common.game.players;

import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.game.gameplay.trade.Trade;
import catan.common.game.gamestate.DevelopmentCard;
import catan.common.game.teams.TeamColor;
import catan.common.game.util.EnumCounter;
import catan.common.game.util.GameResource;

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
        return trades.contains(t);
    }

    @Override
    public int getDiscardCount() {
        return 0;
    }

    @Override
    public void undo() throws EventConsumerException {
        //There is no state to keep track of
    }

    @Override
    public EventConsumerProblem test(PlayerEvent event) {
        switch (event.getType()) {
            case Gain_Resources:
                break;
            case Discard_Resources:
                break;
            case Lose_Resources:
                break;
            case Gain_DevelopmentCard:
                break;
            case Mature_DevelopmentCards:
                break;
            case Use_DevelopmentCard:
                break;
            case Offer_Trade:
                break;
            case Use_Trade:
                //noinspection SuspiciousMethodCalls
                if (!trades.contains(event.getPayload()))
                    return new EventConsumerProblem("Invalid Bank Trade");
                break;
            case Cancel_Trade:
                break;
            case Finish_Discarding:
                break;
        }
        return null;
    }

    @Override
    public void execute(PlayerEvent event) throws EventConsumerException {
        EventConsumerProblem problem = test(event);
        if (problem != null)
            throw new EventConsumerException(event, problem);
    }

}

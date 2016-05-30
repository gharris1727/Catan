package com.gregswebserver.catan.common.game.players;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
import com.gregswebserver.catan.common.game.gameplay.trade.TemporaryTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.gamestate.DevelopmentCard;
import com.gregswebserver.catan.common.game.scoring.rules.GameRules;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.game.util.GameResource;

import java.io.Serializable;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Greg on 8/8/2014.
 * A player in a game of catan, stores resource accounts, victory points, and can make moves on the catan game.
 */
@SuppressWarnings("unchecked")
public class Player implements Serializable, ReversibleEventConsumer<PlayerEvent> {

    private final Username name;
    private final TeamColor teamColor;
    private final EnumCounter<GameResource> inventory;
    private final EnumCounter<DevelopmentCard> bought;
    private final EnumCounter<DevelopmentCard> active;
    private final EnumCounter<DevelopmentCard> used;
    private final EnumCounter<Purchase> purchases;
    private final Stack<PlayerEvent> history;
    private TemporaryTrade trade;

    public Player(Username name, TeamColor teamColor) {
        this.name = name;
        this.teamColor = teamColor;
        inventory = new EnumCounter<>(GameResource.class);
        bought = new EnumCounter<>(DevelopmentCard.class);
        active = new EnumCounter<>(DevelopmentCard.class);
        used = new EnumCounter<>(DevelopmentCard.class);
        purchases = new EnumCounter<>(Purchase.class);
        history = new Stack<>();
        trade = null;
    }

    public Username getName() {
        return name;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }

    public EnumCounter<GameResource> getInventory() {
        return inventory;
    }

    public TemporaryTrade getTrade() {
        return trade;
    }

    public int getVictoryPoints(GameRules rules) {
        int victoryPoints = 0;
        for (Map.Entry<DevelopmentCard, Integer> entry : used)
            victoryPoints += rules.getDevelopmentCardPoints(entry.getKey()) * entry.getValue();
        for (Map.Entry<DevelopmentCard, Integer> entry : active)
            victoryPoints += rules.getDevelopmentCardPoints(entry.getKey()) * entry.getValue();
        for (Map.Entry<DevelopmentCard, Integer> entry : bought)
            victoryPoints += rules.getDevelopmentCardPoints(entry.getKey()) * entry.getValue();
        return victoryPoints;
    }

    public boolean canMakeTrade(Trade t) {
        for (GameResource r : GameResource.values())
            if (!inventory.contains(r, t.request.get(r)))
                return false;
        return true;
    }

    public PlayerEvent getMatureEvent() {
        return new PlayerEvent(name, PlayerEventType.Mature_DevelopmentCards, new EnumCounter<>(bought));
    }

    @Override
    public void undo() throws EventConsumerException {
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        PlayerEvent event = history.pop();
        try {
            switch (event.getType()) {
                case Gain_Resources:
                    EnumCounter<GameResource> income = (EnumCounter<GameResource>) event.getPayload();
                    for (GameResource r : GameResource.values()) {
                        inventory.decrement(r, income.get(r));
                    }
                    break;
                case Make_Purchase:
                    Purchase purchase = (Purchase) event.getPayload();
                    for (GameResource r : GameResource.values())
                        inventory.increment(r, purchase.get(r));
                    purchases.decrement(purchase, 1);
                    break;
                case Gain_DevelopmentCard:
                    bought.decrement((DevelopmentCard) event.getPayload(), 1);
                    break;
                case Mature_DevelopmentCards:
                    EnumCounter<DevelopmentCard> purchased = (EnumCounter<DevelopmentCard>) event.getPayload();
                    for (DevelopmentCard card : DevelopmentCard.values()) {
                        bought.increment(card, purchased.get(card));
                        active.decrement(card, purchased.get(card));
                    }
                    break;
                case Use_DevelopmentCard:
                    active.increment((DevelopmentCard) event.getPayload(), 1);
                    used.decrement((DevelopmentCard) event.getPayload(), 1);
                    break;
                case Offer_Trade:
                    trade = null;
                    break;
                case Fill_Trade:
                    trade = (TemporaryTrade) event.getPayload();
                    for (GameResource r : GameResource.values()) {
                        inventory.decrement(r, trade.request.get(r));
                        inventory.increment(r, trade.offer.get(r));
                    }
                    break;
                case Make_Trade:
                    Trade otherTrade = (Trade) event.getPayload();
                    for (GameResource r : GameResource.values()) {
                        inventory.decrement(r, otherTrade.offer.get(r));
                        inventory.increment(r, otherTrade.request.get(r));
                    }
                    break;
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public void test(PlayerEvent event) throws EventConsumerException {
        switch (event.getType()) {
            case Gain_Resources:
                break;
            case Make_Purchase:
                for (GameResource r : GameResource.values())
                    if (!inventory.contains(r, ((Purchase) event.getPayload()).get(r)))
                        throw new EventConsumerException("Insufficient funds");
                break;
            case Gain_DevelopmentCard:
                break;
            case Mature_DevelopmentCards:
                if (!bought.equals(event.getPayload()))
                    throw new EventConsumerException("Inconsistent card count");
                break;
            case Use_DevelopmentCard:
                if (!active.contains((DevelopmentCard) event.getPayload(), 1))
                    throw new EventConsumerException("No card");
                break;
            case Offer_Trade:
            case Fill_Trade:
                TemporaryTrade trade = (TemporaryTrade) event.getPayload();
                if (trade != null)
                    for (GameResource r : GameResource.values()) {
                        if (!inventory.contains(r, trade.offer.get(r)))
                            throw new EventConsumerException("Insufficient funds");
                    }
                break;
            case Make_Trade:
                if (!canMakeTrade((Trade) event.getPayload()))
                    throw new EventConsumerException("Insufficent funds");
                break;
        }
    }

    @Override
    public void execute(PlayerEvent event) throws EventConsumerException {
        test(event);
        try {
            history.push(event);
            switch (event.getType()) {
                case Gain_Resources:
                    EnumCounter<GameResource> income = (EnumCounter<GameResource>) event.getPayload();
                    for (GameResource r : GameResource.values()) {
                        inventory.increment(r, income.get(r));
                    }
                    break;
                case Make_Purchase:
                    Purchase purchase = (Purchase) event.getPayload();
                    for (GameResource r : GameResource.values())
                        inventory.decrement(r, purchase.get(r));
                    purchases.increment(purchase, 1);
                    break;
                case Gain_DevelopmentCard:
                    bought.increment((DevelopmentCard) event.getPayload(), 1);
                    break;
                case Mature_DevelopmentCards:
                    EnumCounter<DevelopmentCard> purchased = (EnumCounter<DevelopmentCard>) event.getPayload();
                    for (DevelopmentCard card : DevelopmentCard.values()) {
                        bought.decrement(card, purchased.get(card));
                        active.increment(card, purchased.get(card));
                    }
                    break;
                case Use_DevelopmentCard:
                    active.decrement((DevelopmentCard) event.getPayload(), 1);
                    used.increment((DevelopmentCard) event.getPayload(), 1);
                    break;
                case Offer_Trade:
                    trade =(TemporaryTrade) event.getPayload();
                    break;
                case Fill_Trade:
                    trade =(TemporaryTrade) event.getPayload();
                    for (GameResource r : GameResource.values()) {
                        inventory.increment(r, trade.request.get(r));
                        inventory.decrement(r, trade.offer.get(r));
                    }
                    trade = null;
                    break;
                case Make_Trade:
                    Trade otherTrade = (Trade) event.getPayload();
                    for (GameResource r : GameResource.values()) {
                        inventory.increment(r, otherTrade.offer.get(r));
                        inventory.decrement(r, otherTrade.request.get(r));
                    }
                    break;
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (!name.equals(player.name)) return false;
        if (teamColor != player.teamColor) return false;
        if (!inventory.equals(player.inventory)) return false;
        if (!bought.equals(player.bought)) return false;
        if (!active.equals(player.active)) return false;
        if (!purchases.equals(player.purchases)) return false;
        if (!history.equals(player.history)) return false;
        return (trade == null ? (player.trade == null) : (trade.equals(player.trade)));
    }

    @Override
    public String toString() {
        return "Player{" +
                "name=" + name +
                ", teamColor=" + teamColor +
                ", inventory=" + inventory +
                ", bought=" + bought +
                ", active=" + active +
                ", purchases=" + purchases +
                ", history=" + history +
                ", trade=" + trade +
                '}';
    }
}

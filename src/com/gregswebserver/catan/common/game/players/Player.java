package com.gregswebserver.catan.common.game.players;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
import com.gregswebserver.catan.common.game.gameplay.trade.TemporaryTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.gamestate.DevelopmentCard;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.game.util.EnumAccumulator;
import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.game.util.GameResource;

import java.io.Serializable;
import java.util.Stack;

/**
 * Created by Greg on 8/8/2014.
 * A player in a game of catan, stores resource accounts, victory points, and can make moves on the catan game.
 */
@SuppressWarnings("unchecked")
public class Player implements Serializable, ReversibleEventConsumer<PlayerEvent> {

    private final Username name;
    private final TeamColor teamColor;
    private final EnumAccumulator<GameResource> inventory;
    private final EnumAccumulator<DevelopmentCard> bought;
    private final EnumAccumulator<DevelopmentCard> active;
    private final EnumAccumulator<DevelopmentCard> used;
    private final Stack<PlayerEvent> history;
    private final Stack<TemporaryTrade> trades;

    public Player(Username name, TeamColor teamColor) {
        this.name = name;
        this.teamColor = teamColor;
        inventory = new EnumAccumulator<>(GameResource.class);
        bought = new EnumAccumulator<>(DevelopmentCard.class);
        active = new EnumAccumulator<>(DevelopmentCard.class);
        used = new EnumAccumulator<>(DevelopmentCard.class);
        history = new Stack<>();
        trades = new Stack<>();
        trades.push(null);
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

    public EnumCounter<DevelopmentCard> getDevelopmentCards() {
        return active;
    }

    public TemporaryTrade getTrade() {
        return trades.peek();
    }

    public boolean canMakeTrade(Trade t) {
        for (GameResource r : GameResource.values())
            if (!inventory.contains(r, t.getRequest().get(r)))
                return false;
        return true;
    }

    public PlayerEvent getMatureEvent() {
        return new PlayerEvent(name, PlayerEventType.Mature_DevelopmentCards, new EnumAccumulator<>(DevelopmentCard.class, bought));
    }

    @Override
    public void undo() throws EventConsumerException {
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        PlayerEvent event = history.pop();
        try {
            switch (event.getType()) {
                case Gain_Resources:
                    EnumCounter<GameResource> gain = (EnumCounter<GameResource>) event.getPayload();
                    for (GameResource r : GameResource.values()) {
                        inventory.decrement(r, gain.get(r));
                    }
                    break;
                case Lose_Resources:
                    EnumCounter<GameResource> loss = (EnumCounter<GameResource>) event.getPayload();
                    for (GameResource r : GameResource.values()) {
                        inventory.increment(r, loss.get(r));
                    }
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
                case Cancel_Trade:
                    trades.pop();
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
            case Lose_Resources:
                EnumCounter<GameResource> loss = (EnumCounter<GameResource>) event.getPayload();
                for (GameResource r : GameResource.values()) {
                    if (!inventory.contains(r, loss.get(r)))
                        throw new EventConsumerException("Insufficient funds");
                }
                break;
            case Gain_DevelopmentCard:
                break;
            case Mature_DevelopmentCards:
                EnumCounter<DevelopmentCard> maturation = (EnumCounter<DevelopmentCard>) event.getPayload();
                for (DevelopmentCard card : bought) {
                    if (bought.get(card) != maturation.get(card))
                        throw new EventConsumerException("Inconsistent card count");
                }
                break;
            case Use_DevelopmentCard:
                if (!active.contains((DevelopmentCard) event.getPayload(), 1))
                    throw new EventConsumerException("No card");
                break;
            case Cancel_Trade:
                if (trades.peek() == null)
                    throw new EventConsumerException("No trade to cancel");
                break;
            case Offer_Trade:
                TemporaryTrade trade = (TemporaryTrade) event.getPayload();
                if (trade != null) {
                    if (trade.equals(trades.peek()))
                        throw new EventConsumerException("Already proposed");
                    boolean trivial = true;
                    for (GameResource r : GameResource.values()) {
                        if (trade.getOffer().get(r) != 0 || trade.getRequest().get(r) != 0)
                            trivial = false;
                        if (!inventory.contains(r, trade.getOffer().get(r)))
                            throw new EventConsumerException("Insufficient funds");
                    }
                    if (trivial)
                        throw new EventConsumerException("Trivial trade");
                }
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
                    EnumCounter<GameResource> gain = (EnumCounter<GameResource>) event.getPayload();
                    for (GameResource r : inventory) {
                        inventory.increment(r, gain.get(r));
                    }
                    break;
                case Lose_Resources:
                    EnumCounter<GameResource> loss = (EnumCounter<GameResource>) event.getPayload();
                    for (GameResource r : inventory) {
                        inventory.decrement(r, loss.get(r));
                    }
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
                    trades.push((TemporaryTrade) event.getPayload());
                    break;
                case Cancel_Trade:
                    trades.push(null);
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
        if (!history.equals(player.history)) return false;
        return (trades.equals(player.trades));
    }

    @Override
    public String toString() {
        return "Player{" +
                "name=" + name +
                ", teamColor=" + teamColor +
                ", inventory=" + inventory +
                ", bought=" + bought +
                ", active=" + active +
                ", history=" + history +
                ", trades=" + trades +
                '}';
    }
}

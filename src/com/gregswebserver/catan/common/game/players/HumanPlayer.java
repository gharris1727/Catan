package com.gregswebserver.catan.common.game.players;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.gamestate.DevelopmentCard;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.game.test.EqualityException;
import com.gregswebserver.catan.common.game.util.EnumAccumulator;
import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.game.util.GameResource;

import java.util.Collections;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Greg on 8/8/2014.
 * A player in a game of catan, stores resource accounts, victory points, and can make moves on the catan game.
 */
@SuppressWarnings("unchecked")
public class HumanPlayer implements Player {

    private final Username name;
    private final TeamColor teamColor;
    private final EnumAccumulator<GameResource> inventory;
    private final EnumAccumulator<DevelopmentCard> bought;
    private final EnumAccumulator<DevelopmentCard> active;
    private final Stack<PlayerEvent> history;
    private final Stack<Trade> trades;

    public HumanPlayer(Username name, TeamColor teamColor) {
        this.name = name;
        this.teamColor = teamColor;
        inventory = new EnumAccumulator<>(GameResource.class);
        bought = new EnumAccumulator<>(DevelopmentCard.class);
        active = new EnumAccumulator<>(DevelopmentCard.class);
        history = new Stack<>();
        trades = new Stack<>();
        trades.push(null);
    }

    @Override
    public Username getName() {
        return name;
    }

    @Override
    public TeamColor getTeamColor() {
        return teamColor;
    }

    @Override
    public EnumCounter<GameResource> getInventory() {
        return inventory;
    }

    @Override
    public EnumCounter<DevelopmentCard> getBoughtCards() {
        return bought;
    }

    @Override
    public EnumCounter<DevelopmentCard> getDevelopmentCards() {
        return active;
    }

    @Override
    public Set<Trade> getTrades() {
        if (trades.peek() != null)
            return Collections.singleton(trades.peek());
        else
            return Collections.EMPTY_SET;
    }

    @Override
    public boolean canMakeTrade(Trade t) {
        for (GameResource r : GameResource.values())
            if (!inventory.contains(r, t.getRequest().get(r)))
                return false;
        return true;
    }

    @Override
    public int getDiscardCount() {
        int count = 0;
        //Total all of the cards in the player's hand.
        for (GameResource resource : inventory)
            count += inventory.get(resource);
        //Integer division is intentional to round down
        return (count > 7) ? count/2 : 0;
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
                case Discard_Resources:
                    //Intentionally flow into the next case.
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
                    break;
                case Offer_Trade:
                case Use_Trade:
                case Cancel_Trade:
                    trades.pop();
                    break;
                case Finish_Discarding:
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
            case Discard_Resources:
                EnumCounter<GameResource> discard = (EnumCounter<GameResource>) event.getPayload();
                int count = 0;
                for (GameResource r : discard)
                    count += discard.get(r);
                if (count != getDiscardCount())
                    throw new EventConsumerException("Incorrect number of cards discarded.");
                //Intentionally flow into the next case.
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
            case Use_Trade:
                if (trades.peek() == null)
                    throw new EventConsumerException("No trade to use.");
                if (!trades.peek().equals(event.getPayload()))
                    throw new EventConsumerException("Trade not offered by player.");
                break;
            case Cancel_Trade:
                if (trades.peek() == null)
                    throw new EventConsumerException("No trade to cancel");
                break;
            case Offer_Trade:
                Trade trade = (Trade) event.getPayload();
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
            case Finish_Discarding:
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
                case Discard_Resources:
                    //Intentionally flow into the next case.
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
                    break;
                case Offer_Trade:
                    trades.push((Trade) event.getPayload());
                    break;
                case Use_Trade:
                    trades.push(null);
                    break;
                case Cancel_Trade:
                    trades.push(null);
                    break;
                case Finish_Discarding:
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

        HumanPlayer player = (HumanPlayer) o;

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

    @Override
    public void assertEquals(Player other) throws EqualityException {
        if (this == other)
            return;
        if (other == null)
            throw new EqualityException("PlayerObject", this, null);
        if (getClass() != other.getClass())
            throw new EqualityException("PlayerClass", getClass(), other.getClass());
        
        HumanPlayer o = (HumanPlayer) other;

        if (!name.equals(o.name))
            throw new EqualityException("PlayerName", name, o.name);
        if (teamColor != o.teamColor)
            throw new EqualityException("PlayerColor", teamColor, o.teamColor);
        try {
            inventory.assertEquals(o.inventory);
        } catch (EqualityException e) {
            throw new EqualityException("PlayerInventory", e);
        }
        try {
            bought.assertEquals(o.bought);
        } catch (EqualityException e) {
            throw new EqualityException("PlayerBought", e);
        }
        try {
            active.assertEquals(o.active);
        } catch (EqualityException e) {
            throw new EqualityException("PlayerActive", e);
        }
        if (!history.equals(o.history))
            throw new EqualityException("PlayerHistory", history, o.history);
        if (!trades.equals(o.trades))
            throw new EqualityException("PlayerTrades", trades, o.trades);
    }
}

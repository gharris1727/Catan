package catan.common.game.players;

import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.game.gameplay.trade.Trade;
import catan.common.game.gamestate.DevelopmentCard;
import catan.common.game.teams.TeamColor;
import catan.common.game.util.EnumAccumulator;
import catan.common.game.util.EnumCounter;
import catan.common.game.util.GameResource;

import java.util.Arrays;
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
        return trades.peek() != null ? Collections.singleton(trades.peek()) : Collections.emptySet();
    }

    @Override
    public boolean canMakeTrade(Trade t) {
        return Arrays.stream(GameResource.values())
                .allMatch(r -> inventory.contains(r, t.getRequest().get(r)));
    }

    @Override
    public int getDiscardCount() {
        int count = 0;
        //Total all of the cards in the player's hand.
        for (GameResource resource : inventory)
            count += inventory.get(resource);
        //Integer division is intentional to round down
        return (count > 7) ? (count / 2) : 0;
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
    public EventConsumerProblem test(PlayerEvent event) {
        switch (event.getType()) {
            case Gain_Resources:
                break;
            case Discard_Resources:
                EnumCounter<GameResource> discard = (EnumCounter<GameResource>) event.getPayload();
                int count = 0;
                for (GameResource r : discard)
                    count += discard.get(r);
                if (count != getDiscardCount())
                    return new EventConsumerProblem("Incorrect number of cards discarded.");
                //Intentionally flow into the next case.
            case Lose_Resources:
                EnumCounter<GameResource> loss = (EnumCounter<GameResource>) event.getPayload();
                for (GameResource r : GameResource.values()) {
                    if (!inventory.contains(r, loss.get(r)))
                        return new EventConsumerProblem("Insufficient funds");
                }
                break;
            case Gain_DevelopmentCard:
                break;
            case Mature_DevelopmentCards:
                EnumCounter<DevelopmentCard> maturation = (EnumCounter<DevelopmentCard>) event.getPayload();
                for (DevelopmentCard card : bought) {
                    if (bought.get(card) != maturation.get(card))
                        return new EventConsumerProblem("Inconsistent card count");
                }
                break;
            case Use_DevelopmentCard:
                if (!active.contains((DevelopmentCard) event.getPayload(), 1))
                    return new EventConsumerProblem("No card");
                break;
            case Use_Trade:
                if (trades.peek() == null)
                    return new EventConsumerProblem("No trade to use.");
                if (!trades.peek().equals(event.getPayload()))
                    return new EventConsumerProblem("Trade not offered by player.");
                break;
            case Cancel_Trade:
                if (trades.peek() == null)
                    return new EventConsumerProblem("No trade to cancel");
                break;
            case Offer_Trade:
                Trade trade = (Trade) event.getPayload();
                if (trade != null) {
                    if (trade.equals(trades.peek()))
                        return new EventConsumerProblem("Already proposed");
                    boolean trivial = true;
                    for (GameResource r : GameResource.values()) {
                        if ((trade.getOffer().get(r) != 0) || (trade.getRequest().get(r) != 0))
                            trivial = false;
                        if (!inventory.contains(r, trade.getOffer().get(r)))
                            return new EventConsumerProblem("Insufficient funds");
                    }
                    if (trivial)
                        return new EventConsumerProblem("Trivial trade");
                }
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
        if ((o == null) || (getClass() != o.getClass())) return false;

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
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + teamColor.hashCode();
        result = 31 * result + inventory.hashCode();
        result = 31 * result + bought.hashCode();
        result = 31 * result + active.hashCode();
        result = 31 * result + history.hashCode();
        result = 31 * result + trades.hashCode();
        return result;
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

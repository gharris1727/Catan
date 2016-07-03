package com.gregswebserver.catan.common.game.gamestate;

import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.EventConsumerProblem;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import com.gregswebserver.catan.common.util.ReversiblePRNG;
import com.gregswebserver.catan.test.common.game.AssertEqualsTestable;
import com.gregswebserver.catan.test.common.game.EqualityException;

import java.util.Stack;

/**
 * Created by greg on 5/25/16.
 * Management class to maintain the gamestate of a catan game
 */
public class RandomizerState implements ReversibleEventConsumer<GameStateEvent>, AssertEqualsTestable<RandomizerState> {

    private final DiceState dice;
    private final DevelopmentDeckState cards;
    private final TeamTurnState turns;
    private final ReversiblePRNG theft;
    private final Stack<GameStateEvent> history;

    public RandomizerState(GameSettings settings) {
        dice = new DiceState(settings.seed);
        cards = new DevelopmentDeckState(settings.rules, settings.seed);
        turns = new TeamTurnState(settings.seed, settings.playerTeams.allocate(settings.seed).getTeams());
        theft = new ReversiblePRNG(settings.seed);
        history = new Stack<>();
    }

    @Override
    public void undo() throws EventConsumerException {
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        GameStateEvent event = history.pop();
        try {
            switch (event.getType()) {
                case Roll_Dice:
                    dice.prev();
                    break;
                case Draw_DevelopmentCard:
                    cards.prev();
                    break;
                case Advance_Turn:
                    turns.prev();
                    break;
                case Advance_Theft:
                    theft.prev();
                    break;
                case Active_Turn:
                    break;
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public EventConsumerProblem test(GameStateEvent event) {
        if (event == null)
            return new EventConsumerProblem("No event");
        switch (event.getType()) {
            case Roll_Dice:
                if (!dice.hasNext())
                    return new EventConsumerProblem("No next roll");
                DiceRoll diceRoll = dice.get();
                if (diceRoll != event.getPayload())
                    return new EventConsumerProblem("Inconsistent DiceRoll");
                break;
            case Draw_DevelopmentCard:
                if (!cards.hasNext())
                    return new EventConsumerProblem("No next card");
                if (cards.get() != event.getPayload())
                    return new EventConsumerProblem("Inconsistent DevelopmentCard");
                break;
            case Advance_Turn:
                if (!turns.hasNext())
                    return new EventConsumerProblem("No next turn");
                if (turns.get() != event.getPayload())
                    return new EventConsumerProblem("Inconsistent TeamColor");
                break;
            case Advance_Theft:
                //We always have another number to generate.
                break;
            case Active_Turn:
                if (turns.get() != event.getPayload())
                    return new EventConsumerProblem("Not your turn");
                break;
        }
        return null;
    }

    @Override
    public void execute(GameStateEvent event) throws EventConsumerException {
        EventConsumerProblem problem = test(event);
        if (problem != null)
            throw new EventConsumerException(problem);
        try {
            history.push(event);
            switch (event.getType()) {
                case Roll_Dice:
                    dice.next();
                    break;
                case Draw_DevelopmentCard:
                    cards.next();
                    break;
                case Advance_Turn:
                    turns.next();
                    break;
                case Active_Turn:
                    break;
                case Advance_Theft:
                    theft.next();
                    break;
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    public DiceRoll getDiceRoll() {
        return dice.get();
    }

    public DiceRoll getPreviousDiceRoll() {
        dice.prev();
        return dice.next();
    }

    public DevelopmentCard getDevelopmentCard() {
        try {
            return cards.get();
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    public TeamColor getNextTeam() {
        turns.next();
        TeamColor next = turns.get();
        turns.prev();
        return next;
    }

    public int getTheftInt(int limit) {
        return theft.getInt(limit);
    }

    @Override
    public void assertEquals(RandomizerState other) throws EqualityException {
        if (!dice.equals(other.dice))
            throw new EqualityException("RandomizerDice", dice, other.dice);
        if (!cards.equals(other.cards))
            throw new EqualityException("RandomizerCards", cards, other.cards);
        if (!turns.equals(other.turns))
            throw new EqualityException("RandomizerTurns", turns, other.turns);
        if (!theft.equals(other.theft))
            throw new EqualityException("RandomizerTheft", theft, other.theft);
        if (!history.equals(other.history))
            throw new EqualityException("RandomizerHistory", history, other.history);
    }
}

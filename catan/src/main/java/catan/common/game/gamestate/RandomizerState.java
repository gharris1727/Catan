package catan.common.game.gamestate;

import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.event.ReversibleEventConsumer;
import catan.common.game.teams.TeamColor;
import catan.common.structure.game.GameSettings;
import catan.common.util.ReversiblePRNG;

import java.util.Stack;

/**
 * Created by greg on 5/25/16.
 * Management class to maintain the gamestate of a catan game
 */
public class RandomizerState implements ReversibleEventConsumer<GameStateEvent> {

    final DiceState dice;
    final DevelopmentDeckState cards;
    final TeamTurnState turns;
    final ReversiblePRNG theft;
    final Stack<GameStateEvent> history;

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
                    return new EventConsumerProblem("Inconsistent DiceRoll, expected " + event.getPayload() + " but rolled " + diceRoll);
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
            throw new EventConsumerException(event, problem);
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
        return cards.hasNext() ? cards.get() : null;
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

}

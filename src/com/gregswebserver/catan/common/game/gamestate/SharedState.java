package com.gregswebserver.catan.common.game.gamestate;

import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.structure.game.GameSettings;

import java.util.EnumSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created by greg on 5/25/16.
 * Management class to maintain the gamestate of a catan game
 */
public class SharedState implements ReversibleEventConsumer<GameStateEvent> {

    private final DiceState dice;
    private final DevelopmentDeckState cards;
    private final TeamTurnState turns;
    private final Stack<GameStateEvent> history;

    public SharedState(GameSettings settings) {
        dice = new DiceState(settings.seed);
        cards = new DevelopmentDeckState(settings.gameRules, settings.seed);
        Set<TeamColor> set = EnumSet.noneOf(TeamColor.class);
        for (TeamColor color : settings.playerTeams.values())
            set.add(color);
        turns = new TeamTurnState(settings.seed, set);
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
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public void test(GameStateEvent event) throws EventConsumerException {
        if (event == null)
            throw new EventConsumerException("No event");
        switch (event.getType()) {
            case Roll_Dice:
                if (!dice.hasNext())
                    throw new EventConsumerException("No next roll");
                break;
            case Draw_DevelopmentCard:
                if (!cards.hasNext())
                    throw new EventConsumerException("No next card");
                break;
            case Advance_Turn:
                if (!turns.hasNext())
                    throw new EventConsumerException("No next turn");
                break;
        }
    }

    @Override
    public void execute(GameStateEvent event) throws EventConsumerException {
        test(event);
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
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    public DiceRoll getDiceRoll() {
        return dice.get();
    }

    public DevelopmentCard getDevelopmentCard() {
        try {
            return cards.get();
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    public TeamColor getActiveTeam() {
        return turns.get();
    }
}

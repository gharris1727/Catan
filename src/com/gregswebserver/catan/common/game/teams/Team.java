package com.gregswebserver.catan.common.game.teams;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created by greg on 5/24/16.
 * Instance of a team in a specific game, with all constituent players.
 */
public class Team implements ReversibleEventConsumer<TeamEvent> {

    private final TeamColor color;
    private final Set<Username> players;
    private final Stack<TeamEvent> events;

    private int round;
    private TeamState state;
    private boolean freeRobber;

    public Team(TeamColor color) {
        this.color = color;
        players = new HashSet<>();
        events = new Stack<>();
        round = 0;
        state = TeamState.Outpost;
        freeRobber = false;
    }

    public TeamColor getColor() {
        return color;
    }

    public void add(Username username) {
        players.add(username);
    }

    public Set<Username> getPlayers() {
        return players;
    }

    @Override
    public void undo() throws EventConsumerException {
        if (events.isEmpty())
            throw new EventConsumerException("No event");
        TeamEvent event = events.pop();
        try {
            switch (event.getType()) {
                case Use_Robber:
                    freeRobber = true;
                    break;
                case Build_First_Outpost:
                case Build_Second_Outpost:
                    state = TeamState.Outpost;
                    break;
                case Build_Free_Road:
                    state = TeamState.Road;
                    break;
                case Finish_Setup_Turn:
                    if (round == 1)
                        state = TeamState.Done;
                case Finish_Turn:
                    round--;
                    break;
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public void test(TeamEvent event) throws EventConsumerException{
        switch (event.getType()) {
            case Use_Robber:
                if (!freeRobber)
                    throw new EventConsumerException("Robber not active");
                break;
            case Build_First_Outpost:
                if (state != TeamState.Outpost || round != 0)
                    throw new EventConsumerException("First outpost not avaliable");
                break;
            case Build_Second_Outpost:
                if (state != TeamState.Outpost || round != 1)
                    throw new EventConsumerException("Second outpost not avaliable");
                break;
            case Build_Free_Road:
                if (state != TeamState.Road || round >= 2)
                    throw new EventConsumerException("Road not avaliable");
                break;
            case Finish_Setup_Turn:
                if (state != TeamState.Done || round >= 2)
                    throw new EventConsumerException("Setup round not finished");
                break;
            case Finish_Turn:
                if (state != TeamState.Done || round < 2)
                    throw new EventConsumerException("Setup not finished");
                break;
        }
    }

    @Override
    public void execute(TeamEvent event) throws EventConsumerException {
        test(event);
        try {
            events.push(event);
            switch (event.getType()){
                case Use_Robber:
                    freeRobber = false;
                    break;
                case Build_First_Outpost:
                case Build_Second_Outpost:
                    state = TeamState.Road;
                    break;
                case Build_Free_Road:
                    state = TeamState.Done;
                    break;
                case Finish_Setup_Turn:
                    if (round == 0)
                        state = TeamState.Outpost;
                case Finish_Turn:
                    round++;
                    break;
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    private enum TeamState {
        Outpost, Road, Done
    }
}

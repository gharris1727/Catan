package com.gregswebserver.catan.common.game.teams;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.EventConsumerProblem;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
import com.gregswebserver.catan.test.common.game.AssertEqualsTestable;
import com.gregswebserver.catan.test.common.game.EqualityException;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created by greg on 5/24/16.
 * Instance of a team in a specific game, with all constituent players.
 */
public class Team implements ReversibleEventConsumer<TeamEvent>, AssertEqualsTestable<Team> {

    private final TeamColor color;
    private final Set<Username> players;
    private final Stack<TeamEvent> history;

    private int round;
    private int freeRoads;
    private TeamState state;
    private RobberState freeRobber;

    public Team(TeamColor color) {
        this.color = color;
        players = new HashSet<>();
        history = new Stack<>();
        round = 0;
        freeRoads = 1;
        state = TeamState.Outpost;
        freeRobber = RobberState.Inactive;
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
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        TeamEvent event = history.pop();
        try {
            switch (event.getType()) {
                case Activate_Robber:
                    freeRobber = RobberState.Inactive;
                    break;
                case Use_Robber:
                    freeRobber = RobberState.Active;
                    break;
                case Steal_Resources:
                    freeRobber = RobberState.Stealing;
                    break;
                case Build_First_Outpost:
                case Build_Second_Outpost:
                    state = TeamState.Outpost;
                    break;
                case Activate_RoadBuilding:
                    freeRoads -= 2;
                    break;
                case Build_Free_Road:
                    if (round <= 1)
                        state = TeamState.Road;
                    freeRoads++;
                    break;
                case Finish_Setup_Turn:
                    if (round == 1) {
                        state = TeamState.Done;
                        freeRoads--;
                    }
                case Finish_Turn:
                    round--;
                    break;
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public EventConsumerProblem test(TeamEvent event) {
        switch (event.getType()) {
            case Activate_Robber:
                if (freeRobber != RobberState.Inactive)
                    return new EventConsumerProblem("Already have free robber");
                break;
            case Use_Robber:
                if (freeRobber != RobberState.Active)
                    return new EventConsumerProblem("Robber not active");
                break;
            case Steal_Resources:
                if (freeRobber != RobberState.Stealing)
                    return new EventConsumerProblem("Cannot steal");
                break;
            case Build_First_Outpost:
                if (state != TeamState.Outpost || round != 0)
                    return new EventConsumerProblem("First outpost not avaliable");
                break;
            case Build_Second_Outpost:
                if (state != TeamState.Outpost || round != 1)
                    return new EventConsumerProblem("Second outpost not avaliable");
                break;
            case Activate_RoadBuilding:
                break;
            case Build_Free_Road:
                if (freeRoads <= 0)
                    return new EventConsumerProblem("Road not avaliable");
                break;
            case Finish_Setup_Turn:
                if (state != TeamState.Done)
                    return new EventConsumerProblem("Setup round not finished");
                if (round >= 2)
                    return new EventConsumerProblem("Setup round finished already");
                break;
            case Finish_Turn:
                if (state != TeamState.Done || round < 2)
                    return new EventConsumerProblem("Setup not finished");
                if (freeRobber != RobberState.Inactive)
                    return new EventConsumerProblem("Robber still active");
                if (freeRoads != 0)
                    return new EventConsumerProblem("Free roads still avaliable");
                break;
        }
        return null;
    }

    @Override
    public void execute(TeamEvent event) throws EventConsumerException {
        EventConsumerProblem problem = test(event);
        if (problem != null)
            throw new EventConsumerException(problem);
        try {
            history.push(event);
            switch (event.getType()){
                case Activate_Robber:
                    freeRobber = RobberState.Active;
                    break;
                case Use_Robber:
                    freeRobber = RobberState.Stealing;
                    break;
                case Steal_Resources:
                    freeRobber = RobberState.Inactive;
                    break;
                case Build_First_Outpost:
                case Build_Second_Outpost:
                    state = TeamState.Road;
                    break;
                case Activate_RoadBuilding:
                    freeRoads += 2;
                    break;
                case Build_Free_Road:
                    state = TeamState.Done;
                    freeRoads--;
                    break;
                case Finish_Setup_Turn:
                    if (round == 0) {
                        state = TeamState.Outpost;
                        freeRoads++;
                    }
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

    private enum RobberState {
        Inactive, Active, Stealing
    }

    @Override
    public void assertEquals(Team other) throws EqualityException {
        if (other == this)
            return;

        if (!color.equals(other.color))
            throw new EqualityException("TeamColor", color, other.color);
        if (!players.equals(other.players))
            throw new EqualityException("TeamPlayers", players, other.players);
        if (!history.equals(other.history))
            throw new EqualityException("TeamHistory", history, other.history);
        if (round != other.round)
            throw new EqualityException("TeamRound", round, other.round);
        if (freeRoads != other.freeRoads)
            throw new EqualityException("TeamFreeRoads", freeRoads, other.freeRoads);
        if (state != other.state)
            throw new EqualityException("TeamState", state, other.state);
        if (freeRobber != other.freeRobber)
            throw new EqualityException("TeamFreeRobber", freeRobber, other.freeRobber);
    }
}

package catan.common.game.teams;

import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.event.ReversibleEventConsumer;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created by greg on 5/24/16.
 * Instance of a team in a specific game, with all constituent players.
 */
public class Team implements ReversibleEventConsumer<TeamEvent> {

    final TeamColor color;
    final Set<Username> players;
    final Stack<TeamEvent> history;

    int round;
    int freeRoads;
    TeamState state;
    RobberState freeRobber;

    public Team(TeamColor color) {
        this.color = color;
        players = new HashSet<>();
        history = new Stack<>();
        round = 0;
        freeRoads = 0;
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
                    if (round <= 1) {
                        state = TeamState.Road;
                    } else {
                        freeRoads++;
                    }
                    break;
                case Finish_Setup_Turn:
                    if (round == 1) {
                        state = TeamState.Done;
                    }
                    //noinspection fallthrough
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
                if ((state != TeamState.Outpost) || (round != 0))
                    return new EventConsumerProblem("First outpost not avaliable");
                break;
            case Build_Second_Outpost:
                if ((state != TeamState.Outpost) || (round != 1))
                    return new EventConsumerProblem("Second outpost not avaliable");
                break;
            case Activate_RoadBuilding:
                break;
            case Build_Free_Road:
                if ((state != TeamState.Road) && (freeRoads <= 0))
                    return new EventConsumerProblem("Road not avaliable");
                break;
            case Finish_Setup_Turn:
                if (state != TeamState.Done)
                    return new EventConsumerProblem("Setup round not finished");
                if (round >= 2)
                    return new EventConsumerProblem("Setup round finished already");
                break;
            case Finish_Turn:
                if ((state != TeamState.Done) || (round < 2))
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
            throw new EventConsumerException(event, problem);
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
                    if (state == TeamState.Road) {
                        state = TeamState.Done;
                    } else {
                        freeRoads--;
                    }
                    break;
                case Finish_Setup_Turn:
                    if (round == 0) {
                        state = TeamState.Outpost;
                    }
                    //noinspection fallthrough
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

}

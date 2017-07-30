package catan.common.game.teams;

import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.event.ReversibleEventConsumer;
import catan.common.game.gameplay.allocator.TeamAllocation;

import java.util.*;

/**
 * Created by greg on 5/25/16.
 * Collection of teams that can be interacted with through the event consumer interface.
 */
public class TeamPool implements ReversibleEventConsumer<TeamEvent>, Iterable<TeamColor> {

    final Map<TeamColor, Team> teams;
    final Stack<TeamEvent> history;

    public TeamPool(TeamAllocation teamAllocation) {
        teams = new EnumMap<>(TeamColor.class);
        Map<TeamColor, Set<Username>> teamUsers = teamAllocation.getTeamUsers();
        for (TeamColor color : teamAllocation.getTeams()) {
            Team team = new Team(color);
            teams.put(color, team);
            for (Username username : teamUsers.get(color))
                team.add(username);
        }
        history = new Stack<>();
    }

    @Override
    public Iterator<TeamColor> iterator() {
        return teams.keySet().iterator();
    }

    public Team getTeam(TeamColor teamColor) {
        return teams.get(teamColor);
    }

    @Override
    public void undo() throws EventConsumerException {
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        TeamEvent event = history.pop();
        Team team = getTeam(event.getOrigin());
        if (team == null)
            throw new EventConsumerException("No team");
        team.undo();
    }

    @Override
    public EventConsumerProblem test(TeamEvent event) {
        if (event == null)
            return new EventConsumerProblem("No event");
        Team team = getTeam(event.getOrigin());
        if (team == null)
            return new EventConsumerProblem("No team");
        return team.test(event);
    }

    @Override
    public void execute(TeamEvent event) throws EventConsumerException {
        EventConsumerProblem problem = test(event);
        if (problem != null)
            throw new EventConsumerException(event, problem);
        try {
            history.push(event);
            getTeam(event.getOrigin()).execute(event);
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

}

package com.gregswebserver.catan.common.game.teams;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import com.gregswebserver.catan.test.common.game.AssertEqualsTestable;
import com.gregswebserver.catan.test.common.game.EqualityException;

import java.util.*;

/**
 * Created by greg on 5/25/16.
 * Collection of teams that can be interacted with through the event consumer interface.
 */
public class TeamPool implements ReversibleEventConsumer<TeamEvent>, Iterable<TeamColor>, AssertEqualsTestable<TeamPool> {

    private final Map<TeamColor, Team> teams;
    private final Stack<TeamEvent> history;

    public TeamPool(GameSettings settings) {
        teams = new EnumMap<>(TeamColor.class);
        Map<TeamColor, Set<Username>> teamUsers = settings.playerTeams.getTeamUsers();
        for (TeamColor color : settings.playerTeams.getTeams()) {
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
        try {
            Team team = getTeam(event.getOrigin());
            if (team == null)
                throw new EventConsumerException("No team");
            team.undo();
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public void test(TeamEvent event) throws EventConsumerException {
        if (event == null)
            throw new EventConsumerException("No event");
        Team team = getTeam(event.getOrigin());
        if (team == null)
            throw new EventConsumerException("No team");
        team.test(event);
    }

    @Override
    public void execute(TeamEvent event) throws EventConsumerException {
        test(event);
        try {
            history.push(event);
            getTeam(event.getOrigin()).execute(event);
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public void assertEquals(TeamPool other) throws EqualityException {
        if (other == this)
            return;

        for (TeamColor tc : TeamColor.values()) {
            if (teams.containsKey(tc))
                teams.get(tc).assertEquals(other.teams.get(tc));
            else if (other.teams.containsKey(tc))
                throw new EqualityException("TeamPoolTeams", null, other.teams.get(tc));
        }
        if (!history.equals(other.history))
            throw new EqualityException("TeamPoolHistory", history, other.history);
    }
}

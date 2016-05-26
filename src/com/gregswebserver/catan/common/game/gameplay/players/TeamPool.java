package com.gregswebserver.catan.common.game.gameplay.players;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
import com.gregswebserver.catan.common.game.gameplay.enums.TeamColor;
import com.gregswebserver.catan.common.structure.game.GameSettings;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Created by greg on 5/25/16.
 * Collection of teams that can be interacted with through the event consumer interface.
 */
public class TeamPool implements ReversibleEventConsumer<TeamEvent> {

    private final Map<TeamColor, Team> teams;
    private final Stack<TeamEvent> history;

    public TeamPool(GameSettings settings) {
        teams = new EnumMap<>(TeamColor.class);
        for (Map.Entry<Username, TeamColor> entry : settings.playerTeams.entrySet()) {
            TeamColor color = entry.getValue();
            if (!teams.containsKey(color))
                teams.put(color, new Team(color));
            teams.get(color).add(entry.getKey());
        }
        history = new Stack<>();
    }

    public Set<TeamColor> getAll() {
        return teams.keySet();
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
}

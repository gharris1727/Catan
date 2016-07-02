package com.gregswebserver.catan.common.game.gameplay.allocator;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.teams.TeamColor;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created by greg on 7/1/16.
 * A permanent team allocation
 */
public class TeamAllocation {

    private final Map<Username, TeamColor> users;
    private final Map<TeamColor, Set<Username>> teams;

    TeamAllocation(Map<Username, TeamColor> users, Map<TeamColor, Set<Username>> teams) {
        //Make all of the interior collections immutable.
        for (TeamColor color : teams.keySet()) {
            teams.put(color, Collections.unmodifiableSet(teams.get(color)));
        }

        //Make the maps immutable.
        this.users = Collections.unmodifiableMap(users);
        this.teams = Collections.unmodifiableMap(teams);
    }

    public Set<Username> getUsers() {
        return users.keySet();
    }

    public Set<TeamColor> getTeams() {
        return teams.keySet();
    }

    public Map<Username, TeamColor> getPlayerTeams() {
        return users;
    }

    public Map<TeamColor, Set<Username>> getTeamUsers() {
        return teams;
    }
}

package catan.common.game.gameplay.allocator;

import catan.common.crypto.Username;
import catan.common.game.teams.TeamColor;

import java.util.Collections;
import java.util.HashSet;
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

    public static void allocatePlayer(Map<Username, TeamColor> users, Map<TeamColor, Set<Username>> teams, Username username, TeamColor teamColor) {
        //If we dont already have a list of users, create it.
        if (!teams.containsKey(teamColor))
            teams.put(teamColor, new HashSet<>());

        //Set this user's preference.
        users.put(username, teamColor);
        teams.get(teamColor).add(username);
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

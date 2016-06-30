package com.gregswebserver.catan.common.game.gameplay.allocator;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.teams.TeamColor;

import java.util.*;

/**
 * Created by greg on 6/29/16.
 * A class that assigns users to teams arbitrarily.
 */
public class RandomTeamAllocator implements TeamAllocator {

    private final Map<Username, TeamColor> users;
    private final Map<TeamColor, Set<Username>> teams;

    public RandomTeamAllocator(Set<Username> usernames) {
        //Create inner storage for the maps.
        Map<Username, TeamColor> users = new HashMap<>();
        Map<TeamColor, Set<Username>> teams = new EnumMap<>(TeamColor.class);

        //Start allocating teams.
        Iterator<TeamColor> teamAllocator = TeamColor.getTeamSet().iterator();

        for (Username user : usernames) {
            //If we exhausted all the teams, then refresh the list and keep going.
            if (!teamAllocator.hasNext())
                teamAllocator = TeamColor.getTeamSet().iterator();

            //Get the next team to allocate
            TeamColor teamColor = teamAllocator.next();

            //If we dont already have a list of users, create it.
            if (!teams.containsKey(teamColor))
                teams.put(teamColor, new HashSet<>());

            //Store this user's team allocation.
            users.put(user, teamColor);
            teams.get(teamColor).add(user);
        }

        //Make all of the interior collections immutable.
        for (TeamColor color : teams.keySet()) {
            teams.put(color, Collections.unmodifiableSet(teams.get(color)));
        }

        //Make the maps immutable.
        this.users = Collections.unmodifiableMap(users);
        this.teams = Collections.unmodifiableMap(teams);
    }

    @Override
    public Set<Username> getUsers() {
        return users.keySet();
    }

    @Override
    public Set<TeamColor> getTeams() {
        return teams.keySet();
    }

    @Override
    public Map<Username, TeamColor> getPlayerTeams() {
        return users;
    }

    @Override
    public Map<TeamColor, Set<Username>> getTeamUsers() {
        return teams;
    }
}

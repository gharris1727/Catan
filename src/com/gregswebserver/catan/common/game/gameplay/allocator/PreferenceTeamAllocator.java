package com.gregswebserver.catan.common.game.gameplay.allocator;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.teams.TeamColor;

import java.util.*;

/**
 * Created by greg on 6/29/16.
 * A class to give users preferences for their colors.
 * Arbitrarily assigns colors to users that have no preferences.
 */
public class PreferenceTeamAllocator implements TeamAllocator {

    private final Map<Username, TeamColor> users;
    private final Map<TeamColor, Set<Username>> teams;

    public PreferenceTeamAllocator(Map<Username, TeamColor> preferences) {
        //Create local inner storage for maps.
        Map<Username, TeamColor> users = new HashMap<>();
        Map<TeamColor, Set<Username>> teams = new EnumMap<>(TeamColor.class);

        //Keep track of the users and teams that have not been allocated.
        Set<Username> usersToAllocate = new HashSet<>(preferences.keySet());
        Set<TeamColor> teamsToAllocate = TeamColor.getTeamSet();

        //Go over all of the user preferences and assign them.
        for (Map.Entry<Username, TeamColor> preference : preferences.entrySet()) {
            TeamColor teamColor = preference.getValue();

            if (teamColor == null || teamColor == TeamColor.None) {
                //If they didnt specify a preference, then add them to be addressed later.
                usersToAllocate.add(preference.getKey());
            } else {
                //If they had a preference, assign them that color.
                teamsToAllocate.remove(teamColor);
                users.put(preference.getKey(), teamColor);
            }
        }

        //Start by assigning teams that missed the first allocation.
        Iterator<TeamColor> teamAllocator = teamsToAllocate.iterator();

        //Go over all unallocated users
        for (Username user : usersToAllocate) {
            //Get the next team to allocate
            TeamColor teamColor = teamAllocator.next();

            //If we dont already have a list of users, create it.
            if (!teams.containsKey(teamColor))
                teams.put(teamColor, new HashSet<>());

            //Set this user's preference.
            users.put(user, teamAllocator.next());
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

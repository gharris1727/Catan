package com.gregswebserver.catan.common.game.gameplay.allocator;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.teams.TeamColor;

import java.util.*;

/**
 * Created by greg on 6/29/16.
 * A class that assigns users to teams arbitrarily.
 */
public class RandomTeamAllocator implements TeamAllocator {

    private final Set<Username> usernames;

    public RandomTeamAllocator(Set<Username> usernames) {
        this.usernames = usernames;
    }

    @Override
    public TeamAllocation allocate(long seed) {
        //Create inner storage for the maps.
        Map<Username, TeamColor> users = new HashMap<>();
        Map<TeamColor, Set<Username>> teams = new EnumMap<>(TeamColor.class);

        //Start allocating teams.
        Iterator<TeamColor> teamAllocator = TeamColor.getTeamSet().iterator();

        List<Username> randomizedUsernames = new ArrayList<>(usernames);
        Collections.sort(randomizedUsernames);
        Collections.shuffle(randomizedUsernames, new Random(seed));

        for (Username user : randomizedUsernames) {
            //If we exhausted all the teams, then refresh the list and keep going.
            if (!teamAllocator.hasNext())
                teamAllocator = TeamColor.getTeamSet().iterator();

            //Allocate this player to their team.
            TeamAllocation.allocatePlayer(users, teams, user, teamAllocator.next());
        }

        return new TeamAllocation(users, teams);
    }
}

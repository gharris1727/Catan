package catan.common.game.gameplay.allocator;

import catan.common.crypto.Username;
import catan.common.game.teams.TeamColor;

import java.util.*;

/**
 * Created by greg on 6/29/16.
 * A class to give users preferences for their colors.
 * Arbitrarily assigns colors to users that have no preferences.
 */
public class PreferenceTeamAllocator implements TeamAllocator {

    private final Map<Username, TeamColor> preferences;

    public PreferenceTeamAllocator(Map<Username, TeamColor> preferences) {
        this.preferences = preferences;
    }

    @Override
    public TeamAllocation allocate(long seed) {
        //Create local inner storage for maps.
        Map<Username, TeamColor> users = new HashMap<>();
        Map<TeamColor, Set<Username>> teams = new EnumMap<>(TeamColor.class);

        //Keep track of the users and teams that have not been allocated.
        List<Username> usersToAllocate = new ArrayList<>();
        Set<TeamColor> teamsToAllocate = TeamColor.getTeamSet();

        //Go over all of the user preferences and assign them.
        for (Map.Entry<Username, TeamColor> preference : preferences.entrySet()) {
            TeamColor teamColor = preference.getValue();

            if (teamColor == null || teamColor == TeamColor.None) {
                //If they didnt specify a preference, then add them to be addressed later.
                usersToAllocate.add(preference.getKey());
            } else {
                //Remove this team from first-round re-allocation.
                teamsToAllocate.remove(teamColor);
                //Assign this player their preferred color.
                TeamAllocation.allocatePlayer(users, teams, preference.getKey(), preference.getValue());
            }
        }

        //Start by assigning teams that missed the first allocation.
        Iterator<TeamColor> teamAllocator = teamsToAllocate.iterator();

        Collections.sort(usersToAllocate);
        Collections.shuffle(usersToAllocate, new Random(seed));

        //Go over all unallocated users
        for (Username user : usersToAllocate) {
            //If we exhausted all the teams, then refresh the list and keep going.
            if (!teamAllocator.hasNext())
                teamAllocator = TeamColor.getTeamSet().iterator();
            //Assign this player their color.
            TeamAllocation.allocatePlayer(users, teams, user, teamAllocator.next());
        }

        return new TeamAllocation(users, teams);
    }

}

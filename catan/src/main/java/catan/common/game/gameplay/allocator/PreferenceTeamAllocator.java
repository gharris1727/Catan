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

    private final List<Preference> preferences;

    public PreferenceTeamAllocator() {
        this.preferences = new LinkedList<>();
    }

    public void addPreference(Username username, TeamColor teamColor) {
        preferences.add(new Preference(username, teamColor));
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
        for (Preference preference : preferences) {

            if (preference.teamColor == null || preference.teamColor == TeamColor.None) {
                //If they didnt specify a preference, then add them to be addressed later.
                usersToAllocate.add(preference.username);
            } else {
                //Remove this team from first-round re-allocation.
                teamsToAllocate.remove(preference.teamColor);
                //Assign this player their preferred color.
                TeamAllocation.allocatePlayer(users, teams, preference.username, preference.teamColor);
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

    private class Preference {
        private final Username username;
        private final TeamColor teamColor;

        private Preference(Username username, TeamColor teamColor) {
            this.username = username;
            this.teamColor = teamColor;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreferenceTeamAllocator that = (PreferenceTeamAllocator) o;

        return preferences.equals(that.preferences);
    }

    @Override
    public int hashCode() {
        return preferences.hashCode();
    }
}

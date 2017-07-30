package catan.common.game.gameplay.allocator;

import catan.common.game.EqualityTester;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * An EqualityTester for allocations of users to teams.
 */
public final class TeamAllocationEqualityTester implements EqualityTester<TeamAllocation> {

    public static final TeamAllocationEqualityTester INSTANCE = new TeamAllocationEqualityTester();

    private TeamAllocationEqualityTester() {
    }

    @Override
    public void assertEquals(TeamAllocation expected, TeamAllocation actual) {
        if (expected == actual)
            return;

        Assert.assertEquals(expected.getPlayerTeams(), actual.getPlayerTeams());
        Assert.assertEquals(expected.getTeamUsers(), actual.getTeamUsers());
    }
}

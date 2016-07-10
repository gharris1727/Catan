package catan.common.game.gameplay.allocator;

import catan.common.game.EqualityTester;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * An EqualityTester for allocations of users to teams.
 */
public class TeamAllocationEqualityTester implements EqualityTester<TeamAllocation> {

    public static final TeamAllocationEqualityTester INSTANCE = new TeamAllocationEqualityTester();

    private TeamAllocationEqualityTester() {
    }

    @Override
    public void assertEquals(TeamAllocation a, TeamAllocation b) {
        if (a == b)
            return;

        Assert.assertEquals(a.getPlayerTeams(), b.getPlayerTeams());
        Assert.assertEquals(a.getTeamUsers(), b.getTeamUsers());
    }
}

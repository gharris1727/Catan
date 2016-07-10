package catan.common.game.teams;

import catan.common.game.EqualityTester;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * an EqualityTester for Team objects.
 */
public class TeamEqualityTester implements EqualityTester<Team> {

    public static final TeamEqualityTester INSTANCE = new TeamEqualityTester();

    private TeamEqualityTester() {
    }

    @Override
    public void assertEquals(Team a, Team b) {
        if (a == b)
            return;

        Assert.assertEquals(a.color, b.color);
        Assert.assertEquals(a.players, b.players);
        Assert.assertEquals(a.history, b.history);
        Assert.assertEquals(a.round, b.round);
        Assert.assertEquals(a.freeRoads, b.freeRoads);
        Assert.assertEquals(a.state, b.state);
        Assert.assertEquals(a.freeRobber, b.freeRobber);
    }
}

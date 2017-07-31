package catan.common.game.teams;

import catan.common.game.EqualityTester;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * an EqualityTester for Team objects.
 */
public final class TeamEqualityTester implements EqualityTester<Team> {

    @Override
    public void assertEquals(Team expected, Team actual) {
        if (expected == actual)
            return;

        Assert.assertEquals(expected.color, actual.color);
        Assert.assertEquals(expected.players, actual.players);
        Assert.assertEquals(expected.history, actual.history);
        Assert.assertEquals(expected.round, actual.round);
        Assert.assertEquals(expected.freeRoads, actual.freeRoads);
        Assert.assertEquals(expected.state, actual.state);
        Assert.assertEquals(expected.freeRobber, actual.freeRobber);
    }
}

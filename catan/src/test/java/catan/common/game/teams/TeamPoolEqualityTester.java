package catan.common.game.teams;

import catan.common.game.EqualityTester;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * EqualityTester for the TeamPool class.
 */
public class TeamPoolEqualityTester implements EqualityTester<TeamPool> {

    public static final TeamPoolEqualityTester INSTANCE = new TeamPoolEqualityTester();

    private TeamPoolEqualityTester() {
    }

    @Override
    public void assertEquals(TeamPool expected, TeamPool actual) {
        if (expected == actual)
            return;

        for (TeamColor tc : TeamColor.values()) {
            if (expected.teams.containsKey(tc))
                TeamEqualityTester.INSTANCE.assertEquals(expected.teams.get(tc), actual.teams.get(tc));
            else if (actual.teams.containsKey(tc))
                Assert.assertNull(actual.teams.get(tc));
        }
        Assert.assertEquals(expected.history, actual.history);
    }
}

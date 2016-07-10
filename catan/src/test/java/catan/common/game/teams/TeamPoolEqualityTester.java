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
    public void assertEquals(TeamPool a, TeamPool b) {
        if (a == b)
            return;

        for (TeamColor tc : TeamColor.values()) {
            if (a.teams.containsKey(tc))
                TeamEqualityTester.INSTANCE.assertEquals(a.teams.get(tc), b.teams.get(tc));
            else if (b.teams.containsKey(tc))
                Assert.assertEquals(null, b.teams.get(tc));
        }
        Assert.assertEquals(a.history, b.history);
    }
}

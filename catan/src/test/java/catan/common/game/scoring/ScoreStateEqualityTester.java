package catan.common.game.scoring;

import catan.common.game.EqualityTester;
import catan.common.game.players.PlayerPoolEqualityTester;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * Equality tester for a ScoreState.
 */
public class ScoreStateEqualityTester implements EqualityTester<ScoreState> {

    public static final ScoreStateEqualityTester INSTANCE = new ScoreStateEqualityTester();

    private ScoreStateEqualityTester() {
    }

    @Override
    public void assertEquals(ScoreState a, ScoreState b) {
        if (a == b)
            return;

        PlayerPoolEqualityTester.INSTANCE.assertEquals(a.players, b.players);
        Assert.assertEquals(a.listeners, b.listeners);
        Assert.assertEquals(a.history, b.history);
    }
}

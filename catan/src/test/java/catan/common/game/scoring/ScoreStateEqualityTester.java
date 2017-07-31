package catan.common.game.scoring;

import catan.common.game.EqualityTester;
import catan.common.game.players.PlayerPoolEqualityTester;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * Equality tester for a ScoreState.
 */
public final class ScoreStateEqualityTester implements EqualityTester<ScoreState> {

    private final PlayerPoolEqualityTester playerTester = new PlayerPoolEqualityTester();

    @Override
    public void assertEquals(ScoreState expected, ScoreState actual) {
        if (expected == actual)
            return;

        playerTester.assertEquals(expected.players, actual.players);
        Assert.assertEquals(expected.listeners, actual.listeners);
        Assert.assertEquals(expected.history, actual.history);
    }
}

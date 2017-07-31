package catan.common.game.players;

import catan.common.crypto.Username;
import catan.common.game.EqualityTester;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * EqualityTester for the PlayerPool class.
 */
public final class PlayerPoolEqualityTester implements EqualityTester<PlayerPool> {

    private final PlayerEqualityTester playerTester = new PlayerEqualityTester();

    @Override
    public void assertEquals(PlayerPool expected, PlayerPool actual) {
        if (expected == actual)
            return;

        for (Username u : expected)
            playerTester.assertEquals(expected.players.get(u), actual.players.get(u));
        Assert.assertEquals(expected.discards, actual.discards);
        Assert.assertEquals(expected.history, actual.history);
    }
}

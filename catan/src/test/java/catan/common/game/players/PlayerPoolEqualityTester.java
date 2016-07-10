package catan.common.game.players;

import catan.common.crypto.Username;
import catan.common.game.EqualityTester;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * EqualityTester for the PlayerPool class.
 */
public class PlayerPoolEqualityTester implements EqualityTester<PlayerPool> {

    public static final PlayerPoolEqualityTester INSTANCE = new PlayerPoolEqualityTester();

    private PlayerPoolEqualityTester() {
    }

    @Override
    public void assertEquals(PlayerPool a, PlayerPool b) {
        if (a == b)
            return;

        for (Username u : a)
            PlayerEqualityTester.INSTANCE.assertEquals(a.players.get(u), b.players.get(u));
        Assert.assertEquals(a.discards, b.discards);
        Assert.assertEquals(a.history, b.history);
    }
}

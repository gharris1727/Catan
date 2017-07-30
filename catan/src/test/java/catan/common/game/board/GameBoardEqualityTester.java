package catan.common.game.board;

import catan.common.game.EqualityTester;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * EqualityTester for the GameBoard class.
 */
public final class GameBoardEqualityTester implements EqualityTester<GameBoard> {
    
    public static final GameBoardEqualityTester INSTANCE = new GameBoardEqualityTester();

    private GameBoardEqualityTester() {
    }

    @Override
    public void assertEquals(GameBoard expected, GameBoard actual) {
        if (expected == actual)
            return;

        Assert.assertEquals(expected.size, actual.size);
        Assert.assertEquals(expected.hexArray, actual.hexArray);
        Assert.assertEquals(expected.diceRolls, actual.diceRolls);
        Assert.assertEquals(expected.tradingPosts, actual.tradingPosts);
        Assert.assertEquals(expected.history, actual.history);
        Assert.assertEquals(expected.robberLocations, actual.robberLocations);
    }
}

package catan.common.game.board;

import catan.common.game.EqualityTester;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * EqualityTester for the GameBoard class.
 */
public class GameBoardEqualityTester implements EqualityTester<GameBoard> {
    
    public static final GameBoardEqualityTester INSTANCE = new GameBoardEqualityTester();

    private GameBoardEqualityTester() {
    }

    @Override
    public void assertEquals(GameBoard a, GameBoard b) {
        if (a == b)
            return;

        Assert.assertEquals(a.size, b.size);
        Assert.assertEquals(a.hexArray, b.hexArray);
        Assert.assertEquals(a.diceRolls, b.diceRolls);
        Assert.assertEquals(a.tradingPosts, b.tradingPosts);
        Assert.assertEquals(a.history, b.history);
        Assert.assertEquals(a.robberLocations, b.robberLocations);
    }
}

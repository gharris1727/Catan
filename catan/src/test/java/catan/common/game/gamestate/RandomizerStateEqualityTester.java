package catan.common.game.gamestate;

import catan.common.game.EqualityTester;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * Class to assert the equality of two RandomizerState objects.
 */
public class RandomizerStateEqualityTester implements EqualityTester<RandomizerState> {

    public static final RandomizerStateEqualityTester INSTANCE = new RandomizerStateEqualityTester();

    @Override
    public void assertEquals(RandomizerState a, RandomizerState b) {
        Assert.assertEquals(a.dice, b.dice);
        Assert.assertEquals(a.cards, b.cards);
        Assert.assertEquals(a.turns, b.turns);
        Assert.assertEquals(a.theft, b.theft);
        Assert.assertEquals(a.history, b.history);
    }

}

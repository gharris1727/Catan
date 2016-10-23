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
    public void assertEquals(RandomizerState expected, RandomizerState actual) {
        Assert.assertEquals(expected.dice, actual.dice);
        Assert.assertEquals(expected.cards, actual.cards);
        Assert.assertEquals(expected.turns, actual.turns);
        Assert.assertEquals(expected.theft, actual.theft);
        Assert.assertEquals(expected.history, actual.history);
    }

}

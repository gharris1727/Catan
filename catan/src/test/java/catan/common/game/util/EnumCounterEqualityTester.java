package catan.common.game.util;

import catan.common.game.EqualityTester;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * EqualityTester for EnumCounter instances.
 */
public class EnumCounterEqualityTester<T extends Enum<T>> implements EqualityTester<EnumCounter<T>> {

    public EnumCounterEqualityTester() {
    }

    @Override
    public void assertEquals(EnumCounter<T> expected, EnumCounter<T> actual) {
        if (expected == actual)
            return;

        for (T e : expected)
            Assert.assertEquals(expected.get(e), actual.get(e));
    }
}

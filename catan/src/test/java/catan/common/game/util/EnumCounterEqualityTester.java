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
    public void assertEquals(EnumCounter<T> a, EnumCounter<T> b) {
        if (a == b)
            return;

        for (T e : a)
            Assert.assertEquals(a.get(e),b.get(e));
    }
}

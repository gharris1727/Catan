package com.gregswebserver.catan.common.game.util;

import com.gregswebserver.catan.test.common.game.AssertEqualsTestable;
import com.gregswebserver.catan.test.common.game.EqualityException;

/**
 * Created by greg on 6/11/16.
 * A Read-only interface for interacting with an EnumCounter
 */
public interface EnumCounter<T extends Enum<T>> extends Iterable<T>, AssertEqualsTestable<EnumCounter<T>> {

    int get(T e);

    default boolean contains(T e, int c) {
        return get(e) >= c;
    }

    @Override
    default void assertEquals(EnumCounter<T> other) throws EqualityException {
        for (T e : this)
            if (get(e) != other.get(e))
                throw new EqualityException("EnumCounter: " + e, get(e), other.get(e));
    }
}

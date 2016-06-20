package com.gregswebserver.catan.common.game.test;

/**
 * Created by greg on 6/19/16.
 * An object that can be asserted to be equal to another object of the same type.
 */
public interface AssertEqualsTestable<T> {

    void assertEquals(T other) throws EqualityException;
}

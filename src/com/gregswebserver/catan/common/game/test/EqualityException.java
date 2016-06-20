package com.gregswebserver.catan.common.game.test;

/**
 * Created by greg on 6/19/16.
 * An exception thrown when two objects are unequal and should be.
 */
public class EqualityException extends Exception {

    public EqualityException(String message, Object a, Object b) {
        super(message + ": Expected " + a + " but was " + b + ".");
    }

    public EqualityException(String message, Exception cause) {
        super(message, cause);
    }
}

package com.gregswebserver.catan.common;

/**
 * Created by greg on 1/10/16.
 * Thrown whenever
 */
public class IllegalStateException extends RuntimeException {

    public IllegalStateException() {
        super("Something went wrong, and the program got somewhere it shouldn't have.");
    }
}

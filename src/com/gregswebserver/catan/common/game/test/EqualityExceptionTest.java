package com.gregswebserver.catan.common.game.test;

import org.junit.Test;

/**
 * Created by greg on 6/26/16.
 * Testing class for the EqualityException diff algorithm.
 */
public class EqualityExceptionTest {

    @Test
    public void test() {
        System.out.println(EqualityException.buildMessage("message", "ABCDEF", "DEFGHI"));
    }
}

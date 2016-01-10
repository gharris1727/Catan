package com.gregswebserver.catan.common.crypto;

/**
 * Created by greg on 1/10/16.
 * Exception thrown when an error occurs while authenticating a user.
 */
public class AuthenticationException extends Exception {

    public AuthenticationException() {
        super("Authentication Error");
    }
}

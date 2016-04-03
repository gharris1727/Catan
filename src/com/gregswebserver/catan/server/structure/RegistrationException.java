package com.gregswebserver.catan.server.structure;

/**
 * Created by greg on 4/2/16.
 * An exception that occurs when a user encounters an error when changing their account registration.
 */
public class RegistrationException extends Exception {

    public RegistrationException(String message) {
        super(message);
    }
}

package com.gregswebserver.catan.server.console;

/**
 * Created by Greg on 8/12/2014.
 * Interface for finding the index of a user's entry on a console.
 */
public interface UserInput {

    public int getUserInputStart();

    public String getUserInput();
}

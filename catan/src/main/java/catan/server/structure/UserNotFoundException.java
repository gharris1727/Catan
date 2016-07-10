package catan.server.structure;

/**
 * Created by greg on 1/10/16.
 * Exception thrown when a user does not exist in the database.
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User not found");
    }
}

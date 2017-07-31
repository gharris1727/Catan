package catan.server.console;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by greg on 4/27/16.
 * A console for controlling the server via textual input
 */
public class Console {

    private final Logger logger;

    public Console(Logger logger) {
        this.logger = logger;
    }

    public void process(String command) {
        logger.log(Level.INFO, "User command: " + command);
        //TODO: process user input and generate an event to send to the server.
    }
}

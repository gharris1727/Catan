package catan.server.console;

import catan.common.log.LogLevel;
import catan.common.log.Logger;

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
        logger.log("User command: " + command, LogLevel.DEBUG);
        //TODO: process user input and generate an event to send to the server.
    }
}

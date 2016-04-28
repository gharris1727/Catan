package com.gregswebserver.catan.server.console;

import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.server.Server;

/**
 * Created by greg on 4/27/16.
 * A console for controlling the server via textual input
 */
public class Console {

    private final Server server;

    public Console(Server server) {
        this.server = server;
    }

    public void process(String command) {
        server.logger.log("User command: " + command, LogLevel.DEBUG);
        //TODO: process user input and generate an event to send to the server.
    }
}

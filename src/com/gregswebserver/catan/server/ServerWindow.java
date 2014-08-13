package com.gregswebserver.catan.server;

import com.gregswebserver.catan.log.Logger;
import com.gregswebserver.catan.server.console.Console;
import com.gregswebserver.catan.userinterface.GenericWindow;

import java.awt.*;

/**
 * Created by Greg on 8/11/2014.
 * Console window for CLI access to the server application
 */
public class ServerWindow extends GenericWindow {

    Console console;

    public ServerWindow(Logger logger) {
        super("Settlers of Catan - Server", new Dimension(800, 600), true, logger);

        setLayout(new BorderLayout());
        console = new Console(logger);
        getContentPane().add(console);
        logger.addListener(console);
        setVisible(true);
    }

    protected void onClose() {
        logger.removeListener(console);
    }
}

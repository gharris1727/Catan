package com.gregswebserver.catan.server;

import com.gregswebserver.catan.server.console.Console;
import com.gregswebserver.catan.userinterface.GenericWindow;

import java.awt.*;

/**
 * Created by Greg on 8/11/2014.
 * Console window for CLI access to the server application
 */
public class ServerWindow extends GenericWindow {

    Server server;
    Console console;

    public ServerWindow(Server server) {
        super("Settlers of Catan - Server", new Dimension(800, 600), true, server.logger);
        this.server = server;
        setLayout(new BorderLayout());
        console = new Console(server.logger);
        console.setServer(server);
        getContentPane().add(console);
        server.logger.addListener(console);
        setVisible(true);
    }

    protected void onClose() {
        server.logger.removeListener(console);
        server.shutdown();
    }
}

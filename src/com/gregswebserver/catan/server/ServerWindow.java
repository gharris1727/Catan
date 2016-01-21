package com.gregswebserver.catan.server;

import com.gregswebserver.catan.common.CoreWindow;
import com.gregswebserver.catan.server.console.Console;

import java.awt.*;

/**
 * Created by Greg on 8/11/2014.
 * Console window for CLI access to the server application
 */
public class ServerWindow extends CoreWindow {

    private final Server server;
    private final Console console;

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

    @Override
    protected void onClose() {
        server.logger.removeListener(console);
        server.shutdown();
    }
}

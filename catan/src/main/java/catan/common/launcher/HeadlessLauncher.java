package catan.common.launcher;

import catan.common.log.Logger;
import catan.server.Server;

/**
 * Created by greg on 4/23/16.
 * Launcher to start the system in a headless state.
 */
public class HeadlessLauncher {

    private final Server server;

    public HeadlessLauncher(StartupOptions options, Logger logger) {
        //TODO: launch the requested features for a headless interface.
        server = new Server(logger,25000);
    }

    public void waitForClose() {
        server.join();
    }
}

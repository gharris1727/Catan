package catan.common.launcher;

import catan.common.log.Logger;
import catan.server.ServerImpl;

/**
 * Created by greg on 4/23/16.
 * Launcher to start the system in a headless state.
 */
public class HeadlessLauncher {

    private final ServerImpl server;

    public HeadlessLauncher(StartupOptions options, Logger logger) {
        //TODO: launch the requested features for a headless interface.
        server = new ServerImpl(logger,25000);
    }

    public void waitForClose() {
        server.join();
    }
}

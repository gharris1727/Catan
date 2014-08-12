package com.gregswebserver.catan.client;

import com.gregswebserver.catan.userinterface.GenericWindow;

import java.awt.*;

/**
 * Created by Greg on 8/11/2014.
 * Graphics window to provide a GUI to the client.
 */
public class ClientWindow extends GenericWindow {

    public ClientWindow() {
        super("Settlers of Catan - Client", new Dimension(1024, 768), false, Client.logger);
    }
}

package com.gregswebserver.catan.client;

import com.gregswebserver.catan.userinterface.GenericWindow;

import java.awt.*;

/**
 * Created by Greg on 8/11/2014.
 * Graphics window to provide a GUI to the client.
 * Receives render info from the RenderThread
 */
public class ClientWindow extends GenericWindow {

    public ClientWindow(Client client) {
        super("Settlers of Catan - Client", new Dimension(1024, 768), false, client.logger);
        //TODO: setup canvas and pass it to the RenderThread
        setVisible(true);
    }

    public void setListener(InputListener listener) {
        getContentPane().addKeyListener(listener);
        getContentPane().addMouseListener(listener);
    }

    protected void onClose() {

    }
}

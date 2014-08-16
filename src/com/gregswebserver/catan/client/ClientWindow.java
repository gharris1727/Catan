package com.gregswebserver.catan.client;

import com.gregswebserver.catan.client.graphics.Screen;
import com.gregswebserver.catan.client.input.InputListener;
import com.gregswebserver.catan.userinterface.GenericWindow;

import java.awt.*;

/**
 * Created by Greg on 8/11/2014.
 * Graphics window to provide a GUI to the client.
 * Receives render info from the RenderThread
 */
public class ClientWindow extends GenericWindow {

    //TODO: finish getting the render thread connected.

    private Client client;
    private Screen screen;

    public ClientWindow(Client client) {
        super("Settlers of Catan - Client", new Dimension(1024, 768), false, client.logger);
        this.client = client;
        Screen screen = new Screen(512, 384, 2.0);
        add(screen.getCanvas());
        pack();
        setVisible(true);
    }

    public void setListener(InputListener listener) {
        getContentPane().addKeyListener(listener);
        getContentPane().addMouseListener(listener);
    }

    public Screen getScreen() {
        return screen;
    }

    protected void onClose() {
        client.shutdown();
    }
}

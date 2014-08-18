package com.gregswebserver.catan.client;

import com.gregswebserver.catan.client.input.InputListener;
import com.gregswebserver.catan.client.renderer.RenderEvent;
import com.gregswebserver.catan.client.renderer.RenderEventType;
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
    private Canvas canvas;

    public ClientWindow(Client client) {
        super("Settlers of Catan - Client", new Dimension(1024, 768), true, client.logger);
        this.client = client;
        onResize(getSize()); //Prompts an event loop that eventually calls setCanvas and sets this window to visible.
    }

    public void setListener(InputListener listener) {
        getContentPane().addKeyListener(listener);
        getContentPane().addMouseListener(listener);
        getContentPane().addMouseMotionListener(listener);
        getContentPane().addMouseWheelListener(listener);
    }

    protected void onClose() {
        client.shutdown();
    }

    protected void onResize(Dimension size) {
        client.addEvent(new RenderEvent(this, RenderEventType.Window_Resize, size));
    }

    public void setCanvas(Canvas newCanvas) {
        if (canvas != null)
            remove(canvas);
        canvas = newCanvas;
        add(canvas);
//        Main.logger.debug("PACK");
//        pack();
        setVisible(true);
        client.addEvent(new RenderEvent(this, RenderEventType.Render_Enable, null));
    }
}

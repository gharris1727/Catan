package com.gregswebserver.catan.client;

import com.gregswebserver.catan.client.event.RenderEvent;
import com.gregswebserver.catan.client.event.RenderEventType;
import com.gregswebserver.catan.client.input.InputListener;
import com.gregswebserver.catan.common.GenericWindow;

import java.awt.*;

/**
 * Created by Greg on 8/11/2014.
 * Graphics window to provide a GUI to the client.
 * Receives render info from the RenderThread
 */
public class ClientWindow extends GenericWindow {

    private Client client;
    private Canvas canvas;
    private InputListener listener;
    //Field to prevent the onResize function from spamming resize requests when there is one still processing.
    private boolean resizing = false;

    public ClientWindow(Client client) {
        super("Settlers of Catan - Client", new Dimension(640, 480), true, client.logger);
        this.client = client;
    }

    public void setListener(InputListener listener) {
        this.listener = listener;
    }

    protected void onClose() {
        client.shutdown();
    }

    protected void onResize(Dimension size) {
        if (!resizing) {
            client.addEvent(new RenderEvent(this, RenderEventType.Window_Resize, size));
        }
        resizing = true;
    }

    public void setCanvas(Canvas newCanvas) {
        if (canvas != null) {
            canvas.removeKeyListener(listener);
            canvas.removeMouseListener(listener);
            canvas.removeMouseMotionListener(listener);
            canvas.removeMouseWheelListener(listener);
            remove(canvas);
        }
        canvas = newCanvas;
        add(canvas);
        setVisible(true);
        canvas.addKeyListener(listener);
        canvas.addMouseListener(listener);
        canvas.addMouseMotionListener(listener);
        canvas.addMouseWheelListener(listener);
        client.addEvent(new RenderEvent(this, RenderEventType.Render_Enable, null));
        resizing = false;
    }

    public String toString() {
        return client + "ClientWindow";
    }
}

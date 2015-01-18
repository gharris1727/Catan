package com.gregswebserver.catan.client;

import com.gregswebserver.catan.client.event.RenderEvent;
import com.gregswebserver.catan.client.event.RenderEventType;
import com.gregswebserver.catan.client.graphics.graphics.ScreenCanvas;
import com.gregswebserver.catan.client.input.InputListener;
import com.gregswebserver.catan.common.GenericWindow;

import java.awt.Dimension;

/**
 * Created by Greg on 8/11/2014.
 * Graphics window to provide a GUI to the client.
 * Receives render info from the RenderThread
 */
public class ClientWindow extends GenericWindow {

    private Client client;
    private ScreenCanvas canvas;
    private InputListener listener;
    //Field to prevent the onResize function from spamming resize requests when there is one still processing.
    private boolean resizing = false;

    public ClientWindow(Client client) {
        super("Settlers of Catan - Client", new Dimension(640, 480), true, client.logger);
        this.client = client;
    }

    public void setListener(InputListener listener) {
        if (canvas != null) {
            removeListeners();
        }
        this.listener = listener;
        addListeners();
    }

    private void removeListeners() {
        if (canvas != null && listener != null) {
            canvas.removeKeyListener(listener);
            canvas.removeMouseListener(listener);
            canvas.removeMouseMotionListener(listener);
            canvas.removeMouseWheelListener(listener);
        }
    }

    private void addListeners() {
        if (canvas != null && listener != null) {
            canvas.addKeyListener(listener);
            canvas.addMouseListener(listener);
            canvas.addMouseMotionListener(listener);
            canvas.addMouseWheelListener(listener);
        }
    }

    protected void onClose() {
        client.shutdown();
    }

    protected void onResize(Dimension size) {
        if (!resizing) {
            resizing = true;
            if (canvas != null)
                synchronized (canvas) { //Make sure the existing canvas isn't currently rendering.
                    removeListeners();
                    remove(canvas);
                }
            canvas = new ScreenCanvas(size);
            add(canvas);
            addListeners();
            setVisible(true);
            client.addEvent(new RenderEvent(this, RenderEventType.Canvas_Update, canvas));
            resizing = false;
        }
    }

    public String toString() {
        return client + "ClientWindow";
    }
}

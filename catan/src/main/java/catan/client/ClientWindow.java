package catan.client;

import catan.client.graphics.graphics.ScreenCanvas;
import catan.client.input.InputListener;
import catan.client.renderer.RenderEvent;
import catan.client.renderer.RenderEventType;
import catan.common.CoreWindow;

import java.awt.Dimension;

/**
 * Created by Greg on 8/11/2014.
 * Graphics window to provide a GUI to the client.
 * Receives render info from the RenderThread
 */
public class ClientWindow extends CoreWindow {

    private final Client client;
    private final InputListener listener;
    private ScreenCanvas canvas;
    //Field to prevent the onResize function from spamming setMask requests when there is one still processing.
    private boolean resizing;

    public ClientWindow(Client client, InputListener listener) {
        super("Settlers of Catan - Client", new Dimension(800, 600), true);
        this.client = client;
        this.listener = listener;
        display();
    }

    private void removeListeners() {
        if (canvas != null) {
            canvas.removeKeyListener(listener);
            canvas.removeMouseListener(listener);
            canvas.removeMouseMotionListener(listener);
            canvas.removeMouseWheelListener(listener);
        }
    }

    private void addListeners() {
        if (canvas != null) {
            canvas.addKeyListener(listener);
            canvas.addMouseListener(listener);
            canvas.addMouseMotionListener(listener);
            canvas.addMouseWheelListener(listener);
        }
    }

    @Override
    protected void onClose() {
        client.addEvent(new ClientEvent(this, ClientEventType.Quit_All, null));
    }

    @SuppressWarnings("SynchronizeOnNonFinalField")
    @Override
    protected void onResize(Dimension size) {
        // This event can get spammed when the window is actually resizing, so we need to guard against that.
        // The shouldResize variable is set to true for the one first function call when we weren't already resizing.
        boolean shouldResize = false;
        // This synchronization block ensures that the decision on running the real resize is atomic.
        synchronized (this) {
            if (!resizing) {
                shouldResize = true;
                resizing = true;
            }
        }
        // Now the slow code that actually does the resizing can happen asynchronously.
        if (shouldResize) {
            // Make sure if there is an existing canvas it isn't currently rendering.
            if (canvas != null)
                synchronized (canvas) {
                    removeListeners();
                    remove(canvas);
                }
            // Create a new screen canvas of the proper size.
            canvas = new ScreenCanvas(size);
            // Add it to the JFrame for rendering
            add(canvas);
            // Add all of the input listeners.
            addListeners();
            // This only takes effect on startup, but ensure that the frame is visible to the user.
            setVisible(true);
            // Now tell the renderer about the new canvas.
            client.addEvent(new RenderEvent(this, RenderEventType.Canvas_Update, canvas));
            // Finally update the resizing variable when we are finished resizing.
            synchronized (this) {
                resizing = false;
            }
        }
    }

    public String toString() {
        return "ClientWindow";
    }
}

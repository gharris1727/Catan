package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.RenderEvent;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.ScreenCanvas;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.event.ThreadStop;

import java.awt.*;

/**
 * Created by Greg on 8/13/2014.
 * Render engine with a event queue on the front for data input.
 * Stores the clickable information for the InputListener to use.
 */
public class RenderThread extends QueuedInputThread<RenderEvent> {

    private final RenderManager manager;
    private ScreenCanvas canvas;

    public RenderThread(Client client, RenderManager manager) {
        super(client.logger);
        this.manager = manager;
    }

    @Override
    protected void execute() throws ThreadStop {
        //Process the event queue, blocking for every event. Only re-renders what needs to be re-rendered.
        RenderEvent event = getEvent(false);
        if (event != null) {
            switch (event.getType()) {
                case Canvas_Update:
                    canvas = (ScreenCanvas) event.getPayload();
                    manager.setMask(new RectangularMask(canvas.getSize()));
                    break;
                case Animation_Step:
                    manager.step();
                    break;
            }
        } else { //No event to be processed this round.
            if (canvas != null)
                synchronized (canvas) {
                    Graphic screen = canvas.getGraphic();
                    screen.clear();
                    Graphic graphic = manager.getGraphic();
                    if (graphic != null) graphic.renderTo(screen, new Point(), 0);
                    canvas.render();
                }
        }
    }

    public String toString() {
        return "RenderThread";
    }

}

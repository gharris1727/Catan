package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.RenderEvent;
import com.gregswebserver.catan.client.graphics.graphics.ScreenCanvas;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.event.ThreadStop;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.profiler.TimeSlice;

/**
 * Created by Greg on 8/13/2014.
 * Render engine with a event queue on the front for data input.
 * Stores the clickable information for the InputListener to use.
 */
public class RenderThread extends QueuedInputThread<RenderEvent> {

    private final RenderManager manager;
    private final TimeSlice root;
    private ScreenCanvas canvas;

    public RenderThread(Client client, RenderManager manager) {
        super(client.logger);
        this.manager = manager;
        root = new TimeSlice("root");
    }

    @Override
    protected void execute() throws ThreadStop {
        //Process the event queue, blocking for every event. Only re-renders what needs to be re-rendered.
        RenderEvent event = getEvent(false);
        root.reset();
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
            if (canvas != null) {
                manager.assertRenderable();
                boolean accelerated = canvas.render(manager.getGraphic());
                //TODO: find where the slow graphics are coming from and fix.
                if (!accelerated)
                    logger.log("Slow graphic sent to the screen.", LogLevel.WARN);
            }
        }
        root.mark();
        root.addChild(manager.getRenderTime());
        if (canvas != null)
            root.addChild(canvas.getRenderTime());
        root.waitUntil(33*TimeSlice.MILLION);
        if (root.getTime() > 33*TimeSlice.MILLION) {
            String message = "Rendering at less than 30Hz\n" + root.print(2, 0);
            logger.log(message, LogLevel.WARN);
        }
    }

    public String toString() {
        return "RenderThread";
    }

}

package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.event.EventQueueThread;
import com.gregswebserver.catan.log.Logger;

/**
 * Created by Greg on 8/13/2014.
 * Render engine with a event queue on the front for data input.
 */
public class RenderThread extends EventQueueThread<RenderEvent> {

    public RenderThread(Logger logger) {
        super(logger);
    }

    public void execute() {
        //Process the event queue without blocking, allowing the renderer to run more than once per event.
        RenderEvent event = getEvent(false);
    }
}

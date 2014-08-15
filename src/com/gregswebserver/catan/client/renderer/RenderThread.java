package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.input.ScreenHitbox;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.log.Logger;

/**
 * Created by Greg on 8/13/2014.
 * Render engine with a event queue on the front for data input.
 * Stores the hitbox information for the InputListener to use.
 */
public class RenderThread extends QueuedInputThread<RenderEvent> {

    private ScreenHitbox screenHitbox;

    public RenderThread(Logger logger) {
        super(logger);
    }

    public void execute() {
        //Process the event queue without blocking, allowing the renderer to run more than once per event.
        RenderEvent event = getEvent(false);
        //TODO: implement me
    }

    public ScreenHitbox getScreenHitbox() {
        return screenHitbox;
    }
}

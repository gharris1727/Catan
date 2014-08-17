package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.graphics.Screen;
import com.gregswebserver.catan.client.input.ColorHitbox;
import com.gregswebserver.catan.client.input.Hitbox;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.event.ThreadStop;
import com.gregswebserver.catan.log.Logger;

import java.util.HashSet;

/**
 * Created by Greg on 8/13/2014.
 * Render engine with a event queue on the front for data input.
 * Stores the hitbox information for the InputListener to use.
 */
public class RenderThread extends QueuedInputThread {

    private ColorHitbox hitbox;
    private Screen screen;
    //Large, resource intensive background objects. Majority of objects are here.
    //Gets relatively few updates and it's content is mostly static.
    private HashSet<ScreenObject> backgroundBuffer;
    //Small, light animations and things that need to be in front.
    //Stuff like cursors and menus.
    private HashSet<ScreenObject> foregroundBuffer;

    public RenderThread(Logger logger) {
        super(logger);
        //Render thread intentionally does not have access to the Client, no data should ever come back out of here.
    }

    public void execute() throws ThreadStop {
        //Process the event queue without blocking, allowing the renderer to run more than once per event.
        RenderEvent event = (RenderEvent) getEvent(false);
        screen.draw();
        screen.show();
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
}

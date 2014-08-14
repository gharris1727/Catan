package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.event.InternalEvent;

/**
 * Created by Greg on 8/12/2014.
 * Event for communicating with the rendering thread on the client application.
 * Contains references to objects that need to be rendered.
 */
public class RenderEvent extends InternalEvent {

    public final RenderEventType type;
    public final Object data;

    public RenderEvent(Object origin, RenderEventType type, Object data) {
        super(origin);
        this.type = type;
        this.data = data;
    }
}

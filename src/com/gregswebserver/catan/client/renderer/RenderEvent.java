package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.event.InternalEvent;

/**
 * Created by Greg on 8/12/2014.
 * Event for communicating with the rendering thread on the client application.
 * Contains references to objects that need to be rendered.
 */
public class RenderEvent extends InternalEvent {

    public RenderEvent(Object origin, RenderEventType type, Object payload) {
        super(origin, type, payload);
    }

    public RenderEventType getType() {
        return (RenderEventType) type;
    }
}

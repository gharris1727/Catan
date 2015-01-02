package com.gregswebserver.catan.client.event;

import com.gregswebserver.catan.common.event.InternalEvent;

/**
 * Created by Greg on 8/12/2014.
 * Event for communicating with the rendering event on the client application.
 * Contains references to objects that need to be rendered.
 */
public class RenderEvent extends InternalEvent<RenderEventType> {

    public RenderEvent(Object origin, RenderEventType type, Object payload) {
        super(origin, type, payload);
    }
}

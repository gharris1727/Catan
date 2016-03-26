package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.common.event.InternalEvent;

/**
 * Created by Greg on 8/12/2014.
 * Event that pertains to communicating with the rendering system.
 */
public final class RenderEvent extends InternalEvent<RenderEventType> {

    public RenderEvent(Object origin, RenderEventType type, Object payload) {
        super(origin, type, payload);
    }
}

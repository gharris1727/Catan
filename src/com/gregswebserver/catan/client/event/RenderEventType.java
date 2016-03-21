package com.gregswebserver.catan.client.event;

import com.gregswebserver.catan.client.graphics.graphics.ScreenCanvas;
import com.gregswebserver.catan.common.event.EventType;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with the RenderEvent indicating what is to be done with the data at the renderer.
 * General render interface information.
 * References are used to generate InputListener actions.
 */
public enum RenderEventType implements EventType {

    Canvas_Update(ScreenCanvas.class), //Update the screen canvas with a new one.
    Animation_Step(null); //Step all animations.

    private final Class payloadType;

    RenderEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}

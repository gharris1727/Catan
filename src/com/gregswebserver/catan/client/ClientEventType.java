package com.gregswebserver.catan.client;

import com.gregswebserver.catan.client.graphics.ScreenArea;
import com.gregswebserver.catan.event.EventPayloadException;
import com.gregswebserver.catan.event.EventType;

import java.awt.*;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with a ClientEvent to send further information as to what the client needs to do.
 * Stores general client actions.
 */
public enum ClientEventType implements EventType {

    Net_Connect(null), //Connects to a remote server and opens a socket.
    Net_Disconnect(null), //Disconnects from a remote server.
    Canvas_Update(Canvas.class), //Event sent from the renderThread which updates the Canvas object kept in the ClientWindow.
    Hitbox_Update(ScreenArea.class); //Event sent from the renderThread that updates the hitbox information in ClientListener.

    private Class payloadType;

    ClientEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    public void checkPayload(Object o) {
        if (payloadType != null && o != null && o.getClass().isAssignableFrom(payloadType))
            throw new EventPayloadException(o, payloadType);
    }

    public Class getType() {
        return payloadType;
    }
}

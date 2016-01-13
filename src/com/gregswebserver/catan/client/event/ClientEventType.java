package com.gregswebserver.catan.client.event;

import com.gregswebserver.catan.client.input.Clickable;
import com.gregswebserver.catan.common.event.EventType;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with a ClientEvent to send further information as to what the client needs to do.
 * Stores general client actions.
 */
public enum ClientEventType implements EventType {

    Startup(null), //Event that starts the client from inside its own thread.
    Quit_All(null), //Kills all client processes and exits the game.
    Root_Clickable_Update(Clickable.class); //Event sent from the renderThread that updates the clickable information in ClientListener.

    private Class payloadType;

    ClientEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}

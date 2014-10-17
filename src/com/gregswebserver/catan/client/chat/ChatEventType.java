package com.gregswebserver.catan.client.chat;

import com.gregswebserver.catan.common.event.EventPayloadException;
import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.network.Identity;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent along inside of a ChatEvent to indicate it's purpose.
 * Functions as a routing aid, and allows the server to rebroadcast it the right direction.
 */
public enum ChatEventType implements EventType {

    Broadcast(null),  //Message should be rebroadcast to all clients connected to the server.
    Lobby(null), //Message should be rebroadcast to only the clients in a local Group.
    Private(Identity.class); //Message should be forwarded to only the destination client.

    private Class payloadType;

    ChatEventType(Class payloadType) {
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

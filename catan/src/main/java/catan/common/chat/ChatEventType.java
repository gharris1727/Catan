package catan.common.chat;

import catan.common.crypto.Username;
import catan.common.event.EventType;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent along inside of a ChatEvent to indicate it's purpose.
 * Functions as a routing aid, and allows the server to rebroadcast it the right direction.
 */
public enum ChatEventType implements EventType {

    Broadcast(null),  //Message should be rebroadcast to all clients connected to the server.
    Lobby(null), //Message should be rebroadcast to only the clients in a local Group.
    Private(Username.class); //Message should be forwarded to only the destination client.

    private final Class payloadType;

    ChatEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }

}

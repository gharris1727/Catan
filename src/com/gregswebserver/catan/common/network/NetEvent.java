package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.crypto.AuthToken;
import com.gregswebserver.catan.common.event.GenericEvent;

import java.io.Serializable;

/**
 * Created by Greg on 8/12/2014.
 * Network event for communicating between client and server.
 */
public class NetEvent extends GenericEvent implements Serializable {

    public static final long serialVersionUID = 1L;

    private final AuthToken origin;
    private final NetEventType type;
    private final Object payload;
    private transient NetConnection connection; //Not sent over the network, purely internal reference.

    public NetEvent(AuthToken origin, NetEventType type, Object payload) {
        this.origin = origin;
        this.type = type;
        this.payload = payload;
        type.checkPayload(payload);
    }

    public AuthToken getOrigin() {
        return origin;
    }

    public NetEventType getType() {
        return type;
    }

    public Object getPayload() {
        return payload;
    }

    public NetConnection getConnection() {
        return connection;
    }

    public void setConnection(NetConnection connection) {
        this.connection = connection;
    }

    public String toString() {
        return "[NETWORK] O(" + origin + ") T(" + type + ") P(" + payload + ")";
    }
}

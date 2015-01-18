package com.gregswebserver.catan.common.event;

import com.gregswebserver.catan.common.network.NetConnection;

import java.io.Serializable;

/**
 * Created by Greg on 8/12/2014.
 * Network event for communicating between client and server.
 */
public class NetEvent extends GenericEvent implements Serializable {

    public static final long serialVersionUID = 1L;

    private final Object origin;
    private final NetEventType type;
    private final Object payload;
    private transient NetConnection connection; //Not sent over the network, purely internal reference.

    public NetEvent(Object origin, NetEventType type, Object payload) {
        this.origin = origin;
        this.type = type;
        this.payload = payload;
        type.checkPayload(payload);
    }

    public Object getOrigin() {
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
        return "[NET EVENT] O:" + origin + " T:" + type + " P:" + payload;
    }
}

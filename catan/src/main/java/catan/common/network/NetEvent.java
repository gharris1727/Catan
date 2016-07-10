package catan.common.network;

import catan.common.crypto.AuthToken;
import catan.common.event.GenericEvent;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetEvent)) return false;

        NetEvent netEvent = (NetEvent) o;

        if (origin != null ? !origin.equals(netEvent.origin) : netEvent.origin != null) return false;
        if (type != netEvent.type) return false;
        if (payload != null ? !payload.equals(netEvent.payload) : netEvent.payload != null) return false;
        return connection != null ? connection.equals(netEvent.connection) : netEvent.connection == null;

    }

    @Override
    public int hashCode() {
        int result = origin != null ? origin.hashCode() : 0;
        result = 31 * result + type.hashCode();
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (connection != null ? connection.hashCode() : 0);
        return result;
    }
}

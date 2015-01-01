package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.event.EventPayload;

/**
 * Created by Greg on 8/12/2014.
 * An identity, containing a username. Used to connect to a server and to route ExternalEvents.
 */
public class Identity extends EventPayload {

    public final String username;

    public Identity(String username) {
        this.username = username;
    }

    public String toString() {
        return "Identity: " + username;
    }

    public boolean equals(Object o) {
        return o instanceof Identity && o.toString().equals(toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }
}

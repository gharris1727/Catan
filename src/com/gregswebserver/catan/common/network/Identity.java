package com.gregswebserver.catan.common.network;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Greg on 8/12/2014.
 * An identity, containing a username. Used to connect to a server and to route ExternalEvents.
 */
public class Identity implements Serializable {

    public final String username;
    public final UUID uuid;

    public Identity(String username) {
        this.username = username;
        uuid = UUID.randomUUID();
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

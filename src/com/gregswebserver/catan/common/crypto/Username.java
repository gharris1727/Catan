package com.gregswebserver.catan.common.crypto;

import com.gregswebserver.catan.common.event.EventPayload;

/**
 * Created by Greg on 8/12/2014.
 * A username, used for logging the client in. Used to connect to a server and to route ExternalEvents.
 */
public final class Username extends EventPayload {

    public final String username;

    public Username(String username) {
        this.username = username;
    }

    public String toString() {
        return "Username: " + username;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof Username) {
            Username i = (Username) o;
            return username.equals(i.username);
        } else return false;
    }

    public int hashCode() {
        return username.hashCode();
    }
}

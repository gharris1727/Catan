package com.gregswebserver.catan.network;

/**
 * Created by Greg on 8/12/2014.
 * An identity, containing a username. Used to connect to a server and to route ExternalEvents.
 */
public class Identity {

    public final String username;

    public Identity(String username) {
        this.username = username;
    }
}

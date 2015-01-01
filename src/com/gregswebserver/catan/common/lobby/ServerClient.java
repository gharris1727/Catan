package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.network.Identity;

/**
 * Created by Greg on 10/17/2014.
 * An object that exists to represent a client on the server side.
 */
public class ServerClient extends EventPayload {
    private final Identity identity;
    private final int uniqueID;
    private String displayName;

    public ServerClient(Identity identity, int uniqueID) {
        this.identity = identity;
        this.uniqueID = uniqueID;
        displayName = identity.username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Identity getIdentity() {
        return identity;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public String toString() {
        return "ServerClient " + identity + " UID: " + uniqueID + " Name: " + displayName;
    }
}

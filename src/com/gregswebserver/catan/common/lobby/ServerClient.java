package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.network.Identity;

/**
 * Created by Greg on 10/17/2014.
 * An object that exists to represent a client on the server side.
 */
public class ServerClient extends EventPayload {
    private final int uniqueID;
    private final Identity identity;
    private String displayName;

    public ServerClient(int uniqueID, Identity identity, String displayName) {
        this.uniqueID = uniqueID;
        this.identity = identity;
        this.displayName = displayName;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public Identity getIdentity() {
        return identity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String toString() {
        return "ServerClient UID: " + uniqueID + " Name: " + displayName;
    }
}

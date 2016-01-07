package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.crypto.Username;

/**
 * Created by Greg on 10/17/2014.
 * An object that exists to represent a client on the server side.
 */
public class ServerClient extends EventPayload {
    private final int uniqueID;
    private final Username username;
    private String displayName;

    public ServerClient(int uniqueID, Username username, String displayName) {
        this.uniqueID = uniqueID;
        this.username = username;
        this.displayName = displayName;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public Username getUsername() {
        return username;
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

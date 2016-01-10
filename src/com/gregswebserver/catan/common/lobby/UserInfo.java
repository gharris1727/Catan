package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.crypto.Username;

/**
 * Created by Greg on 10/17/2014.
 * An object containing client info relevant to the other clients.
 */
public class UserInfo extends EventPayload {
    private final Username username;
    private final String displayName;

    public UserInfo(Username username, String displayName) {
        this.username = username;
        this.displayName = displayName;
    }

    public Username getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String toString() {
        return "UserInfo Name: " + displayName;
    }
}

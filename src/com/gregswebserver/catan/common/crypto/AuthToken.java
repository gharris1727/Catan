package com.gregswebserver.catan.common.crypto;

import com.gregswebserver.catan.common.network.Identity;

/**
 * Created by Greg on 1/18/2015.
 * A token sent from the server to client to avoid re-authenicating the client.
 */
public class AuthToken {

    protected final Identity identity;
    protected final int sessionID;

    public AuthToken(Identity identity, int sessionID) {
        this.identity = identity;
        this.sessionID = sessionID;
    }
}

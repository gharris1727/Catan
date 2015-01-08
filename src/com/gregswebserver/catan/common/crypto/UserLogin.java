package com.gregswebserver.catan.common.crypto;

import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.network.Identity;

/**
 * Created by Greg on 10/17/2014.
 * A set of details pertaining to connecting to a remote server.
 */
public class UserLogin extends EventPayload {

    public final Identity identity;
    public final Password password;

    protected UserLogin(Identity name, Password pass) {
        identity = name;
        password = pass;
    }

    public String toString() {
        return identity + " " + password;
    }
}

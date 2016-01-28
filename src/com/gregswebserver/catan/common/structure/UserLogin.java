package com.gregswebserver.catan.common.structure;

import com.gregswebserver.catan.common.crypto.Password;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventPayload;

/**
 * Created by Greg on 10/17/2014.
 * A set of details pertaining to connecting to a remote server.
 */
public class UserLogin extends EventPayload {

    public final Username username;
    public final Password password;

    public UserLogin(Username name, Password pass) {
        username = name;
        password = pass;
    }

    public String toString() {
        return username + " " + password;
    }
}

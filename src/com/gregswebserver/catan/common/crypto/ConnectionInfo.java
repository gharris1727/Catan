package com.gregswebserver.catan.common.crypto;

import com.gregswebserver.catan.common.network.Identity;
import com.gregswebserver.catan.common.network.NetID;

import java.io.Serializable;

/**
 * Created by Greg on 10/17/2014.
 * A set of details pertaining to connecting to a remote server.
 */
public class ConnectionInfo implements Serializable {

    public final Identity identity;
    public final Password password;
    public final NetID remote;

    public ConnectionInfo(Identity identity, Password password, NetID remote) {
        this.identity = identity;
        this.password = password;
        this.remote = remote;
    }
}

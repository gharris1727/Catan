package com.gregswebserver.catan.client.structure;

import com.gregswebserver.catan.common.network.NetID;
import com.gregswebserver.catan.common.structure.UserLogin;

/**
 * Created by Greg on 12/28/2014.
 * All of the details needed for connecting to a remote server.
 */
public class ServerLogin {

    public final NetID remote;
    public final UserLogin login;

    public ServerLogin(NetID remote, UserLogin login) {
        this.remote = remote;
        this.login = login;
    }

    public String toString() {
        return "ServerLogin " + login;
    }
}

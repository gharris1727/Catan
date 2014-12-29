package com.gregswebserver.catan.common.crypto;

import com.gregswebserver.catan.common.network.Identity;
import com.gregswebserver.catan.common.network.NetID;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Greg on 12/28/2014.
 * All of the details needed for connecting to a remote server.
 */
public class ServerLogin {

    public final NetID remote;
    public final UserLogin login;

    public ServerLogin(String host, int port, String user, String pass) {
        NetID tempRemote = null;
        try {
            tempRemote = new NetID(InetAddress.getByName(host), port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        remote = tempRemote;
        login = new UserLogin(new Identity(user), pass);
    }
}

package com.gregswebserver.catan.common.crypto;

import com.gregswebserver.catan.common.network.NetID;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Greg on 1/6/2015.
 * A connection info
 */
public class ConnectionInfo {

    private String remote;
    private String port;
    private String username;
    private Password password;

    public ConnectionInfo(ConnectionInfo other) {
        this.remote = other.remote;
        this.port = other.port;
        this.username = other.username;
        this.password = other.password;
    }

    public ConnectionInfo(String remote, String port, String username, String password) {
        this.remote = remote;
        this.port = port;
        this.username = username;
        this.password = new Password(password);
    }

    public ConnectionInfo() {
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setPassword(String password) {
        this.password = new Password(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ServerLogin createServerLogin() throws NumberFormatException, UnknownHostException {
        int portNumber = Integer.parseInt(port);
        NetID netID = new NetID(InetAddress.getByName(remote), portNumber);
        UserLogin userLogin = new UserLogin(new Username(username), password);
        return new ServerLogin(netID, userLogin);
    }
}

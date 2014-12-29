package com.gregswebserver.catan.server.client;

import com.gregswebserver.catan.common.lobby.Lobby;
import com.gregswebserver.catan.common.network.Identity;
import com.gregswebserver.catan.common.network.ServerConnection;

/**
 * Created by Greg on 10/17/2014.
 * An object that exists to represent a client on the server side.
 */
public class ServerClient {
    private Identity identity;
    private ServerConnection connection;
    private Lobby lobby;

    public ServerClient(ServerConnection connection, Identity identity) {
        this.connection = connection;
        this.identity = identity;
    }

    public Identity getIdentity() {
        return identity;
    }

    public ServerConnection getConnection() {
        return connection;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }
}

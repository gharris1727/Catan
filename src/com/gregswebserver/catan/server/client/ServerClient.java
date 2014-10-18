package com.gregswebserver.catan.server.client;

import com.gregswebserver.catan.client.state.ClientState;
import com.gregswebserver.catan.common.lobby.Lobby;
import com.gregswebserver.catan.common.network.Identity;
import com.gregswebserver.catan.common.network.ServerConnection;

/**
 * Created by Greg on 10/17/2014.
 * An object that exists to represent a client on the server side.
 */
public class ServerClient {
    private ClientState state;
    private Identity identity;
    private ServerConnection connection;
    private Lobby lobby;

    public ServerClient(ServerConnection connection, Identity identity) {
        this.connection = connection;
        this.identity = identity;
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

    public Identity getIdentity() {
        return identity;
    }
}

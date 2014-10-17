package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.lobby.Lobby;
import com.gregswebserver.catan.server.Server;

import java.net.Socket;

/**
 * Created by Greg on 8/11/2014.
 * A client as represented on the server side application.
 */
public class ServerConnection extends NetConnection<Server> {

    private Server server;
    private Lobby lobby;

    public ServerConnection(Server server, Socket socket) {
        super(server, server.getIdentity());
        this.server = server;
        setSocket(socket);
        connect();
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

}


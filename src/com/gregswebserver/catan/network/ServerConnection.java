package com.gregswebserver.catan.network;

import com.gregswebserver.catan.server.Server;
import com.gregswebserver.catan.server.lobby.Lobby;

import java.net.Socket;

/**
 * Created by Greg on 8/11/2014.
 * A client as represented on the server side application.
 */
public class ServerConnection extends NetConnection {

    private Server server;
    private Lobby lobby;
    private Identity identity;

    public ServerConnection(Socket socket, Server server) {
        super(server.logger);
        this.server = server;
        setSocket(socket);
        connect();
    }

    public void process(NetEvent e) {
        server.addEvent(e.getEvent());
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public Identity getIdentity() {
        return identity;
    }
}


package com.gregswebserver.catan.network;

import com.gregswebserver.catan.server.Server;
import com.gregswebserver.catan.server.ServerEvent;
import com.gregswebserver.catan.server.ServerEventType;
import com.gregswebserver.catan.server.lobby.Lobby;

import java.net.Socket;

/**
 * Created by Greg on 8/11/2014.
 * A client as represented on the server side application.
 */
public class ServerConnection extends NetConnection {

    private Server server;
    private Lobby lobby;

    public ServerConnection(Socket socket, Server server) {
        super(server.logger);
        this.server = server;
        setSocket(socket);
        connect();
    }

    public void process(NetEvent e) {
        if (e.event instanceof ServerEvent) {
            ServerEvent sEvent = (ServerEvent) e.event;
            if (sEvent.type.equals(ServerEventType.Client_Connect) || sEvent.type.equals(ServerEventType.Client_Disconnect))
                //If the event is a connect/disconnect message, intercept it and resend it with a reference to this connection instance.
                //Used to move the net connection from connecting to established, and remove reference for disconnects.
                server.addEvent(new ServerEvent(sEvent.origin, sEvent.type, this));
        }
        server.addEvent(e.event);
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }
}


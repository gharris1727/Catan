package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.crypto.UserLogin;
import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.event.GenericEvent;
import com.gregswebserver.catan.common.lobby.ServerClient;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.server.Server;
import com.gregswebserver.catan.server.event.ServerEvent;
import com.gregswebserver.catan.server.event.ServerEventType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Greg on 8/11/2014.
 * A client as represented on the server side application.
 */
public class ServerConnection extends NetConnection {

    public final int uniqueID;
    private final Server server;

    public ServerConnection(Server server, Socket socket, int uniqueID) {
        super(server.logger);
        this.server = server;
        this.socket = socket;
        local = new NetID(socket);
        remote = new NetID((InetSocketAddress) socket.getRemoteSocketAddress());
        this.uniqueID = uniqueID;
    }

    public void run() {
        logger.log("Connecting to new Client...", LogLevel.INFO);
        try {
            open = true;
            in = new ObjectInputStream(socket.getInputStream());
            ControlEvent handshake = (ControlEvent) ((NetEvent) in.readObject()).event;
            //Authenticate the client.
            UserLogin login = (UserLogin) handshake.getPayload();
            boolean auth = server.authenticate(remote, login);
            ExternalEvent reply;
            GenericEvent action;
            if (auth) {
                reply = new ControlEvent(server.getIdentity(), ControlEventType.Handshake_Client_Connect_Success, server.getClientPool());
                ServerClient client = new ServerClient(login.identity, uniqueID);
                action = new ControlEvent(login.identity, ControlEventType.Client_Connect, client);
            } else {
                reply = new ControlEvent(server.getIdentity(), ControlEventType.Handshake_Client_Connect_Failure, "Authentication Failed");
                action = new ServerEvent(this, ServerEventType.Client_Disconnect, uniqueID);
            }
            out = new ObjectOutputStream(socket.getOutputStream());
            sendEvent(reply);
            out.flush();
            server.addEvent(action);
            if (auth) {
                logger.log("Client Connected.", LogLevel.DEBUG);
                receive.start(); //Start processing objects after the connection is established.
            }
        } catch (Exception e) {
            open = false;
            logger.log("Connect Failure", e, LogLevel.ERROR);
        }
    }

    //Pre-process disconnect messages to gracefully close.
    public void process(ExternalEvent event) {
        if (event instanceof ControlEvent) {
            if (((ControlEvent) event).getType() == ControlEventType.Client_Disconnect) {
                open = false;
                logger.log("Received Client Disconnect message, closing...", LogLevel.INFO);
            }
        }
        server.addEvent(event);
    }
}


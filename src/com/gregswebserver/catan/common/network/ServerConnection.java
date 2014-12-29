package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.crypto.UserLogin;
import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.server.Server;
import com.gregswebserver.catan.server.client.ServerClient;
import com.gregswebserver.catan.server.event.ControlEvent;
import com.gregswebserver.catan.server.event.ControlEventType;
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
public class ServerConnection extends NetConnection<Server> {

    public final int uniqueID;

    public ServerConnection(Server server, Socket socket, int uniqueID) {
        super(server);
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
            ExternalEvent reply;
            boolean auth = host.authenticate(remote, login);
            if (auth)
                reply = new ControlEvent(host.getIdentity(), ControlEventType.Client_Connected, uniqueID);
            else
                reply = new ControlEvent(host.getIdentity(), ControlEventType.Client_Disconnected, uniqueID);
            out = new ObjectOutputStream(socket.getOutputStream());
            sendEvent(reply);
            out.flush();
            ServerClient client = new ServerClient(this, login.identity);
            host.addEvent(new ServerEvent(this, ServerEventType.Client_Login, client));
            if (!auth)
                throw new Exception("Authentication Failed");
            logger.log("Client Connected.", LogLevel.INFO);
            receive.start(); //Start processing objects after the connection is established.
        } catch (Exception e) {
            open = false;
            logger.log("Connect Failure", e, LogLevel.ERROR);
        }
    }
}


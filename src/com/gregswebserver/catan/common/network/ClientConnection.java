package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.common.crypto.UserLogin;
import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.server.event.ControlEvent;
import com.gregswebserver.catan.server.event.ControlEventType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Greg on 8/10/2014.
 * Game client that includes graphics, networking, and playing catan games.
 */
public class ClientConnection extends NetConnection<Client> {

    UserLogin info;

    public ClientConnection(Client client, NetID remote, UserLogin info) {
        super(client);
        this.info = info;
        this.remote = remote;
    }

    public void run() {
        logger.log("Connecting to remote Server...", LogLevel.INFO);
        try {
            open = true;
            socket = new Socket(remote.address, remote.port);
            local = new NetID(socket);
            ExternalEvent handshake = new ControlEvent(info.identity, ControlEventType.Client_Connect, info);
            out = new ObjectOutputStream(socket.getOutputStream());
            sendEvent(handshake);
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            logger.log("Received reply from server.", LogLevel.INFO);
            //Receive thread will pass the handshake reply to the client for processing
            //Notifying the client that the connection succeeded.
            receive.start();
        } catch (Exception e) {
            open = false;
            logger.log("No reply from server", e, LogLevel.ERROR);
        }
    }
}

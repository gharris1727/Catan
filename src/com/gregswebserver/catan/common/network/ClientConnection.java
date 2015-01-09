package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.common.crypto.ServerLogin;
import com.gregswebserver.catan.common.crypto.UserLogin;
import com.gregswebserver.catan.common.log.LogLevel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by Greg on 8/10/2014.
 * Game client that includes graphics, networking, and playing catan games.
 */
public class ClientConnection extends NetConnection {

    private final UserLogin info;

    public ClientConnection(Client client, ServerLogin login) {
        super(client);
        this.info = login.login;
        this.remote = login.remote;
    }

    public void run() {
        logger.log("Connecting to remote Server...", LogLevel.INFO);
        try {
            open = true;
            socket = new Socket(remote.address, remote.port);
            local = new NetID(socket);
            out = new ObjectOutputStream(socket.getOutputStream());
            sendEvent(new ControlEvent(info.identity, ControlEventType.Handshake_Client_Connect, info));
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            logger.log("Received reply from server.", LogLevel.DEBUG);
            receive.start();
        } catch (ConnectException ignored) {
            open = false;
            logger.log("Connection Refused", LogLevel.WARN);
        } catch (IOException e) {
            open = false;
            logger.log("Connection Error", e, LogLevel.ERROR);
        }
    }
}

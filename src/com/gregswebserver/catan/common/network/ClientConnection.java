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
        try {
            open = true;
            socket = new Socket(remote.address, remote.port);
            local = new NetID(socket);
            out = new ObjectOutputStream(socket.getOutputStream());
            sendEvent(new ControlEvent(info.identity, ControlEventType.Handshake_Client_Connect, info));
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            receive.start();
        } catch (ConnectException ignored) {
            hostDisconnect("Connect error: connection refused.");
        } catch (IOException e) {
            hostDisconnect("Connect error: " + e.getMessage() + ".");
            logger.log("Connection Error", e, LogLevel.ERROR);
        }
    }

    public void hostDisconnect(String message) {
        open = false;
        logger.log("Disconnected : " + message, LogLevel.DEBUG);
        host.addEvent(new ControlEvent(((Client) host).getIdentity(), ControlEventType.Handshake_Client_Connect_Failure, message));
    }
}

package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.ui.primary.ServerLogin;
import com.gregswebserver.catan.common.crypto.UserLogin;
import com.gregswebserver.catan.common.event.NetEvent;
import com.gregswebserver.catan.common.event.NetEventType;
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
            sendEvent(new NetEvent(host.getToken(), NetEventType.Log_In, info));
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            receive.start();
        } catch (ConnectException ignored) {
            onDisconnect("Connect error: connection refused.");
        } catch (IOException e) {
            onDisconnect("Connect error: " + e.getMessage() + ".");
            logger.log("Connection Link_Error", e, LogLevel.ERROR);
        }
    }
}

package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.crypto.ConnectionInfo;
import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.event.GenericEvent;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.server.event.ControlEvent;
import com.gregswebserver.catan.server.event.ControlEventType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Greg on 8/11/2014.
 * Generic connection class with buffers and IO functions built in.
 */
public abstract class NetConnection<H extends QueuedInputThread<GenericEvent>> {

    private final H host;
    private final Logger logger;
    private final ConnectionInfo info;

    private Socket socket;
    private Thread connect, disconnect, receive;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean open;

    public NetConnection(H owner, ConnectionInfo remote) {
        host = owner;
        logger = owner.logger;
        this.info = remote;
        //Thread to open object buffers for transmitting data.
        connect = new Thread("Connect") {
            public void run() {
                logger.log("Connecting...", LogLevel.INFO);
                try {
                    open = true;
                    ExternalEvent handshake;
                    if (socket == null) {
                        //Originating from the client.
                        socket = new Socket(info.remote.address, info.remote.port);
                        handshake = new ControlEvent(info.identity, ControlEventType.Client_Connect, info);
                    } else {
                        //Originating from the server.
                        handshake = new ControlEvent(info.identity, ControlEventType.Client_Connected, null);
                    }
                    out = new ObjectOutputStream(socket.getOutputStream());
                    sendEvent(handshake);
                    out.flush();
                    in = new ObjectInputStream(socket.getInputStream());
                    logger.log("Connected.", LogLevel.INFO);
                    receive.start(); //Start processing objects after the connection is established.
                } catch (Exception e) {
                    open = false;
                    logger.log("Connect Failure", e, LogLevel.ERROR);
                }
            }
        };
        //Thread to accept incoming objects and sendEvent them to the process implementation.
        receive = new Thread("Receive") {
            public void run() {
                while (open) {
                    try {
                        ExternalEvent e = ((NetEvent) in.readObject()).event;
                        host.addEvent(e);
                    } catch (Exception e) {
                        open = false;
                        logger.log("Receive Failure", e, LogLevel.WARN);
                    }
                }
            }
        };
        //Thread to close the socket and sever the connection.
        disconnect = new Thread("Disconnect") {
            public void run() {
                logger.log("Disconnecting...", LogLevel.INFO);
                try {
                    if (open) {
                        out.flush();
                        socket.close();
                    }
                    open = false;
                    logger.log("Disconnected.", LogLevel.INFO);
                } catch (IOException e) {
                    logger.log("Disconnect Failure", LogLevel.WARN);
                }
            }
        };
    }

    public void sendEvent(ExternalEvent event) {
        try {
            if (!open) throw new IOException("Connection is closed");
            out.writeObject(new NetEvent(info.remote, event));
        } catch (Exception e) {
            logger.log("Send Failure", e, LogLevel.WARN);
        }
    }

    public Identity getIdentity() {
        return info.identity;
    }

    protected void connect() {
        connect.start();
    }

    public void disconnect() {
        disconnect.start();
    }

    public boolean isOpen() {
        return open;
    }
}

package com.gregswebserver.catan.network;

import com.gregswebserver.catan.event.ExternalEvent;
import com.gregswebserver.catan.log.LogLevel;
import com.gregswebserver.catan.log.Logger;
import com.gregswebserver.catan.server.ServerEvent;
import com.gregswebserver.catan.server.ServerEventType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Greg on 8/11/2014.
 * Generic connection class with buffers and IO functions built in.
 */
public abstract class NetConnection {

    protected NetID netID;
    protected Identity identity;
    private Socket socket;
    private Thread connect, disconnect, receive;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean open;
    private Logger logger;

    public NetConnection(final Logger logger) {
        this.logger = logger;
        //Thread to open object buffers for transmitting data.
        connect = new Thread("Connect") {
            public void run() {
                logger.log("Connecting...", LogLevel.INFO);
                try {
                    ExternalEvent handshake = null;
                    if (socket == null) {
                        //Originating from the client.
                        socket = new Socket(netID.address, netID.port);
                        handshake = new ServerEvent(identity, ServerEventType.Client_Connect, null);
                    }
                    out = new ObjectOutputStream(socket.getOutputStream());
                    if (handshake != null)
                        sendEvent(handshake);
                    out.flush();
                    in = new ObjectInputStream(socket.getInputStream());
                    open = true;
                    logger.log("Connected.", LogLevel.INFO);
                    receive.start(); //Start processing objects after the connection is established.
                } catch (Exception e) {
                    logger.log("Connect Failure", e, LogLevel.ERROR);
                }
            }
        };
        //Thread to accept incoming objects and sendEvent them to the process implementation.
        receive = new Thread("Receive") {
            public void run() {
                while (open) {
                    try {
                        process((NetEvent) in.readObject());
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

    public NetID getNetID() {
        return netID;
    }

    public void setNetID(NetID id) {
        netID = id;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity id) {
        identity = id;
    }

    public void setSocket(Socket s) {
        socket = s;
        netID = new NetID(s);
    }

    public void connect() {
        connect.start();
    }

    public void disconnect() {
        disconnect.start();
    }

    public void sendEvent(ExternalEvent event) {
        try {
            if (!open) throw new IOException("Connection is closed");
            out.writeObject(new NetEvent(netID, event));
        } catch (Exception e) {
            logger.log("Send Failure", e, LogLevel.WARN);
        }
    }

    public boolean isOpen() {
        return open;
    }

    protected abstract void process(NetEvent e);
}

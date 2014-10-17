package com.gregswebserver.catan.common.network;

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
    private Logger logger;
    private Identity identity;

    private NetID netID;
    private Socket socket;
    private Thread connect, disconnect, receive;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean open;

    public NetConnection(H owner, Identity id) {
        host = owner;
        logger = owner.logger;
        identity = id;
        //Thread to open object buffers for transmitting data.
        connect = new Thread("Connect") {
            public void run() {
                logger.log("Connecting...", LogLevel.INFO);
                try {
                    ExternalEvent handshake = null;
                    if (socket == null) {
                        //Originating from the client.
                        socket = new Socket(netID.address, netID.port);
                        handshake = new ControlEvent(identity, ControlEventType.Client_Connect, null);
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
}

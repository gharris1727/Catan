package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.event.GenericEvent;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Greg on 8/11/2014.
 * Generic connection class with buffers and IO functions built in.
 */
public abstract class NetConnection<H extends QueuedInputThread<GenericEvent>> implements Runnable {

    protected final H host;
    protected final Logger logger;
    protected NetID local;
    protected NetID remote;

    protected Socket socket;
    protected Thread connect, disconnect, receive;
    protected ObjectInputStream in;
    protected ObjectOutputStream out;
    protected boolean open;

    public NetConnection(H owner) {
        host = owner;
        logger = owner.logger;
        connect = new Thread(this);
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
                    open = false;
                    connect.join();
                    if (in != null)
                        in.close();
                    if (out != null)
                        out.close();
                    if (socket != null)
                        socket.close();
                    receive.join();
                    logger.log("Disconnected.", LogLevel.INFO);
                } catch (Exception e) {
                    logger.log("Disconnect Failure", LogLevel.WARN);
                }
            }
        };
    }

    public void sendEvent(ExternalEvent event) {
        try {
            if (!open) throw new IOException("Connection is closed");
            out.writeObject(new NetEvent(local, event));
        } catch (Exception e) {
            logger.log("Send Failure", e, LogLevel.WARN);
        }
    }

    public void connect() {
        connect.start();
    }

    public void disconnect() {
        disconnect.start();
    }

    //Connect method to be implemented in subclasses.
    public abstract void run();
}

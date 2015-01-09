package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.event.GenericEvent;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Greg on 8/11/2014.
 * Generic connection class with buffers and IO functions built in.
 */
public abstract class NetConnection implements Runnable {

    protected final Logger logger;
    protected NetID local;
    protected NetID remote;
    protected int sessionID;

    protected Socket socket;
    protected Thread connect, disconnect, receive;
    protected ObjectInputStream in;
    protected ObjectOutputStream out;
    protected boolean open;

    public NetConnection(QueuedInputThread<GenericEvent> host) {
        this.logger = host.logger;
        //Thread to establish a connection, uses this class' own run() method from Runnable.
        connect = new Thread(this);
        //Thread to accept incoming objects and sendEvent them to the process implementation.
        receive = new Thread("Receive") {
            public void run() {
                while (open) {
                    try {
                        NetEvent e = ((NetEvent) in.readObject());
                        host.addEvent(e);
                    } catch (EOFException | SocketException ignored) {
                        open = false;
                        logger.log("Connection closed", LogLevel.INFO);
                    } catch (ClassCastException | ClassNotFoundException | IOException e) {
                        open = false;
                        logger.log("Error while receiving", e, LogLevel.ERROR);
                    }
                }
            }
        };
        //Thread to close the socket and sever the connection.
        disconnect = new Thread("Disconnect") {
            public void run() {
                logger.log("Disconnecting...", LogLevel.INFO);
                open = false;
                try {
                    connect.join();
                    if (in != null)
                        in.close();
                    if (out != null)
                        out.close();
                    if (socket != null)
                        socket.close();
                    receive.join();
                } catch (InterruptedException ignored) {
                    //Something happened to the other threads
                } catch (SocketException ignored) {
                    logger.log("Connection Closed", LogLevel.INFO);
                } catch (IOException e) {
                    logger.log("Disconnect Failure", e, LogLevel.ERROR);
                }
                logger.log("Disconnected", LogLevel.INFO);
            }
        };
    }

    public void sendEvent(ExternalEvent event) {
        try {
            out.writeObject(new NetEvent(sessionID, event));
        } catch (SocketException ignored) {
            open = false;
            logger.log("Connection closed", LogLevel.INFO);
        } catch (IOException e) {
            open = false;
            logger.log("Send Failure", e, LogLevel.ERROR);
        }
    }

    public void connect() {
        connect.start();
    }

    public void disconnect() {
        disconnect.start();
    }

    public boolean isOpen() {
        return open;
    }
}

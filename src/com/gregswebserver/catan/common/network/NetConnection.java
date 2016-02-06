package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.CoreThread;
import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.event.NetEvent;
import com.gregswebserver.catan.common.event.NetEventType;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Greg on 8/11/2014.
 * Generic connection class with buffers and IO functions built in.
 */
public abstract class NetConnection implements Runnable {

    protected final Logger logger;
    protected final CoreThread host;
    protected NetID local;
    protected NetID remote;

    protected Socket socket;
    private final Thread connect;
    private final Thread disconnect;
    protected final Thread receive;
    protected ObjectInputStream in;
    protected ObjectOutputStream out;
    protected boolean open;

    public NetConnection(CoreThread host) {
        this.host = host;
        this.logger = host.logger;
        //Thread to establish a connection, uses this class' own run() method from Runnable.
        connect = new Thread(this);
        //Thread to accept incoming objects and sendEvent them to the process implementation.
        receive = new Thread("Receive") {
            @Override
            public void run() {
                while (open) {
                    try {
                        NetEvent e = ((NetEvent) in.readObject());
                        e.setConnection(NetConnection.this);
                        host.addEvent(e);
                    } catch (EOFException | SocketException | StreamCorruptedException ignored) {
                        onDisconnect("Receive failure: connection closed.");
                    } catch (ClassCastException | ClassNotFoundException | IOException e) {
                        onDisconnect("Receive failure: " + e.getMessage() + ".");
                        logger.log("Link_Error while receiving", e, LogLevel.ERROR);
                    }
                }
            }
        };
        //Thread to close the socket and sever the connection.
        disconnect = new Thread("Disconnect") {
            @Override
            public void run() {
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
                    onDisconnect("Disconnect failure: synchronization error.");
                } catch (SocketException ignored) {
                    onDisconnect("Disconnect failure: connection closed.");
                } catch (IOException e) {
                    onDisconnect("Disconnect failure: " + e.getMessage() + ".");
                    logger.log("Disconnect failure", e, LogLevel.ERROR);
                }
            }
        };
    }

    public void sendEvent(NetEvent event) {
        try {
            out.writeObject(event);
            out.flush();
        } catch (SocketException ignored) {
            onDisconnect("Send failure: connection closed.");
        } catch (IOException e) {
            onDisconnect("Send failure: " + e.getMessage() + ".");
            logger.log("Send Failure", e, LogLevel.ERROR);
        }
    }

    public void sendEvent(ExternalEvent event) {
        sendEvent(new NetEvent(host.getToken(), NetEventType.External_Event, event));
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

    protected void onDisconnect(String message) {
        open = false;
        NetEvent error = new NetEvent(host.getToken(), NetEventType.Link_Error, message);
        error.setConnection(this);
        host.addEvent(error);
    }

    @Override
    public String toString() {
        return "NetConnection(" + host + ")";
    }
}

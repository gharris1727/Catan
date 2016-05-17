package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.CoreThread;
import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;

import java.io.EOFException;
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

    protected NetConnection(CoreThread host) {
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
                        process((NetEvent) in.readObject());
                    } catch (SocketException | EOFException e) {
                        onClose(e);
                    } catch (Exception e) {
                        onError("Receive", e);
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
                } catch (Exception e) {
                    onError("Disconnect", e);
                }
            }
        };
    }

    public void sendEvent(NetEvent event) {
        try {
            out.writeObject(event);
            out.flush();
        } catch (Exception e) {
            onError("Send", e);
        }
    }

    public void sendEvent(ExternalEvent event) {
        sendEvent(new NetEvent(host.getToken(), NetEventType.External_Event, event));
    }

    protected void process(NetEvent event) {
        if (event.getType() == NetEventType.Disconnect)
            open = false;
        event.setConnection(this);
        host.addEvent(event);
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

    protected void onError(String message, Exception e) {
        open = false;
        process(new NetEvent(host.getToken(), NetEventType.Link_Error, message + " error: " + e.getMessage()));
        logger.log(message + " error", e, LogLevel.ERROR);
    }

    private void onClose(Exception e) {
        open = false;
        process(new NetEvent(host.getToken(), NetEventType.Disconnect, "Unexpected disconnect: " + e));
    }

    @Override
    public String toString() {
        return "NetConnection(" + host + ")";
    }
}

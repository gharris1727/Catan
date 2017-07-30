package catan.common.network;

import catan.common.crypto.AuthToken;
import catan.common.event.EventProcessor;
import catan.common.event.GenericEvent;
import catan.common.log.LogLevel;
import catan.common.log.Logger;

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
    protected final EventProcessor<GenericEvent> host;
    protected final NetID remote;
    protected NetID local;

    protected Socket socket;
    private final Thread connect;
    private final Thread disconnect;
    protected final Thread receive;
    protected ObjectInputStream in;
    protected ObjectOutputStream out;
    protected boolean open;
    protected AuthToken token;

    protected NetConnection(EventProcessor<GenericEvent> host, Logger logger, NetID remote) {
        this.host = host;
        this.logger = logger;
        this.remote = remote;
        //Thread to establish a connection, uses this class' own run() method from Runnable.
        connect = new Thread(this);
        //Thread to accept incoming objects and sendEvent them to the process implementation.
        receive = new Thread(() -> {
            while (open) {
                try {
                    process((NetEvent) in.readObject());
                } catch (SocketException | EOFException e) {
                    onClose(e);
                } catch (Exception e) {
                    onError("Receive", e);
                }
            }
        }, "Receive");
        //Thread to close the socket and sever the connection.
        disconnect = new Thread(() -> {
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
        }, "Disconnect");
    }

    public void sendEvent(NetEventType type, Object payload) {
        try {
            out.writeObject(new NetEvent(token, type, payload));
            out.flush();
        } catch (Exception e) {
            onError("Send", e);
        }
    }

    public void setToken(AuthToken token) {
        this.token = token;
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
        process(new NetEvent(token, NetEventType.Link_Error, message + " error: " + e.getMessage()));
        logger.log(message + " error", e, LogLevel.ERROR);
    }

    private void onClose(Exception e) {
        open = false;
        process(new NetEvent(token, NetEventType.Disconnect, "Unexpected disconnect: " + e));
    }

    @Override
    public String toString() {
        return "NetConnection(" + host + ")";
    }
}

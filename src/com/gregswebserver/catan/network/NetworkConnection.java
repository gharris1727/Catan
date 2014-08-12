package com.gregswebserver.catan.network;

import com.gregswebserver.catan.log.LogLevel;
import com.gregswebserver.catan.log.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Greg on 8/11/2014.
 * Generic connection class with buffers and IO functions built in.
 */
public abstract class NetworkConnection {

    private InetAddress remote;
    private int port;
    private Socket socket;
    private Thread connect, disconnect, receive;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean open;
    private Logger logger;

    public NetworkConnection(InetAddress remote, int port) {
        this();
        this.remote = remote;
        this.port = port;
    }

    public NetworkConnection(Socket socket, Logger logger) {
        this();
        this.remote = socket.getInetAddress();
        this.port = socket.getPort();
        this.socket = socket;
    }

    private NetworkConnection() {
        //Thread to open object buffers for transmitting data.
        connect = new Thread("Connect") {
            public void run() {
                logger.log("Connecting...", LogLevel.INFO);
                try {
                    if (socket == null) {
                        socket = new Socket(remote, port);
                    }
                    out = new ObjectOutputStream(socket.getOutputStream());
                    out.flush();
                    in = new ObjectInputStream(socket.getInputStream());
                    open = true;
                    receive.start(); //Start processing objects after the connection is established.
                } catch (Exception e) {
                    logger.log("Connect Failure", e, LogLevel.ERROR);
                }
            }
        };
        //Thread to accept incoming objects and send them to the process implementation.
        receive = new Thread("Receive") {
            public void run() {
                while (open) {
                    try {
                        receive(in.readObject());
                    } catch (Exception e) {
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
                    out.flush();
                    socket.close();
                    open = false;
                } catch (IOException e) {
                    logger.log("Disconnect Failure", LogLevel.WARN);
                }
            }
        };
    }

    public void connect() {
        connect.start();
    }

    public void disconnect() {
        disconnect.start();
    }

    public void send(Object o) {
        try {
            if (!open) throw new IOException("Connection is closed");
            out.writeObject(o);
        } catch (Exception e) {
            logger.log("Send Failure", e, LogLevel.WARN);
        }
    }

    public boolean isOpen() {
        return open;
    }

    public abstract void receive(Object o);
}

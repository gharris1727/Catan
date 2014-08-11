package com.gregswebserver.catan.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Greg on 8/10/2014.
 * Game client that includes graphics, networking, and playing catan games.
 */
public class Client extends Thread {

    private int port;
    private String host;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private LinkedBlockingQueue<Request> requests;
    private boolean running;
}

package com.gregswebserver.catan.client;

import com.gregswebserver.catan.client.input.InputListener;
import com.gregswebserver.catan.event.EventQueueThread;
import com.gregswebserver.catan.event.GenericEvent;
import com.gregswebserver.catan.log.Logger;
import com.gregswebserver.catan.network.ClientConnection;
import com.gregswebserver.catan.network.NetEvent;

/**
 * Created by Greg on 8/11/2014.
 * Game client handling user input, graceful error handling, local game simulation, and communication to a server.
 */
public class Client extends EventQueueThread<NetEvent> {

    private Client instance;
    private ClientWindow window;
    private InputListener listener;
    private ClientConnection connection;

    public Client() {
        super(new Logger());
        instance = this;
        window = new ClientWindow(instance);
        listener = new InputListener(instance);
        window.setListener(listener);
    }


    public void execute() {
        //Process NetEvents from the input queue.
        GenericEvent event = getEvent(true).getEvent();
    }
}

package com.gregswebserver.catan;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.ui.primary.ConnectionInfo;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.server.Server;

/**
 * Created by greg on 1/22/16.
 * Test class to automate repeated testing.
 */
public class Test {

    public static void main(String[] args) {
        //Create a common logger.
        Logger logger = new Logger();
        logger.useStdOut();
        //Start a server for testing.
        Server server = new Server(logger);
        server.start(25000);
        //Start some clients and trigger some events.
        Client greg = new Client(logger);
        Client bob = new Client(logger);
        //Create login information for the two clients
        ConnectionInfo gregLogin = new ConnectionInfo("localhost", "25000", "Greg");
        gregLogin.setPassword("password");
        ConnectionInfo bobLogin = new ConnectionInfo("localhost", "25000", "Bob");
        bobLogin.setPassword("pw");
        //Add the events to the client event queues.
        greg.addEvent(new UserEvent("Test", UserEventType.Net_Connect, gregLogin));
        //bob.addEvent(new UserEvent("Test", UserEventType.Net_Connect, bobLogin));
    }
}

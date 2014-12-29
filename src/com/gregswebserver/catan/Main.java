package com.gregswebserver.catan;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.ClientEvent;
import com.gregswebserver.catan.client.event.ClientEventType;
import com.gregswebserver.catan.common.crypto.ServerLogin;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.common.util.Statics;
import com.gregswebserver.catan.server.Server;

/**
 * Created by Greg on 8/8/2014.
 * Main game file that contains the main(String[] args) to execute the program from an executable jar.
 */
public class Main {

    public static Logger logger;

    public static void main(String[] args) {
        // Load the startup window to create/login to a server.
        logger = new Logger();
        new Statics(); //Just to initialize all of the values.
        Server server = new Server();
        server.start(25000);
        Client client = new Client();
        client.addEvent(new ClientEvent(null, ClientEventType.Net_Connect, new ServerLogin("localhost", 25000, "greg", "a")));
    }
}

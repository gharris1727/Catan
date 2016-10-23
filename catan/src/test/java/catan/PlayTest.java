package catan;

import catan.client.Client;
import catan.client.input.UserEvent;
import catan.client.input.UserEventType;
import catan.client.structure.ConnectionInfo;
import catan.common.crypto.Username;
import catan.common.log.Logger;
import catan.junit.ManualTests;
import catan.server.Server;
import org.junit.experimental.categories.Category;

/**
 * Created by greg on 1/22/16.
 * Test class to automate repeated testing.
 */
@Category(ManualTests.class)
public class PlayTest {

    @org.junit.Test
    public void gameplayTest() {
        //Create a common logger.
        Logger logger = new Logger();
        logger.useStdOut();
        //Start a server for testing.
        Server server = new Server(logger, 25000);
        //Make sure the server is listening before proceeding, as the creation of a server is asynchronous.
        try {
            while (!server.isListening())
                Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Start some clients and trigger some events.
        Client greg = new Client(logger);
        Client michael = new Client(logger);
        //Create login information for the two clients
        ConnectionInfo gregLogin = new ConnectionInfo("localhost", "25000", "Greg");
        gregLogin.setPassword("password");
        ConnectionInfo michaelLogin = new ConnectionInfo("localhost", "25000", "Michael");
        michaelLogin.setPassword("pw");
        //Add the events to the client event queues.
        greg.addEvent(new UserEvent(null, UserEventType.Net_Connect, gregLogin));
        michael.addEvent(new UserEvent(null, UserEventType.Net_Connect, michaelLogin));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
        greg.addEvent(new UserEvent(null, UserEventType.Lobby_Create, null));
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }
        michael.addEvent(new UserEvent(null, UserEventType.Lobby_Join, new Username("Greg")));
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }
        greg.addEvent(new UserEvent(null, UserEventType.Lobby_Start, null));
    }
}
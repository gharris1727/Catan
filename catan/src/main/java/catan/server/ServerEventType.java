package catan.server;

import catan.common.crypto.Username;
import catan.common.event.EventType;

import java.net.Socket;

/**
 * Created by Greg on 8/13/2014.
 * Several types of events generated as a ServerEvent.
 */
public enum ServerEventType implements EventType {

    Quit_All(null), //Kill all processes in the running server.
    Client_Connect(Socket.class),
    Client_Disconnect(Integer.class),
    User_Connect(Username.class),
    User_Disconnect(Username.class); //Removes a connection from the server after a logout.


    private final Class type;

    ServerEventType(Class type) {
        this.type = type;
    }

    @Override
    public Class getType() {
        return type;
    }

}

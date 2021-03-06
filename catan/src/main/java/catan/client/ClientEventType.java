package catan.client;

import catan.common.event.EventType;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with a ClientEvent to send further information as to what the client needs to do.
 * Stores general client actions.
 */
public enum ClientEventType implements EventType {

    Startup(null), //Event that starts the client from inside its own thread.
    Quit_All(null); //Kills all client processes and exits the game.

    private final Class type;

    ClientEventType(Class type) {
        this.type = type;
    }

    @Override
    public Class getType() {
        return type;
    }
}

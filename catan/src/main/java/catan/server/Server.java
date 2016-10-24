package catan.server;

import catan.common.event.EventProcessor;
import catan.common.event.GenericEvent;
import catan.common.game.event.GameEvent;

/**
 * Created by greg on 10/23/16.
 * An abstract server that can handle incoming events and host a server for external clients.
 */
public interface Server extends EventProcessor<GenericEvent> {

    void broadcastGameEvent(GameEvent event);

    boolean isListening();
}

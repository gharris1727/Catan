package com.gregswebserver.catan.server.lobby;

import com.gregswebserver.catan.event.ExternalEvent;
import com.gregswebserver.catan.event.GenericEvent;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.event.ThreadStop;
import com.gregswebserver.catan.game.CatanGame;
import com.gregswebserver.catan.game.gameplay.GameType;
import com.gregswebserver.catan.network.Identity;
import com.gregswebserver.catan.network.ServerConnection;
import com.gregswebserver.catan.server.Server;

import java.util.HashMap;

/**
 * Created by Greg on 8/21/2014.
 * A server lobby to facilitate starting games and routing chat.
 */
public class Lobby extends QueuedInputThread {

    private Server server;
    private Identity owner;
    private HashMap<Identity, ServerConnection> clients;
    private CatanGame activeGame;
    private GameType gameType;

    public Lobby(Server server, ServerConnection owner) {
        super(server.logger);
        this.server = server;
        clients = new HashMap<>();
        addClient(owner);
    }

    public void addClient(ServerConnection client) {
        clients.put(client.getIdentity(), client);
        client.setLobby(this);
    }

    protected void execute() throws ThreadStop {
        GenericEvent event = getEvent(true);
    }

    public String toString() {
        return "Lobby";
    }

    public HashMap<Identity, ServerConnection> getClients() {
        return clients;
    }

    public void broadcastEvent(ExternalEvent event) {
        //Sends an external event to all clients in the lobby, except for the one who sent it.
        for (ServerConnection connection : clients.values()) {
            if (!connection.getIdentity().equals(event.origin)) {
                connection.sendEvent(event);
            }
        }
    }
}

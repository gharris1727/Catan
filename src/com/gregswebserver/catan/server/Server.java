package com.gregswebserver.catan.server;


import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.chat.ChatEvent;
import com.gregswebserver.catan.client.chat.ChatThread;
import com.gregswebserver.catan.common.event.GenericEvent;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.event.ThreadStop;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.lobby.Lobby;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.network.Identity;
import com.gregswebserver.catan.common.network.ServerConnection;
import com.gregswebserver.catan.server.event.ControlEvent;
import com.gregswebserver.catan.server.event.ControlEventType;
import com.gregswebserver.catan.server.event.ServerEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Greg on 8/9/2014.
 * Server architecture to allow for multi-player over internet.
 */
public class Server extends QueuedInputThread<GenericEvent> {

    private final Identity identity;
    private final ArrayList<ServerConnection> connecting;
    private final HashMap<Identity, ServerConnection> established;
    private final HashMap<Identity, Lobby> lobbies;
    private final ServerWindow window;
    private final Thread listen;
    //Other sub-QueuedInputThreads
    private final ChatThread globalChat;
    private boolean listening;
    private ServerSocket socket;

    public Server() {
        super(Main.logger); //TODO: REMOVE ME!
//        super(new Logger());
        identity = new Identity("[SERVER]");
        connecting = new ArrayList<>();
        established = new HashMap<>();
        lobbies = new HashMap<>();
        window = new ServerWindow(this);
        listen = new Thread("Listen") {
            public void run() {
                logger.log("Listening...", LogLevel.INFO);
                try {
                    listening = true;
                    while (listening) {
                        Socket clientSocket = socket.accept();
                        ServerConnection newClient = new ServerConnection(Server.this, clientSocket);
                        connecting.add(newClient);
                    }
                } catch (Exception e) {
                    logger.log("Listen Failure", e, LogLevel.ERROR);
                }

            }
        };
        globalChat = new ChatThread(logger);
    }

    public void start(int port) {
        try {
            if (port <= 1024) throw new IOException("Port Number Reserved");
            socket = new ServerSocket(port);
            listen.start();
        } catch (IOException e) {
            logger.log("Server connection failure", e, LogLevel.ERROR);
        }
    }

    public void execute() throws ThreadStop {
        //Process events from the input queue.
        GenericEvent event = getEvent(true);
        if (event instanceof ChatEvent) {
            ChatEvent cEvent = (ChatEvent) event;
            switch (cEvent.getType()) {
                case Broadcast:
                    for (ServerConnection chatConnection : established.values()) {
                        chatConnection.sendEvent(cEvent);
                    }
                    break;
                case Lobby:
                    Lobby lobby = established.get(cEvent.getOrigin()).getLobby();
                    if (lobby != null)
                        lobby.broadcastEvent(cEvent);
                    //Ignore the event if the player is not in a lobby.
                    break;
                case Private:
                    ServerConnection otherClient = established.get(cEvent.getPayload());
                    if (otherClient != null)
                        otherClient.sendEvent(cEvent);
                    //Ignore the event if the destination client does not exist.
                    break;
            }
        }
        if (event instanceof GameEvent) {
            GameEvent gEvent = (GameEvent) event;
            Lobby lobby = established.get(gEvent.getOrigin()).getLobby();
            if (lobby != null)
                lobby.broadcastEvent(gEvent);
            //Ignore the event if the client is not in a game lobby.
        }
        if (event instanceof ServerEvent) {
            ServerEvent sEvent = (ServerEvent) event;
            switch (sEvent.getType()) {
                case Client_Connection:
                    Identity id = (Identity) sEvent.getOrigin();
                    ServerConnection clientConnect = (ServerConnection) sEvent.getPayload();
                    //If the connection exists, move it. (should always happen...)
                    //If it doesn't exist, then we wont lose the reference.
                    //Also prevents null pointers.
                    if (connecting.remove(clientConnect))
                        established.put(id, clientConnect);
                    break;
                case Client_Disconnection:
                    ServerConnection clientDisconnect = (ServerConnection) sEvent.getPayload();
                    clientDisconnect.disconnect();
                    //Checks each list for removing clients.
                    if (!connecting.remove(clientDisconnect))
                        established.remove(sEvent.getOrigin());
                    break;
            }
        }
        if (event instanceof ControlEvent) {
            ControlEvent cEvent = (ControlEvent) event;
            switch (cEvent.getType()) {
                case Lobby_Create:
                    createLobby(cEvent.getOrigin());
                    break;
                case Lobby_Delete:
                    deleteLobby(cEvent.getOrigin());
                    break;
                case Lobby_Join:
                    joinLobby(cEvent.getOrigin(), (Identity) cEvent.getPayload());
                    break;
                case Lobby_Leave:
                    leaveLobby(cEvent.getOrigin(), (Identity) cEvent.getPayload());
                    break;
            }
        }
    }

    private void createLobby(Identity owner) {
        //If this person already owns a lobby (somehow?) then delete it.
        deleteLobby(owner);
        ServerConnection ownerConnection = established.get(owner);
        Lobby lobby = new Lobby(ownerConnection);
        lobbies.put(owner, lobby);
    }

    private void deleteLobby(Identity owner) {
        Lobby lobby = lobbies.remove(owner);
        if (lobby != null)
            lobby.broadcastEvent(new ControlEvent(owner, ControlEventType.Lobby_Delete, null));
    }

    private void joinLobby(Identity client, Identity owner) {
        ServerConnection clientConnection = established.get(client);
        Lobby lobby = lobbies.get(owner);
        lobby.addClient(clientConnection);
        lobby.broadcastEvent(new ControlEvent(identity, ControlEventType.Lobby_Update, null));
    }

    private void leaveLobby(Identity client, Identity owner) {
        ServerConnection clientConnection = established.get(client);
        Lobby lobby = lobbies.get(owner);
        lobby.addClient(clientConnection);
        lobby.broadcastEvent(new ControlEvent(client, ControlEventType.Lobby_Leave, null));
    }


    public void shutdown() {
        for (ServerConnection connection : established.values()) {
            connection.disconnect();
        }
        window.dispose();
        listening = false;
        try {
            socket.close();
        } catch (IOException e) {
            logger.log("ServerSocket Close error", e, LogLevel.WARN);
        }
        stop();
    }

    public Identity getIdentity() {
        return identity;
    }

    public String toString() {
        return "Server";
    }
}

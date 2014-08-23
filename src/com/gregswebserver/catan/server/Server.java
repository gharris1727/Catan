package com.gregswebserver.catan.server;


import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.chat.ChatEvent;
import com.gregswebserver.catan.client.game.GameEvent;
import com.gregswebserver.catan.event.GenericEvent;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.event.ThreadStop;
import com.gregswebserver.catan.log.LogLevel;
import com.gregswebserver.catan.network.Identity;
import com.gregswebserver.catan.network.ServerConnection;
import com.gregswebserver.catan.server.lobby.Lobby;
import com.gregswebserver.catan.server.lobby.LobbyUpdate;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Greg on 8/9/2014.
 * Server architecture to allow for multi-player over internet.
 */
public class Server extends QueuedInputThread {

    private ArrayList<ServerConnection> connecting;
    private HashMap<Identity, ServerConnection> established;
    private HashMap<Identity, Lobby> lobbies;
    private ServerSocket socket;
    private Thread listen;
    private boolean listening;
    private ServerWindow window;
    private Server instance;
    private Identity identity;

    public Server() {
        super(Main.logger); //TODO: REMOVE ME!
//        super(new Logger());
        instance = this;
        identity = new Identity("[SERVER]");
        connecting = new ArrayList<>();
        established = new HashMap<>();
        window = new ServerWindow(this);
        listen = new Thread("Listen") {
            public void run() {
                logger.log("Listening...", LogLevel.INFO);
                try {
                    listening = true;
                    while (listening) {
                        Socket clientSocket = socket.accept();
                        ServerConnection newClient = new ServerConnection(clientSocket, instance);
                        connecting.add(newClient);
                    }
                } catch (Exception e) {
                    logger.log("Listen Failure", e, LogLevel.ERROR);
                }

            }
        };
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
            switch (cEvent.type) {
                case Broadcast:
                    for (ServerConnection chatConnection : established.values()) {
                        chatConnection.sendEvent(cEvent);
                    }
                    break;
                case Lobby:
                    Lobby chatLobby = established.get(cEvent.origin).getLobby();
                    if (chatLobby != null)
                        chatLobby.broadcastEvent(cEvent);
                    //Ignore the event if the player is not in a lobby.
                    break;
                case Private:
                    ServerConnection chatConnection = established.get(cEvent.destination);
                    if (chatConnection != null)
                        chatConnection.sendEvent(cEvent);
                    //Ignore the event if the destination client does not exist.
                    break;
            }
        }
        if (event instanceof GameEvent) {
            GameEvent gEvent = (GameEvent) event;
            Lobby chatLobby = established.get(gEvent.origin).getLobby();
            if (chatLobby != null)
                chatLobby.broadcastEvent(gEvent);
            //Ignore the event if the client is not in a game lobby.
        }
        if (event instanceof ServerEvent) {
            ServerEvent sEvent = (ServerEvent) event;
            switch (sEvent.type) {
                case Client_Connect:
                    Identity id = sEvent.origin;
                    ServerConnection clientConnect = (ServerConnection) sEvent.data;
                    //If the connection exists, move it. (should always happen...)
                    //If it doesn't exist, then we wont lose the reference.
                    //Also prevents null pointers.
                    if (connecting.remove(clientConnect))
                        established.put(id, clientConnect);
                    break;
                case Client_Disconnect:
                    ServerConnection clientDisconnect = (ServerConnection) sEvent.data;
                    clientDisconnect.disconnect();
                    //Checks each list for removing clients.
                    if (!connecting.remove(clientDisconnect))
                        established.remove(sEvent.origin);
                    break;
                case Lobby_Create:
                    createLobby(sEvent.origin);
                    break;
                case Lobby_Delete:
                    deleteLobby(sEvent.origin);
                    break;
                case Lobby_Join:
                    joinLobby(sEvent.origin, (Identity) sEvent.data);
                    break;
                case Lobby_Leave:
                    leaveLobby(sEvent.origin, (Identity) sEvent.data);
                    break;
            }
        }
    }

    private void createLobby(Identity owner) {
        //If this person already owns a lobby (somehow?) then delete it.
        deleteLobby(owner);
        ServerConnection ownerConnection = established.get(owner);
        Lobby lobby = new Lobby(this, ownerConnection);
        lobbies.put(owner, lobby);
    }

    private void deleteLobby(Identity owner) {
        Lobby lobby = lobbies.remove(owner);
        if (lobby != null)
            lobby.broadcastEvent(new ServerEvent(owner, ServerEventType.Lobby_Delete, null));
    }

    private void joinLobby(Identity client, Identity owner) {
        ServerConnection clientConnection = established.get(client);
        Lobby lobby = lobbies.get(owner);
        lobby.addClient(clientConnection);
        lobby.broadcastEvent(new ServerEvent(identity, ServerEventType.Lobby_Update, new LobbyUpdate(lobbies)));

    }

    private void leaveLobby(Identity client, Identity owner) {
        ServerConnection clientConnection = established.get(client);
        Lobby lobby = lobbies.get(owner);
        lobby.addClient(clientConnection);
        lobby.broadcastEvent(new ServerEvent(client, ServerEventType.Lobby_Leave, new LobbyUpdate(lobbies)));
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

    public String toString() {
        return "Server";
    }
}
